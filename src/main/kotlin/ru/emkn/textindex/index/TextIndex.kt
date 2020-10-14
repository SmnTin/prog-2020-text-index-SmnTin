package ru.emkn.textindex.index

data class TextIndex(
    val dictionary: TrieMap<WordId>,
    val wordIdToInfo: Map<WordId, WordInfo>
)