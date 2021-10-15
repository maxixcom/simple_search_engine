package search

import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.notExists
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    val filePath = parseFilePathFromArguments(args)
    val lines: List<String> = loadFile(Paths.get(filePath))
    val tokenizer: Tokenizer = TokenizerImpl()
    val index: Index = IndexImpl.fromLines(lines, tokenizer)

    while (true) {
        println(
            """
            === Menu ===
            1. Find a person
            2. Print all people
            0. Exit
            """.trimIndent()
        )
        when (readLine()!!) {
            "1" -> findAPerson(index, lines)
            "2" -> printAllPeople(lines)
            "0" -> break
            else -> println("Incorrect option! Try again.")
        }
    }
    println("Bye!")
}

fun loadFile(path: Path): List<String> {
    if (path.notExists() || !path.isRegularFile()) {
        throw FileNotFoundException("File not found ${path.fileName}")
    }
    return path.readLines()
}

fun parseFilePathFromArguments(args: Array<String>): String {
    val index = args.indexOf("--data")
    if (index == -1 || args.size < index + 2) {
        throw Exception("You have pass mandatory option --data with file path")
    }
    return args[index + 1]
}

fun printAllPeople(lines: List<String>) {
    println("=== List of people ===")
    lines.forEach {
        println(it)
    }
}

fun findAPerson(index: Index, lines: List<String>) {
    println("Select a matching strategy: ALL, ANY, NONE")
    val strategy = try {
        Strategy.valueOf(readLine()!!)
    } catch (e: IllegalArgumentException) {
        return
    }

    println("Enter a name or email to search all suitable people.")
    val query = readLine()!!

    val result = when (strategy) {
        Strategy.ANY -> index.findAny(query)
        Strategy.ALL -> index.findAll(query)
        Strategy.NONE -> index.findNone(query)
    }

    if (result.isNotEmpty()) {
        println("People found:")
        result.forEach {
            println(lines[it])
        }
    } else {
        println("No matching people found.")
    }
}
