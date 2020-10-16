package ru.emkn.textindex.index

class TextIndexBuilder(
    private val wordFormsDictionary: TrieMap<WordId>
) {
    private val wordIdMapping: MutableMap<WordId, WordId> = mutableMapOf()

    private var maxId: WordId = 0
    private val dictionary: TrieMap<WordId> = emptyTrieMap()
    private val wordIdToEntries: MutableMap<WordId, MutableList<WordEntry>> = mutableMapOf()

    private fun putWord(word: Word, position: WordPosition) {
        dictionary[word.str] = word.id
        if (word.id !in wordIdToEntries)
            wordIdToEntries[word.id] = mutableListOf()
        wordIdToEntries[word.id]?.add(WordEntry(word, position))
    }

    fun processWord(wordStr: String, position: WordPosition) {
        val formId = wordFormsDictionary[wordStr]
        val mappingId = wordIdMapping[formId]

        if (formId != null && mappingId != null) {
            putWord(Word(id = mappingId, str = wordStr), position)
        } else if (formId != null) {
            val id = maxId++
            wordIdMapping[formId] = id
            putWord(Word(id, str = wordStr), position)
        } else {
            putWord(Word(id = maxId++, str = wordStr), position)
        }
    }

    fun build() =
        TextIndex(
            dictionary = dictionary,
            wordIdToInfo =
            wordIdToEntries.mapValues { (_, entries) ->
                WordInfo(entries)
            }
        )
}