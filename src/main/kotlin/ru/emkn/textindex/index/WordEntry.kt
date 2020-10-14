package ru.emkn.textindex.index

typealias WordId = Int

data class Word(val id: WordId, val str: String)

data class WordPosition(val pageIndex: Int, val lineIndex: Int, val wordIndex: Int)

data class WordEntry(val word: Word, val position: WordPosition)

data class WordInfo(val entries: List<WordInfo>) {
    val frequency: Int
        get() = entries.size
}

const val linesPerPage: Int = 45

fun globalToLocalLineIndex(globalLineIndex: Int) =
    globalLineIndex % linesPerPage

fun globalLineIndexToPageIndex(globalLineIndex: Int) =
    globalLineIndex / linesPerPage

fun localToGlobalLineIndex(pageIndex: Int, localLineIndex: Int) =
    pageIndex * linesPerPage + localLineIndex