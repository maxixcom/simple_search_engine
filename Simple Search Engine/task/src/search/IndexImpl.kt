package search

import java.io.FileNotFoundException
import java.nio.file.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.notExists
import kotlin.io.path.readLines

class IndexImpl(
    private val data: Map<String, Set<Int>> = emptyMap(),
    private val tokenizer: Tokenizer,
) : Index {

    override fun findAny(query: String): Set<Int> =
        tokenizer.split(query.lowercase()).flatMap {
            data[it] ?: emptySet()
        }.toSet()

    override fun findAll(query: String): Set<Int> =
        tokenizer.split(query.lowercase())
            .map {
                data[it] ?: emptySet()
            }
            .reduce { acc, set ->
                acc.intersect(set)
            }

    override fun findNone(query: String): Set<Int> {
        val found = findAny(query)
        return data.values
            .flatten()
            .toSet()
            .filter { it !in found }
            .toSet()
    }

    companion object {
        fun fromFile(path: Path, tokenizer: Tokenizer): Index {
            if (path.notExists() || !path.isRegularFile()) {
                throw FileNotFoundException("File not found ${path.fileName}")
            }
            return fromLines(path.readLines(), tokenizer)
        }

        fun fromLines(lines: List<String>, tokenizer: Tokenizer): Index {
            val data = mutableMapOf<String, Set<Int>>()
            lines.flatMapIndexed { index, line ->
                tokenizer.split(line)
                    .map { it.lowercase() to index }
            }.forEach {
                data.merge(it.first, setOf(it.second)) { oldValue, newValue ->
                    oldValue + newValue
                }
            }
            return IndexImpl(data, tokenizer)
        }
    }
}
