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

/**
 * This class was created to implement trie map
 * serialization that is not dependent to the
 * way it is stored.
 *
 * It actually encodes DFS traversal of the
 * tree.
 */
interface TrieMapStructureInspector<Elem> {
    sealed class Unit {
        class ElemHolder<Elem>(val elem: Elem) : Unit()
        class ForwardEdge(val letter: Char) : Unit()
        class BackwardEdge(val letter: Char) : Unit()
    }

    fun decompose(trie: TrieMap<Elem>): List<Unit>

    /**
     * @throws IllegalArgumentException if anything is wrong with the sequence
     */
    fun construct(structure: List<Unit>): TrieMap<Elem>
}