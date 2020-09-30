package nu.westlin.either.evenbetterfileparser

import arrow.core.Either

private val incorrectFile = """
    peter pestlin,Rejsingvägen 1,11111,Medeskora
    Ge Valia,Kaffegränd,ABC123,Gävle
    Tomtefar,Snöstigen 1,12345,Nordpolen
    Snure Sprätt,Fame Street 6,54321,Hollywood 
    Belse Bub,Helvetet 
""".trimIndent()

private val correctFile = """
    peter pestlin,Rejsingvägen 1,11111,Medeskora
    Snure Sprätt,Fame Street 6,54321,Hollywood
""".trimIndent()

private data class Address(val street: String, val zip: Int, val city: String)

private data class Person(val first: String, val last: String, val address: Address)

fun main() {
    incorrectFile
        .reader()
        .readLines()
        .mapIndexed { index, row -> parsePerson(index, row) }
        .forEach { println(it) }
}

private fun parsePerson(rowNo: Int, row: String): Either<ParserError, Person> {
    return try {
        val columns = row.split(",")
        val (first, last) = splitName(columns[0])   // Detta går faktiskt att göra, det kallas "destructuring declarations" - https://kotlinlang.org/docs/reference/multi-declarations.html
        Either.right(Person(first, last, parseAddress(columns)))
    } catch (e: RuntimeException) {
        when (e) {
            is NumberFormatException -> Either.left(ParserError.NumberFormatError(e))
            is IndexOutOfBoundsException -> Either.left(ParserError.ColumntNotFoundError(e))
            else -> Either.left(ParserError.GenereicError(e))
        }
    }
}

private fun parseAddress(columns: List<String>): Address = Address(columns[1], columns[2].toInt(), columns[3])

private fun splitName(name: String): List<String> = name.split(" ")

private sealed class ParserError(open val exception: RuntimeException) {
    class NumberFormatError(override val exception: NumberFormatException) : ParserError(exception)
    class ColumntNotFoundError(override val exception: IndexOutOfBoundsException) : ParserError(exception)
    class GenereicError(override val exception: RuntimeException) : ParserError(exception)

    override fun toString(): String {
        return "${this.javaClass.simpleName}: $exception"
    }

}
