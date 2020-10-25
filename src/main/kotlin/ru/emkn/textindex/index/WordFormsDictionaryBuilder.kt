package ru.emkn.textindex.index

/**
 * Builds word forms dictionary.
 * The idea is simple: just associate
 * all the forms of one word with
 * the same id.
 *
 * It also stores all the word forms
 * in the easy to read format which
 * is lately used in text index building.
 *
 * @see TextIndexBuilder
 */
class WordFormsDictionaryBuilder {
    private val dictionary: TrieMap<WordId> = emptyTrieMap()
    private val wordIdToForms = mutableMapOf<WordId, List<String>>()
    private var maxId: WordId = 0

    fun addWordForms(forms: List<String>) {
        val id = maxId++
        for (form in forms)
            dictionary.put(form, id)
        wordIdToForms[id] = forms
    }

    fun build() = WordFormsDictionary(
        dictionary,
        wordIdToForms
    )
}