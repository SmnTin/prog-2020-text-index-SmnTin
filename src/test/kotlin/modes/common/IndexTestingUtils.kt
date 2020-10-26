package modes.common

import ru.emkn.textindex.index.*

fun buildIndexOutOfLines(lines: List<String>): TextIndex {
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

fun getNumOfEntries(lines: List<String>, wordQ: String) =
    lines.map { line ->
        line.split(' ')
            .filter { word ->
                word == wordQ
            }
            .count()
    }.sum()