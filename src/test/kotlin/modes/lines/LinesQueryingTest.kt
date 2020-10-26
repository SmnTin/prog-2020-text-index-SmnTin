package modes.lines

import org.junit.jupiter.api.*
import kotlin.test.*

import ru.emkn.textindex.modes.lines.*
import ru.emkn.textindex.index.*

class LinesQueryingTest {
    @Test
    fun `test lines search`() {
        val builder = TextIndexBuilder(
            WordFormsDictionary(emptyTrieMap(), emptyMap())
        )

        builder.processWord(
            "a",
            WordPosition(
                pageIndex = 0,
                lineIndex = 1,
                wordIndex = 0
            )
        )

        builder.processWord(
            "b",
            WordPosition(
                pageIndex = 1,
                lineIndex = 0,
                wordIndex = 0
            )
        )

        val index = builder.build()

        assertEquals(
            listOf(localToGlobalLineIndex(pageIndex = 0, localLineIndex = 1)),
            getIndicesOfLinesWithWord(index, "a")
        )
        assertEquals(
            listOf(localToGlobalLineIndex(pageIndex = 1, localLineIndex = 0)),
            getIndicesOfLinesWithWord(index, "b")
        )
    }

    @Test
    fun `test lines retrieving`() {
        val linesSequence = sequenceOf("a", "b", "c", "d", "e")
        val indices = listOf(3, 1)

        assertEquals(listOf("b", "d"), getLinesWithIndices(linesSequence, indices))
    }
}