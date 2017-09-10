package pl.jpetryk.kotlinshortener.resource

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import pl.jpetryk.kotlinshortener.domain.Link
import pl.jpetryk.kotlinshortener.model.LinkDto
import pl.jpetryk.kotlinshortener.repo.LinkRepository

@Controller
class LinkResource(val linkRepository: LinkRepository) {

    @PostMapping(value = "/links", consumes = arrayOf("application/json"))
    fun createLink(@RequestBody createLinkRequest: LinkDto): ResponseEntity<Any> {
        if (createLinkRequest.originalUrl.isNullOrEmpty() || createLinkRequest.redirectHash.isNullOrEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        if (linkRepository.existsByRedirectHash(createLinkRequest.redirectHash)) {
            return ResponseEntity(HttpStatus.CONFLICT);
        } else {
            val savedLink = linkRepository.save(Link(originalUrl = createLinkRequest.originalUrl, redirectHash = createLinkRequest.redirectHash))
            return ResponseEntity(LinkDto(savedLink.originalUrl, savedLink.redirectHash), HttpStatus.CREATED)
        }
    }

}
