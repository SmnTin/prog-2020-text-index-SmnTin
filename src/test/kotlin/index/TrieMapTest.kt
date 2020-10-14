package index

import org.junit.jupiter.api.*
import kotlin.test.*

import ru.emkn.textindex.index.*

class TrieMapTest {
    private val words = listOf(
        "absolute", "absent", "abusive", "bachelor", "humble",
        "gangbang", "visual", "jojo", "traverse", ""
    )

    @Test
    fun `test adding unique words`() {
        val trie: TrieMap<Int> = emptyTrieMap()

        words.forEachIndexed { index, word ->
            trie.put(word, index)
            assertEquals(index + 1, trie.size)
        }
    }

    @Test
    fun `test access`() {
        val trie: TrieMap<Int> = emptyTrieMap()

        val values = words.indices

        var testPairs = words zip values
        testPairs = testPairs.shuffled()

        for ((word, value) in testPairs)
            trie.put(word, value)

        testPairs = testPairs.shuffled()

        for ((word, value) in testPairs)
            assertEquals(value, trie[word])
    }

    @Test
    fun `test adding duplicate`() {
        val trie: TrieMap<Int> = emptyTrieMap()

        words.forEachIndexed { index, word ->
            trie.put(word, index)
        }

        trie.put(words[1], words.size)

        // Adding duplicate does not affect the size
        assertEquals(trie.size, words.size)

        // The value is supposed to be overwritten
        assertEquals(trie[words[1]], words.size)
    }

    @Test
    fun `test access after reassemble`() {
        val trie: TrieMap<Int> = emptyTrieMap()

        val values = words.indices

        var testPairs = words zip values
        testPairs = testPairs.shuffled()

        for ((word, value) in testPairs)
            trie.put(word, value)

        val decomposition = decomposeTrie(trie)
        val trie2 = constructTrie<Int>(decomposition)

        testPairs = testPairs.shuffled()

        for ((word, value) in testPairs)
            assertEquals(value, trie2[word])
    }
}