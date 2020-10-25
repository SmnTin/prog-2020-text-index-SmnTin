package ru.emkn.textindex.index

/**
 * Besides what is described in
 * the actual code, one important
 * implementation detail to notice:
 * word forms of one word just share
 * the same id in the dictionary.
 */
data class TextIndex(
    val dictionary: TrieMap<WordId>,
    val wordIdToInfo: Map<WordId, WordInfo>
)