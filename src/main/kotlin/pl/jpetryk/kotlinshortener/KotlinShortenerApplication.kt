package pl.jpetryk.kotlinshortener

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinShortenerApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinShortenerApplication::class.java, *args)
}
