package ru.emkn.textindex.io

fun String.cleanUp(): String =
    this.filter(Character::isLetter)
        .toLowerCase()