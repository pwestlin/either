package nu.westlin.either.betterfileparser

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
        .map { parsePerson(it) }
        .forEach { println(it) }
}

private fun parsePerson(row: String): Either<RuntimeException, Person> {
    return try {
        val columns = row.split(",")
        val (first, last) = splitName(columns[0])   // Detta går faktiskt att göra, det kallas "destructuring declarations" - https://kotlinlang.org/docs/reference/multi-declarations.html
        Either.right(Person(first, last, parseAddress(columns)))
    } catch(e: RuntimeException) {
        Either.left(e)
    }
}

private fun parseAddress(columns: List<String>): Address = Address(columns[1], columns[2].toInt(), columns[3])

private fun splitName(name: String): List<String> = name.split(" ")
