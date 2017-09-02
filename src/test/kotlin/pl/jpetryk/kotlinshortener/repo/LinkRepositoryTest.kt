package pl.jpetryk.kotlinshortener.repo

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pl.jpetryk.kotlinshortener.domain.Link

import org.assertj.core.api.Assertions.*
import org.springframework.dao.DataIntegrityViolationException


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LinkRepositoryTest {

    @Autowired
    lateinit var linkRepository: LinkRepository;

    @Test
    fun testSave() {
        linkRepository.save(Link(originalUrl = "http://onet.pl", redirectHash = "asd"))
        assertThat(linkRepository.count()).isEqualTo(1)
    }

    @Test(expected = DataIntegrityViolationException::class)
    fun saveWithSameRedirectHash() {
        linkRepository.save(Link(originalUrl = "http://wp.pl", redirectHash = "123"))
        linkRepository.save(Link(originalUrl = "http://wp.pl", redirectHash = "123"))
    }

    @Test
    fun testIfHashExists() {
        val redirectHash = "uiop"
        linkRepository.save(Link(originalUrl = "", redirectHash = redirectHash))
        assertThat(linkRepository.existsByRedirectHash(redirectHash)).isTrue()
    }
}
