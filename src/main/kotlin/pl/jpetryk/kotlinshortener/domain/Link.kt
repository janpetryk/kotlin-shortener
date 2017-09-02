package pl.jpetryk.kotlinshortener.domain

import javax.persistence.*

@Entity
class Link(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,
        var originalUrl: String = "",
        @Column(unique = true)
        var redirectHash: String = ""
)
