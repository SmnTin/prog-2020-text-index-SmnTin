package ru.emkn.textindex.index

/**
 * Allows to build the text index word by word
 * without any need in reading the whole file in
 * advance.
 *
 * The resulting index is fully independent from
 * the word forms dictionary which allows to read it
 * and perform queries on it without loading the heavy
 * word forms dictionary every time.
 *
 * Furthermore, the resulted dictionary is complete
 * in terms that it allows to perform queries using
 * word forms that weren't presented in the text.
 * It is done in the last stage of building the index
 * which is complementing the dictionary with
 * all the word forms of used words regardless if
 * they were or weren't presented in the text.
 */
class TextIndexBuilder(
    private val wordFormsDictionary: WordFormsDictionary
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
        val formId = wordFormsDictionary.dict[wordStr]
        val mappingId = wordIdMapping[formId]
        val dictId = dictionary[wordStr]

        if (formId != null && mappingId != null) {
            putWord(Word(id = mappingId, str = wordStr), position)
        } else if (formId != null) {
            val id = maxId++
            wordIdMapping[formId] = id
            putWord(Word(id, str = wordStr), position)
        } else {
            putWord(Word(id = dictId ?: maxId++, str = wordStr), position)
        }
    }

    private fun addAllWordFormsOfUsedWords() {
        wordIdMapping.forEach { (formId, dictId) ->
            wordFormsDictionary.wordIdToForms[formId]?.forEach { wordForm ->
                dictionary.put(wordForm, dictId)
            }
        }
    }

    fun build() : TextIndex {
        addAllWordFormsOfUsedWords()

        return TextIndex(
            dictionary = dictionary,
            wordIdToInfo =
            wordIdToEntries.mapValues { (_, entries) ->
                WordInfo(entries)
            }
        )
    }
}