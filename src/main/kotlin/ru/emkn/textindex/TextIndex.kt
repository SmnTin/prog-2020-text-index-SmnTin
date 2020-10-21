package ru.emkn.textindex

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

import ru.emkn.textindex.modes.build.BuildModeCli

class TextIndexCli : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) =
    TextIndexCli()
        .subcommands(BuildModeCli())
        .main(args)