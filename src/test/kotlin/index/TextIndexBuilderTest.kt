package index

import org.junit.jupiter.api.*
import kotlin.test.*

import ru.emkn.textindex.index.*

class TextIndexBuilderTest {
    private val wordSet1: List<String> = listOf("апельсинов", "апельсина")
    private val wordSet2: List<String> = listOf("яблок", "яблоко", "яблоком")

    private val text = listOf("она", "она", "купила", "яблок", "и", "апельсинов", "для", "друга", "апельсина",)

    private val wordFormsDictionary: TrieMap<WordId>

    init {
        val builder = WordFormsDictionaryBuilder()
        builder.addWordForms(wordSet1)
        builder.addWordForms(wordSet2)

        wordFormsDictionary = builder.build()
    }

    private fun buildIndex(wordToPosition: List<Pair<String, WordPosition>>): TextIndex {
        val builder = TextIndexBuilder(wordFormsDictionary)

        for ((wordStr, position) in wordToPosition)
            builder.processWord(wordStr, position)

        return builder.build()
    }

    @Test
    fun `test all words are indexed even not presented in word forms dictionary ones`() {
        val index = buildIndex(
            text.map { wordStr -> Pair(wordStr, WordPosition(0, 0, 0)) }
        )

        for (wordStr in text)
            assertNotNull(index.dictionary[wordStr])
    }

    @Test
    fun `test forms of one word have the same id`() {
        val index = buildIndex(
            text.map { wordStr -> Pair(wordStr, WordPosition(0, 0, 0)) }
        )

        assertEquals(index.dictionary[wordSet1[0]], index.dictionary[wordSet1[1]])
    }

    @Test
    fun `test different words have different ids`() {
        val index = buildIndex(
            text.map { wordStr -> Pair(wordStr, WordPosition(0, 0, 0)) }
        )

        assertNotEquals(index.dictionary[wordSet1[0]], index.dictionary[wordSet2[0]])
    }

    @Test
    fun `test all word entries are listed`() {
        val index = buildIndex(
            text.mapIndexed { wordIndex, wordStr ->
                Pair(wordStr, WordPosition(pageIndex = 0, lineIndex = 0, wordIndex))
            }
        )

        val entries = index.wordIdToInfo.flatMap { (_, info) -> info.entries }
        text.forEachIndexed { wordIndex, wordStr ->
            val entry = entries.find { it.word.str == wordStr && it.position.wordIndex == wordIndex }
            assertNotNull(entry)
        }
    }

    @Test
    fun `test that the same but not presented in dictionary words share the same id`() {
        val index = buildIndex(
            text.mapIndexed { wordIndex, wordStr ->
                Pair(wordStr, WordPosition(pageIndex = 0, lineIndex = 0, wordIndex))
            }
        )
        val entries = index.wordIdToInfo.flatMap { (_, info) -> info.entries }
        val entry0 = entries.find { it.word.str == text[0] && it.position.wordIndex == 0 }
        val entry1 = entries.find { it.word.str == text[1] && it.position.wordIndex == 1 }

        assertEquals(entry0?.word?.id, entry1?.word?.id)
    }
}