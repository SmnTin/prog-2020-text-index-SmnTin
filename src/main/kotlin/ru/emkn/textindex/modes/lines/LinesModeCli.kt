package ru.emkn.textindex.modes.lines

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import ru.emkn.textindex.io.*
import java.io.File

class LinesModeCli : CliktCommand(
    name = "lines",
    help = "Find all lines on which the specified word is mentioned"
) {
    private val indexFilename by option(
        "-i", "--index",
        help = "Text index file"
    ).required()

    private val textFilename by option(
        "-f", "--file",
        help = "Text file on which index was built"
    ).required()

    private val word by option(
        "-w", "--word",
        help = "Word to query"
    ).required()

    override fun run() {
        val index = readTextIndexFromFile(File(indexFilename))
        val textFile = File(textFilename)

        val indices = getIndicesOfLinesWithWord(index, word.cleanUp())
        val lines = getLinesWithIndices(
            lines = textFile.bufferedReader().lineSequence(),
            indices
        )

        printLines(lines)
    }

    private fun printLines(lines: List<String>) {
        for (line in lines)
            println(line)
    }
}