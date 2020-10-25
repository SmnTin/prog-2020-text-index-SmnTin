package ru.emkn.textindex.modes.word

import ru.emkn.textindex.index.*

data class WordAnalysis(
    val numOfEntries: Int,
    val usedWordForms: Set<String>,
    val pages: Set<Int>
)

fun queryWord(index: TextIndex, word: String) : WordAnalysis {
    val id = index.dictionary[word]

    return if (id in index.wordIdToInfo) {
        val info = index.wordIdToInfo[id]!!
        WordAnalysis(
            numOfEntries = info.frequency,
            usedWordForms = info.entries.map { it.word.str }.toSet(),
            pages = info.entries.map { it.position.pageIndex }.toSortedSet()
        )
    } else {
        WordAnalysis(
            numOfEntries = 0,
            usedWordForms = emptySet(),
            pages = emptySet()
        )
    }
}
