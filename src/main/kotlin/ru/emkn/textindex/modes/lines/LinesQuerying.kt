package ru.emkn.textindex.modes.lines

import ru.emkn.textindex.index.*
import java.io.File

fun getIndicesOfLinesWithWord(index: TextIndex, wordStr: String): List<Int> {
    val id = index.dictionary[wordStr]

    return index.wordIdToInfo[id]?.entries?.map { entry ->
        localToGlobalLineIndex(
            entry.position.pageIndex,
            entry.position.lineIndex
        )
    } ?: emptyList()
}

fun getLines(textFile: File, linesIndices: List<Int>): List<String> {
    if (linesIndices.isEmpty())
        return emptyList()

    val result = mutableListOf<String>()

    // Using two pointers method to get all lines from file in one traverse
    val indicesIterator = linesIndices.sorted().iterator()
    var nextIndex = indicesIterator.next()

    textFile.bufferedReader()
        .lineSequence()
        .forEachIndexed { index, line ->
            if (nextIndex == index) {
                result.add(line)
                if (indicesIterator.hasNext())
                    nextIndex = indicesIterator.next()
            }
        }

    return result
}