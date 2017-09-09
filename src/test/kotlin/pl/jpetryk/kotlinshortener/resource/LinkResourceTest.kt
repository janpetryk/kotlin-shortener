package pl.jpetryk.kotlinshortener.resource

import org.springframework.beans.factory.annotation.Autowired
import pl.jpetryk.kotlinshortener.repo.LinkRepository

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.context.SpringBootTest
import pl.jpetryk.kotlinshortener.model.LinkDto
import org.springframework.http.HttpStatus

@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
class LinkResourceTest {

    @Autowired
    lateinit var linkRepository: LinkRepository

    @Autowired
    lateinit var linkResource: LinkResource;

    @Test
    fun sendBadRequest() {
        assertThat(linkResource.createLink(LinkDto()).statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(linkResource.createLink(LinkDto(originalUrl = "http://onet.pl")).statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(linkResource.createLink(LinkDto(redirectHash = "asd")).statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun createLink() {
        val redirectHash = "uooo"
        val originalUrl = "http://onet.pl"
        val response = linkResource.createLink(LinkDto(originalUrl = originalUrl, redirectHash = redirectHash))
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        val linkFromDb = linkRepository.findByRedirectHash(redirectHash)
        assertThat(linkFromDb).isNotNull()
        assertThat(originalUrl).isEqualTo(linkFromDb?.originalUrl)
        assertThat(redirectHash).isEqualTo(linkFromDb?.redirectHash)
    }

    @Test
    fun createTwoLinksWithSameHash() {
        val numberOfLinksCreatedBefore = linkRepository.count();
        val redirectHash = "hash34"
        val response = linkResource.createLink(LinkDto(originalUrl = "http://wp.pl", redirectHash = redirectHash))
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        val duplicatedResponse = linkResource.createLink(LinkDto(originalUrl = "http://wp.pl", redirectHash = redirectHash))
        assertThat(duplicatedResponse.statusCode).isEqualTo(HttpStatus.CONFLICT)
        assertThat(numberOfLinksCreatedBefore + 1).isEqualTo(linkRepository.count())

    }

}
