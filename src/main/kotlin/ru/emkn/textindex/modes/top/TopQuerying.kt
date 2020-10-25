package ru.emkn.textindex.modes.top

import ru.emkn.textindex.index.*

import java.util.*

data class TopWord(val word: Word, val numOfEntries: Int, val usageRatio: Float)

/**
 * Gets the top N most frequently used words
 * including all its word forms.
 *
 * Uses binary heap to perform query using
 * O(n * log(k)) time where n is the number
 * of words in the [index] and k is the top size.
 *
 * Also tries to minimize iterations over the
 * whole index.
 */
fun findTopNWords(index: TextIndex, num: Int, minLen: Int): List<TopWord> {
    val queue = PriorityQueue(compareBy(TopWord::numOfEntries))

    var totalNumOfWords = 0

    index.wordIdToInfo.forEach { (_, info) ->
        // It is too long to perform this in the separate loop as we iterate over the whole index
        totalNumOfWords += getNumOfEntries(info, minLen)

        queue.add(
            TopWord(
                word = info.entries.first().word,
                numOfEntries = getNumOfEntries(info, minLen),
                usageRatio = 0.0f // Will be added later
            )
        )
        if (queue.size > num)
            queue.poll() // Always removes the queue min thus only N max remain
    }

    return queue
        .toList()
        .map { topWord ->
            TopWord(
                topWord.word,
                topWord.numOfEntries,
                usageRatio = 1.0f * topWord.numOfEntries / totalNumOfWords // Complementing the lack parameter
            )
        }
        .sortedByDescending(TopWord::numOfEntries) // Converting to a list does not guarantee that sorted order persists
}

// Optimization not to filter the word if min length equals to 1
fun getNumOfEntries(info: WordInfo, minLen: Int) =
    if (minLen == 1) info.entries.size else info.entries.filter { it.word.str.length >= minLen }.size