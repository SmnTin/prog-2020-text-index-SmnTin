package index

import org.junit.jupiter.api.*
import kotlin.test.*

import ru.emkn.textindex.index.*

class WordFormsDictionaryBuilderTest {
    private val wordSet1: List<String> = listOf("апельсин", "апельсинов")
    private val wordSet2: List<String> = listOf("яблоко", "яблок")

    @Test
    fun `test different words have different ids`() {
        val builder = WordFormsDictionaryBuilder()
        builder.addWordForms(wordSet1)
        builder.addWordForms(wordSet2)

        val dict = builder.build().dict

        for (word1 in wordSet1)
            for (word2 in wordSet2)
                assertNotEquals(dict[word1], dict[word2])
    }

    @Test
    fun `test word forms have the same id`() {
        val builder = WordFormsDictionaryBuilder()
        builder.addWordForms(wordSet1)
        builder.addWordForms(wordSet2)

        val dict = builder.build().dict

        for (word1_1 in wordSet1)
            for (word1_2 in wordSet1)
                assertEquals(dict[word1_1], dict[word1_2])

        for (word2_1 in wordSet2)
            for (word2_2 in wordSet2)
                assertEquals(dict[word2_1], dict[word2_2])
    }
}