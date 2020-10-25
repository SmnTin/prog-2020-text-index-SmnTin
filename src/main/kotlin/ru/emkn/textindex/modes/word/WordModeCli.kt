package ru.emkn.textindex.modes.word

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import ru.emkn.textindex.io.cleanUp
import ru.emkn.textindex.io.readTextIndexFromFile
import java.io.File

class WordModeCli : CliktCommand(
    name = "word",
    help = "Analyze usage of specified word"
) {
    private val indexFilename by option(
        "-i", "--index",
        help = "Text index file"
    ).required()

    private val word by option(
        "-w", "--word",
        help = "Word to query"
    ).required()

    override fun run() {
        val index = readTextIndexFromFile(File(indexFilename))
        val analysis = queryWord(index, word.cleanUp())
        printAnalysis(analysis)
    }

    private fun printAnalysis(analysis: WordAnalysis) {
        printNumOfEntries(analysis.numOfEntries)
        printUsedWordForm(analysis.usedWordForms)
        printPages(analysis.pages)
    }

    private fun printNumOfEntries(numOfEntries: Int) {
        val pluralEnding = if (numOfEntries != 1) "s" else ""
        println("Used $numOfEntries time$pluralEnding.")
        println()
    }

    private fun printUsedWordForm(usedWordForms: Set<String>) {
        println("Used with the following word forms:")
        for (wordForm in usedWordForms)
            print("$wordForm ")
        println()
        println()
    }

    private fun printPages(pages: Set<Int>) {
        println("Found on pages (1-indexed):")
        for (page in pages)
            print("${page + 1} ")
        println()
        println()
    }
}