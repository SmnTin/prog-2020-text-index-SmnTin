package ru.emkn.textindex.index

class TrieMapImpl<Elem> : TrieMap<Elem> {
    override val size: Int
        get() = _size

    internal var _size: Int = 0

    internal class Node<Elem> {
        val edges: MutableMap<Char, Node<Elem>> = hashMapOf()
        var elem: Elem? = null
    }

    internal var root: Node<Elem> = Node()

    override fun clear() {
        _size = 0
        root = Node()
    }

    override fun containsKey(key: String): Boolean =
        getNodeByKey(key) != null

    override fun isEmpty(): Boolean =
        size == 0

    override fun get(key: String): Elem? =
        getNodeByKey(key)?.elem

    override fun put(key: String, value: Elem): Elem? {
        var cur: Node<Elem> = root
        for (c in key) {
            if (c !in cur.edges)
                cur.edges[c] = Node()
            cur = cur.edges[c]!!
        }

        val prev = cur.elem
        if (prev == null)
            ++_size

        cur.elem = value
        return prev
    }

    private fun getNodeByKey(key: String): Node<Elem>? {
        var cur: Node<Elem> = root
        for (c in key) {
            if (c !in cur.edges)
                return null
            cur = cur.edges[c]!!
        }
        return cur
    }
}

fun <Elem> emptyTrieMap(): TrieMap<Elem> =
    TrieMapImpl()

class TrieMapStructureInspectorImpl<Elem> : TrieMapStructureInspector<Elem> {
    private fun traverseDecompose(
        node: TrieMapImpl.Node<Elem>,
        decomposition: MutableList<TrieMapStructureInspector.Unit>
    ) {
        if (node.elem != null)
            decomposition.add(TrieMapStructureInspector.Unit.ElemHolder<Elem>(node.elem!!))
        for ((letter, next) in node.edges) {
            decomposition.add(TrieMapStructureInspector.Unit.ForwardEdge(letter))
            traverseDecompose(next, decomposition)
            decomposition.add(TrieMapStructureInspector.Unit.BackwardEdge(letter))
        }
    }

    private fun <Elem> traverseConstruct(
        trie: TrieMapImpl<Elem>,
        node: TrieMapImpl.Node<Elem>,
        decompositionIterator: Iterator<TrieMapStructureInspector.Unit>
    ) {
        if (decompositionIterator.hasNext()) {
            var unit = decompositionIterator.next()
            if (unit is TrieMapStructureInspector.Unit.ElemHolder<*>) {
                trie._size++
                node.elem = unit.elem as Elem?
                unit = decompositionIterator.next()
            }
            while (unit !is TrieMapStructureInspector.Unit.BackwardEdge) {
                if (unit is TrieMapStructureInspector.Unit.ForwardEdge) {
                    node.edges[unit.letter] = TrieMapImpl.Node()
                    traverseConstruct(
                        trie,
                        node.edges[unit.letter]!!,
                        decompositionIterator
                    )
                    unit = decompositionIterator.next()
                } else if (unit is TrieMapStructureInspector.Unit.ElemHolder<*>) {
                    throw IllegalArgumentException("Two objects can't be associated with the same node.")
                }
            }
        }
    }

    override fun decompose(trie: TrieMap<Elem>): List<TrieMapStructureInspector.Unit> {
        val concreteTrie = trie as TrieMapImpl<Elem>
        val decomposition: MutableList<TrieMapStructureInspector.Unit> = mutableListOf()

        traverseDecompose(concreteTrie.root, decomposition)

        decomposition.add(TrieMapStructureInspector.Unit.BackwardEdge('\r'))
        return decomposition
    }


    override fun construct(structure: List<TrieMapStructureInspector.Unit>): TrieMap<Elem> {
        val trie = TrieMapImpl<Elem>()
        traverseConstruct(trie, trie.root, structure.iterator())
        return trie
    }
}

fun <Elem> decomposeTrie(trie: TrieMap<Elem>): List<TrieMapStructureInspector.Unit> {
    val inspector = TrieMapStructureInspectorImpl<Elem>()
    return inspector.decompose(trie)
}

fun <Elem> constructTrie(decomposition: List<TrieMapStructureInspector.Unit>): TrieMap<Elem> {
    val inspector = TrieMapStructureInspectorImpl<Elem>()
    return inspector.construct(decomposition)
}

