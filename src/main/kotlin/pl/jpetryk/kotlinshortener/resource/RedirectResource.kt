package pl.jpetryk.kotlinshortener.resource

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import pl.jpetryk.kotlinshortener.repo.LinkRepository

@Controller("/")
class RedirectResource(private val linkRepository: LinkRepository) {

    @GetMapping("/{redirectHash}")
    fun redirect(@PathVariable("redirectHash") redirectHash: String): ResponseEntity<Any> {
        val dbLink = linkRepository.findByRedirectHash(redirectHash)
        if (dbLink == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            val headers = HttpHeaders()
            headers.add("Location", dbLink.originalUrl)
            return ResponseEntity("", headers, HttpStatus.FOUND)
        }
    }
}
