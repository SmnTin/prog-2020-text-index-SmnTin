package ru.emkn.textindex.index

interface TrieMap<Elem> {
    val size: Int

    fun clear()
    fun isEmpty(): Boolean

    fun containsKey(key: String): Boolean
    operator fun get(key: String): Elem?

    fun put(key: String, value: Elem): Elem?
    operator fun set(str: String, value: Elem)
}

interface TrieMapStructureInspector<Elem> {
    sealed class Unit {
        class ElemHolder<Elem>(val elem: Elem) : Unit()
        class ForwardEdge(val letter: Char) : Unit()
        class BackwardEdge(val letter: Char) : Unit()
    }

    fun decompose(trie: TrieMap<Elem>): List<Unit>
    fun construct(structure: List<Unit>): TrieMap<Elem>
}