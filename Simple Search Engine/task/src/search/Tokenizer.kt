package search

interface Tokenizer {
    fun split(line: String): List<String>
}
