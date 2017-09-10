package pl.jpetryk.kotlinshortener.service

import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
class HashGeneratorTest {

    @Autowired
    private lateinit var hashGenerator: HashGenerator;

    @Test
    fun generateHash() {
        val hash = hashGenerator.generateHash()
        Assertions.assertThat(hash.length).isEqualTo(HashGenerator.HASH_LENGTH)
    }

}