package pl.jpetryk.kotlinshortener.domain

import javax.persistence.*

@Entity
class Link(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val originalUrl: String = "",
        @Column(unique = true)
        val redirectHash: String = ""
)
