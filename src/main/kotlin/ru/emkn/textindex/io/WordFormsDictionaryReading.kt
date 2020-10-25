package ru.emkn.textindex.io

import ru.emkn.textindex.index.*

import org.apache.commons.csv.*

const val wordFormsDictFileName = "odict.csv"

/**
 * Reads word forms dictionary using
 * [WordFormsDictionaryBuilder].
 *
 * The dictionary file is placed in resources
 * folder to be loaded into the final .jar file.
 *
 * It guarantees that the tool can be used with
 * the single executable without any additional
 * environment. Ave to portability!
 */
fun readWordFormsDictionary(): WordFormsDictionary {
    val builder = WordFormsDictionaryBuilder()

    val resourceStream = Thread.currentThread().contextClassLoader?.getResourceAsStream(wordFormsDictFileName)!!

    CSVParser(resourceStream.bufferedReader(), CSVFormat.DEFAULT.withSkipHeaderRecord())
        .forEach { row ->
            val wordForms = row
                .filterIndexed { index, _ -> index != 1 } // The second word is auxiliary mark so it is skipped
                .map { it.toLowerCase() }

            builder.addWordForms(wordForms)
        }

    return builder.build()
}