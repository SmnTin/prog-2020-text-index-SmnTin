package ru.emkn.textindex.modes.build

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*

import ru.emkn.textindex.io.writeTextIndexToFile

import java.io.File

class BuildModeCli : CliktCommand(
    name = "build",
    help = "Build index for a specified file"
) {
    private val indexFilename by option(
        "--index", "-i",
        help = "Index file to which built index is written"
    ).default("index.i")

    private val textFilename by option(
        "--file", "-f",
        help = "Text file to build index on"
    ).required()

    override fun run() {
        val textFile = File(textFilename)
        val indexFile = File(indexFilename)

        val index = buildIndexForFile(textFile.bufferedReader().lineSequence())

        writeTextIndexToFile(
            index,
            indexFile
        )
    }
}