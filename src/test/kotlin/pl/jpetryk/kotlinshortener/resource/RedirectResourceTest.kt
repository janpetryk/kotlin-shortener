package pl.jpetryk.kotlinshortener.resource

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import pl.jpetryk.kotlinshortener.domain.Link
import pl.jpetryk.kotlinshortener.repo.LinkRepository

@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
class RedirectResourceTest {

    @Autowired
    private lateinit var redirectResource: RedirectResource

    @Autowired
    private lateinit var linkRepository: LinkRepository

    @Test
    fun linkWithWrongHash() {
        val response = redirectResource.redirect("asd")
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testRedirect() {
        val originalUrl = "http://onet.pl"
        val redirectHash = "asdaa"
        linkRepository.save(Link(originalUrl = originalUrl, redirectHash = redirectHash))
        val response = redirectResource.redirect(redirectHash)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FOUND)
        assertThat(response.headers.get("Location")).contains(originalUrl)
    }

}