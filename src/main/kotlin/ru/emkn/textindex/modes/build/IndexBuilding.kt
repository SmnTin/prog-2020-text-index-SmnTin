package ru.emkn.textindex.modes.build

import ru.emkn.textindex.index.*
import ru.emkn.textindex.io.*

fun buildIndexForFile(lineSequence: Sequence<String>): TextIndex {
    val builder = TextIndexBuilder(readWordFormsDictionary())
    lineSequence.forEachIndexed { lineIndex, line ->
        val words = line
            .split(" ")
            .map(String::cleanUp)

        words.forEachIndexed { wordIndex, word ->
            builder.processWord(
                word,
                WordPosition(
                    pageIndex = globalLineIndexToPageIndex(lineIndex),
                    lineIndex,
                    wordIndex
                )
            )
        }
    }
    return builder.build()
}