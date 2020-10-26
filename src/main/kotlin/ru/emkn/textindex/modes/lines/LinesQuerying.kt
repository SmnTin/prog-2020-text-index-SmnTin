package ru.emkn.textindex.modes.lines

import ru.emkn.textindex.index.*
import java.io.File

/**
 * Finds all entries of the specified [wordStr]
 * and gets indices of the lines on which the word
 * or one of its word forms is mentioned.
 *
 * @return indices
 */
fun getIndicesOfLinesWithWord(index: TextIndex, wordStr: String): List<Int> {
    val id = index.dictionary[wordStr]

    return index.wordIdToInfo[id]?.entries?.map { entry ->
        localToGlobalLineIndex(
            entry.position.pageIndex,
            entry.position.lineIndex
        )
    } ?: emptyList()
}

/**
 * Gets lines with the specified [linesIndices]
 * from the [lines] sequence
 *
 * @return lines in ascending indices order
 */
fun getLinesWithIndices(lines: Sequence<String>, linesIndices: List<Int>): List<String> {
    if (linesIndices.isEmpty())
        return emptyList()

    val result = mutableListOf<String>()

    // Using two pointers method to get all lines from file in one traverse
    val indicesIterator = linesIndices.sorted().iterator()
    var nextIndex = indicesIterator.next()

    lines.forEachIndexed { index, line ->
        if (nextIndex == index) {
            result.add(line)
            if (indicesIterator.hasNext())
                nextIndex = indicesIterator.next()
        }
    }

    return result
}