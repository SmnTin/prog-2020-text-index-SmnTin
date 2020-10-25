package ru.emkn.textindex.index

data class WordFormsDictionary(
    val dict: TrieMap<WordId>,
    val wordIdToForms: Map<WordId, List<String>>
)