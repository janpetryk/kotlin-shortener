package pl.jpetryk.kotlinshortener.service

import org.apache.commons.text.CharacterPredicate
import org.apache.commons.text.CharacterPredicates
import org.apache.commons.text.RandomStringGenerator
import org.springframework.stereotype.Service
import pl.jpetryk.kotlinshortener.repo.LinkRepository


@Service
class HashGenerator(private val linkRepository: LinkRepository) {

    companion object {
        const val HASH_LENGTH: Int = 6
    }

    private val stringGenerator = RandomStringGenerator.Builder()
            .withinRange('0'.toInt(), 'z'.toInt())
            .filteredBy(CharacterPredicate { codePoint -> CharacterPredicates.LETTERS.test(codePoint) || CharacterPredicates.DIGITS.test(codePoint) })
            .build()

    fun generateHash(): String {
        var hash = stringGenerator.generate(HASH_LENGTH)
        while (linkRepository.existsByRedirectHash(hash)) {
            hash = stringGenerator.generate(HASH_LENGTH)
        }
        return hash
    }

}

