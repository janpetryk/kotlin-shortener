package pl.jpetryk.kotlinshortener.model

data class LinkDto(val originalUrl: String = "", val redirectHash: String = "")
