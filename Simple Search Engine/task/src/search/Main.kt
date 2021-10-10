package search

fun main() {
    val tokens = readLine()!!.split("\\s+".toRegex())
    val searchString = readLine()!!
    val index = tokens.indexOf(searchString)
    println(
        if (index == -1) {
            "Not found"
        } else {
            index + 1
        }
    )
}
