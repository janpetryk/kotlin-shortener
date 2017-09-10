package pl.jpetryk.kotlinshortener.resource

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import pl.jpetryk.kotlinshortener.domain.Link
import pl.jpetryk.kotlinshortener.model.LinkDto
import pl.jpetryk.kotlinshortener.repo.LinkRepository
import pl.jpetryk.kotlinshortener.service.HashGenerator

@Controller
class LinkResource(val linkRepository: LinkRepository, val hashGenerator: HashGenerator) {

    @PostMapping(value = "/links", consumes = arrayOf("application/json"))
    fun createLink(@RequestBody createLinkRequest: LinkDto): ResponseEntity<Any> {
        if (createLinkRequest.originalUrl.isNullOrEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        if (createLinkRequest.redirectHash.isNotEmpty()) {
            if (linkRepository.existsByRedirectHash(createLinkRequest.redirectHash)) {
                return ResponseEntity(HttpStatus.CONFLICT)
            } else {
                return responseEntity(createLinkRequest.originalUrl, createLinkRequest.redirectHash)
            }
        } else {
            return responseEntity(createLinkRequest.originalUrl, hashGenerator.generateHash())
        }
    }

    private fun responseEntity(originalUrl: String, redirectHash: String): ResponseEntity<Any> {
        val savedLink = linkRepository.save(Link(originalUrl = originalUrl, redirectHash = redirectHash))
        return ResponseEntity(LinkDto(savedLink.originalUrl, savedLink.redirectHash), HttpStatus.CREATED)
    }

}
