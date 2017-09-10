package pl.jpetryk.kotlinshortener.resource

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.jpetryk.kotlinshortener.model.LinkDto
import pl.jpetryk.kotlinshortener.repo.LinkRepository


@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
@AutoConfigureMockMvc
class LinkResourceTest {

    @Autowired
    private lateinit var linkRepository: LinkRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun sendBadRequest() {
        performPost("", "").andExpect(status().isBadRequest)
        performPost("", "asd").andExpect(status().isBadRequest)
    }

    @Test
    fun createLinkWithoutHash() {
        val originalUrl = "http://onet.pl"
        val result = performPost(originalUrl, "").andExpect(status().isCreated).andReturn()
        val link = objectMapper.readValue(result.response.contentAsString, LinkDto::class.java);
        val linkFromDb = linkRepository.findByRedirectHash(link.redirectHash)
        assertThat(linkFromDb).isNotNull()
        assertThat(originalUrl).isEqualTo(linkFromDb?.originalUrl)
    }

    @Test
    fun createLinkWithSpecifiedHash() {
        val redirectHash = "uooo"
        val originalUrl = "http://onet.pl"
        performPost(originalUrl, redirectHash).andExpect(status().isCreated)
        val linkFromDb = linkRepository.findByRedirectHash(redirectHash)
        assertThat(linkFromDb).isNotNull()
        assertThat(originalUrl).isEqualTo(linkFromDb?.originalUrl)
    }

    @Test
    fun createTwoLinksWithSameHash() {
        val numberOfLinksCreatedBefore = linkRepository.count();
        val redirectHash = "hash34"
        val originalUrl = "http://wp.pl"
        performPost(originalUrl, redirectHash).andExpect(status().isCreated)
        performPost(originalUrl, redirectHash).andExpect(status().isConflict)
        assertThat(numberOfLinksCreatedBefore + 1).isEqualTo(linkRepository.count())

    }

    private fun performPost(originalUrl: String, redirectHash: String): ResultActions {
        return mockMvc.perform(post("/links")
                .content(objectMapper.writeValueAsString(LinkDto(originalUrl = originalUrl, redirectHash = redirectHash)))
                .contentType("application/json"))
        println()
    }

}
