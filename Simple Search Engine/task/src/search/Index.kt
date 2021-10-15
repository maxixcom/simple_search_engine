package search

interface Index {
    /**
     * If the strategy is ANY, the program should print the lines containing at least one word from the query.
     * @return list of lines where token is found
     */
    fun findAny(query: String): Set<Int>

    /**
     * If the strategy is ALL, the program should print the lines containing all the words from the query.
     * @return list of lines where token is found
     */
    fun findAll(query: String): Set<Int>

    /**
     * If the strategy is NONE, the program should print the lines that do not contain any words from the query at all.
     * @return list of lines where token is found
     */
    fun findNone(query: String): Set<Int>
}
