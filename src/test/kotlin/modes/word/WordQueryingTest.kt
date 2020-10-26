package modes.word

import org.junit.jupiter.api.*
import kotlin.test.*

import modes.common.*
import ru.emkn.textindex.modes.word.*

class WordQueryingTest {
    private val lines = listOf("a b", "b b b", "c c")

    private fun getTestAnalysis(): WordAnalysis {
        val index = buildIndexOutOfLines(lines)

        return queryWord(index, "b")
    }

    @Test
    fun `test num of entries`() {
        val analysis = getTestAnalysis()

        val numOfEntries = getNumOfEntries(lines, "b")

        assertEquals(numOfEntries, analysis.numOfEntries)
    }

    @Test
    fun `test used word forms`() {
        val analysis = getTestAnalysis()

        assertEquals(setOf("b"), analysis.usedWordForms)
    }

    @Test
    fun `test pages`() {
        val analysis = getTestAnalysis()

        assertEquals(setOf(0), analysis.pages)
    }

    @Test
    fun `test not presented word`() {
        val index = buildIndexOutOfLines(lines)

        val analysis = queryWord(index, "d")

        assertEquals(0, analysis.numOfEntries)
        assertEquals(emptySet(), analysis.pages)
        assertEquals(emptySet(), analysis.usedWordForms)
    }
}