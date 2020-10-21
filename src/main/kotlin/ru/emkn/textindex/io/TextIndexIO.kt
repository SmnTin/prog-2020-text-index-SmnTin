package ru.emkn.textindex.io

import ru.emkn.textindex.index.*

import java.io.File

import ru.emkn.textindex.io.DictionaryStructureProtos.DictionaryStructure as DictionaryStructureMsg
import ru.emkn.textindex.io.WordEntryProtos.WordEntry as WordEntryMsg
import ru.emkn.textindex.io.WordEntryProtos.WordPosition as WordPositionMsg
import ru.emkn.textindex.io.WordEntryProtos.Word as WordMsg
import ru.emkn.textindex.io.TextIndexProtos.TextIndex as TextIndexMsg

internal fun serializeForwardEdgeUnit(unit: TrieMapStructureInspector.Unit.ForwardEdge) =
    DictionaryStructureMsg.Unit.newBuilder()
        .setType(DictionaryStructureMsg.Unit.Type.FORWARD_EDGE)
        .setLetter(unit.letter.toString())
        .build()

internal fun serializeBackwardEdgeUnit(unit: TrieMapStructureInspector.Unit.BackwardEdge) =
    DictionaryStructureMsg.Unit.newBuilder()
        .setType(DictionaryStructureMsg.Unit.Type.BACKWARD_EDGE)
        .setLetter(unit.letter.toString())
        .build()

internal fun serializeElemHolderUnit(unit: TrieMapStructureInspector.Unit.ElemHolder<*>) =
    DictionaryStructureMsg.Unit.newBuilder()
        .setType(DictionaryStructureMsg.Unit.Type.ELEM_HOLDER)
        .setElem(if (unit.elem is WordId) unit.elem else 0)
        .build()

internal fun serializeUnit(unit: TrieMapStructureInspector.Unit) =
    when (unit) {
        is TrieMapStructureInspector.Unit.ForwardEdge -> {
            serializeForwardEdgeUnit(unit)
        }
        is TrieMapStructureInspector.Unit.BackwardEdge -> {
            serializeBackwardEdgeUnit(unit)
        }
        is TrieMapStructureInspector.Unit.ElemHolder<*> -> {
            serializeElemHolderUnit(unit)
        }
    }

internal fun serializeDictionary(dictionary: TrieMap<WordId>) =
    DictionaryStructureMsg.newBuilder()
        .addAllUnits(
            decomposeTrie(dictionary)
                .map { unit ->
                    serializeUnit(unit)
                }
        )
        .build()

internal fun serializeWordPosition(position: WordPosition) =
    WordPositionMsg.newBuilder()
        .setPageIndex(position.pageIndex)
        .setLineIndex(position.lineIndex)
        .setWordIndex(position.wordIndex)
        .build()

internal fun serializeWord(word: Word) =
    WordMsg.newBuilder()
        .setId(word.id)
        .setStr(word.str)
        .build()

internal fun serializeWordEntry(entry: WordEntry) =
    WordEntryMsg.newBuilder()
        .setPosition(serializeWordPosition(entry.position))
        .setWord(serializeWord(entry.word))
        .build()

internal fun serializeTextIndex(index: TextIndex) =
    TextIndexMsg.newBuilder()
        .setDictionary(serializeDictionary(index.dictionary))
        .addAllEntries(
            index.wordIdToInfo
                .flatMap { (_, info) -> info.entries }
                .map { entry -> serializeWordEntry(entry) }
        )
        .build()

internal fun deserializeWordPosition(positionMsg: WordPositionMsg) =
    WordPosition(
        positionMsg.pageIndex,
        positionMsg.lineIndex,
        positionMsg.wordIndex
    )

internal fun deserializeWord(wordMsg: WordMsg): Word =
    Word(
        wordMsg.id,
        wordMsg.str
    )

internal fun deserializeWordEntry(entryMsg: WordEntryMsg): WordEntry =
    WordEntry(
        word = deserializeWord(entryMsg.word),
        position = deserializeWordPosition(entryMsg.position)
    )

internal fun deserializeForwardEdgeUnit(unitMsg: DictionaryStructureMsg.Unit) =
    TrieMapStructureInspector.Unit.ForwardEdge(unitMsg.letter.first()) // There is no Char type in Protobuf

internal fun deserializeBackwardEdgeUnit(unitMsg: DictionaryStructureMsg.Unit) =
    TrieMapStructureInspector.Unit.BackwardEdge(unitMsg.letter.first()) // There is no Char type in Protobuf

internal fun deserializeElemHolderUnit(unitMsg: DictionaryStructureMsg.Unit) =
    TrieMapStructureInspector.Unit.ElemHolder(unitMsg.elem)

internal fun deserializeUnit(unitMsg: DictionaryStructureMsg.Unit) =
    when (unitMsg.type) {
        DictionaryStructureMsg.Unit.Type.FORWARD_EDGE -> {
            deserializeForwardEdgeUnit(unitMsg)
        }
        DictionaryStructureMsg.Unit.Type.BACKWARD_EDGE -> {
            deserializeBackwardEdgeUnit(unitMsg)
        }
        DictionaryStructureMsg.Unit.Type.ELEM_HOLDER -> {
            deserializeElemHolderUnit(unitMsg)
        }
        else -> {
            throw NoSuchElementException("")
        }
    }

internal fun deserializeDictionary(dictionaryMsg: DictionaryStructureMsg) =
    constructTrie<WordId>(
        dictionaryMsg.unitsList.map { unitMsg ->
            deserializeUnit(unitMsg)
        }
    )

internal fun deserializeTextIndex(indexMsg: TextIndexMsg) =
    TextIndex(
        dictionary = deserializeDictionary(indexMsg.dictionary),
        wordIdToInfo = indexMsg.entriesList
            .map { entryMsg -> deserializeWordEntry(entryMsg) }
            .groupBy { entry -> entry.word.id }
            .mapValues { (_, entries) -> WordInfo(entries) }
    )

fun writeTextIndexToFile(index: TextIndex, file: File) {
    serializeTextIndex(index)
        .writeTo(file.outputStream())
}

fun readTextIndexFromFile(file: File) =
    deserializeTextIndex(
        TextIndexMsg.parseFrom(file.inputStream())
    )