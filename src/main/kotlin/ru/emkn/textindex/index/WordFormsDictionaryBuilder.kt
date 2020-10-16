package ru.emkn.textindex.index

class WordFormsDictionaryBuilder {
    private val dictionary: TrieMap<WordId> = emptyTrieMap()
    private var maxId: WordId = 0

    fun addWordForms(forms: List<String>) {
        val id = maxId++
        for (form in forms)
            dictionary.put(form, id)
    }

    fun build() = dictionary
}