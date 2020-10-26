package ru.emkn.textindex.modes.word

import ru.emkn.textindex.index.*

data class WordAnalysis(
    val numOfEntries: Int,
    val usedWordForms: Set<String>,
    val pages: Set<Int>
)

/**
 * Looks for the usages of a [word] in the [index]
 * The analyzed properties are the number of
 * entries, used word forms and indices of pages
 * containing one of the word forms.
 */
fun queryWord(index: TextIndex, word: String): WordAnalysis {
    val id = index.dictionary[word]

    return if (id in index.wordIdToInfo)
        queryPresentedWord(index, id)
    else
        emptyWordAnalysis()
}

internal fun queryPresentedWord(index: TextIndex, id: WordId?): WordAnalysis {
    val info = index.wordIdToInfo[id]!!
    return WordAnalysis(
        numOfEntries = info.frequency,
        usedWordForms = info.entries.map { it.word.str }.toSet(),
        pages = info.entries.map { it.position.pageIndex }.toSortedSet()
    )
}

internal fun emptyWordAnalysis() =
    WordAnalysis(
        numOfEntries = 0,
        usedWordForms = emptySet(),
        pages = emptySet()
    )
