package pl.jpetryk.kotlinshortener.domain

import javax.persistence.*

@Entity
data class Link(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        @Column(nullable = false)
        val originalUrl: String = "",
        @Column(unique = true, nullable = false)
        val redirectHash: String = ""
)
