package ru.emkn.textindex.modes.top

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*

import ru.emkn.textindex.io.readTextIndexFromFile

import java.io.File

class TopModeCli : CliktCommand(
    name = "top",
    help = "Find top N used words including all its word forms"
) {
    private val indexFilename by option(
        "-i", "--index",
        help = "Text index file"
    ).required()

    private val numOfWords by option(
        "-n", "--number", "-c", "--count",
        help = "The number of top words to find"
    ).int().default(5)

    private val minLen by option(
        "-m", "--min",
        help = "Minimum length of words. Useful to filter out prepositions and other auxiliary words"
    ).int().default(1)

    override fun run() {
        val index = readTextIndexFromFile(File(indexFilename))
        val topWords = findTopNWords(index, numOfWords, minLen)
        printTop(topWords)
    }

    private fun printTop(top: List<TopWord>) {
        println("Usage    Total    Word")
        for (topWord in top)
            printTopWord(topWord)
    }

    private fun printTopWord(topWord: TopWord) {
        println(String.format(
            "%05.02f%%   %-8d %s",
            topWord.usageRatio,
            topWord.numOfEntries,
            topWord.word.str
        ))
    }
}

