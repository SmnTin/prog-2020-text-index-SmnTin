package ru.emkn.textindex.index

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