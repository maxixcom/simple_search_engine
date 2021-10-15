package search

class TokenizerImpl : Tokenizer {
    override fun split(line: String): List<String> = line.split("\\s+".toRegex())
}
