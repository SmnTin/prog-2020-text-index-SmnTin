package modes.top

import org.junit.jupiter.api.*
import kotlin.test.*

import ru.emkn.textindex.index.*
import ru.emkn.textindex.modes.top.TopWord
import ru.emkn.textindex.modes.top.findTopNWords

class TopQueryingTest {
    private fun buildIndexOutOfLines(lines: List<String>): TextIndex {
        val builder = TextIndexBuilder(
            WordFormsDictionary(emptyTrieMap(), emptyMap())
        )

        lines.forEachIndexed { lineIndex, line ->
            line.split(' ')
                .forEachIndexed { wordIndex, word ->
                    builder.processWord(
                        word,
                        WordPosition(
                            pageIndex = 0,
                            lineIndex,
                            wordIndex
                        )
                    )
                }
        }

        return builder.build()
    }

    private val lines = listOf("a b", "b b b", "c c")

    private fun getTestTop(): List<TopWord> {
        val index = buildIndexOutOfLines(lines)

        return findTopNWords(index, num = 2, minLen = 1)
    }

    private fun getNumOfEntries(wordQ: String) =
        lines.map { line ->
            line.split(' ')
                .filter { word ->
                    word == wordQ
                }
                .count()
        }.sum()

    @Test
    fun `test descending order`() {
        val top = getTestTop()

        assertEquals("b", top[0].word.str)
        assertEquals("c", top[1].word.str)
    }

    @Test
    fun `test num of entries`() {
        val top = getTestTop()

        val bNum = getNumOfEntries("b")
        val cNum = getNumOfEntries("c")

        assertEquals(bNum, top[0].numOfEntries)
        assertEquals(cNum, top[1].numOfEntries)
    }

    @Test
    fun `test usage ratio`() {
        val top = getTestTop()

        val totalNumOfWords = lines.map { it.split(' ').count() }.sum()

        val bNum = getNumOfEntries("b")
        val cNum = getNumOfEntries("c")

        assertEquals(1.0f * bNum / totalNumOfWords, top[0].usageRatio)
        assertEquals(1.0f * cNum / totalNumOfWords, top[1].usageRatio)
    }
}