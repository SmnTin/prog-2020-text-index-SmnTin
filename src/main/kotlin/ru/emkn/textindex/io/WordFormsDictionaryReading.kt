package ru.emkn.textindex.io

import ru.emkn.textindex.index.WordFormsDictionaryBuilder
import ru.emkn.textindex.index.TrieMap
import ru.emkn.textindex.index.WordId

import org.apache.commons.csv.*
import java.io.File

const val wordFormsDictFileName = "odict.csv"

fun readWordFormsDictionary(): TrieMap<WordId> {
    val builder = WordFormsDictionaryBuilder()

    val resourceStream = Thread.currentThread().contextClassLoader?.getResourceAsStream(wordFormsDictFileName)!!
//    val resourceStream = File(wordFormsDictFileName)

    CSVParser(resourceStream.bufferedReader(), CSVFormat.DEFAULT.withSkipHeaderRecord())
        .forEach { row ->
            val wordForms = row
                .filterIndexed { index, _ -> index != 1 }
                .map { it.toLowerCase() }

            builder.addWordForms(wordForms)
        }

    return builder.build()
}