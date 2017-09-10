package pl.jpetryk.kotlinshortener

import org.junit.Test
import kotlin.test.assertNotNull


class JsKotlinTest {

    @Test
    fun testMain() {
        assertNotNull(JsKotlin().main(emptyArray()))
    }
}