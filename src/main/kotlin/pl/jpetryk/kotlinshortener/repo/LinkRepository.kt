package pl.jpetryk.kotlinshortener.repo

import org.springframework.data.repository.CrudRepository
import pl.jpetryk.kotlinshortener.domain.Link

interface LinkRepository : CrudRepository<Link, Long> {

    fun existsByRedirectHash(redirectHash: String): Boolean

}
