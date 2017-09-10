package pl.jpetryk.kotlinshortener.resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.jpetryk.kotlinshortener.domain.Link
import pl.jpetryk.kotlinshortener.repo.LinkRepository


@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
@AutoConfigureMockMvc
class RedirectResourceTest {

    @Autowired
    private lateinit var linkRepository: LinkRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun linkWithWrongHash() {
        mockMvc.perform(get("/sdfghjkl")).andExpect(status().isNotFound)
    }

    @Test
    fun testRedirect() {
        val originalUrl = "http://onet.pl"
        val redirectHash = "redirectHash"
        linkRepository.save(Link(originalUrl = originalUrl, redirectHash = redirectHash))
        mockMvc.perform(get("/$redirectHash"))
                .andExpect(status().isFound)
                .andExpect(header().string("Location", originalUrl))
    }

    @Test
    fun testIndex() {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().is2xxSuccessful)

    }
}