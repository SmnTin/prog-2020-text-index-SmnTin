package ru.emkn.textindex.modes.build

import ru.emkn.textindex.index.*
import ru.emkn.textindex.io.*

/**
 * Parses input line sequence
 * and builds text index on it.
 *
 * The line sequence is used because
 * it can be lazily evaluated.
 * In other words, only one
 * line is loaded per minute.
 * (theoretically, not taking
 * into account the slowness of
 * garbage collector and input
 * stream buffering)
 */
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
                    lineIndex = globalToLocalLineIndex(lineIndex),
                    wordIndex
                )
            )
        }
    }
    return builder.build()
}