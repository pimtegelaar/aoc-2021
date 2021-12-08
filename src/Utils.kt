import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun verifyEquals(actual: Any, expected: Any) {
    check(actual == expected) { "Failed, expected $expected, but got $actual" }
}

fun String.sort() = toCharArray().sorted().joinToString("")

fun String.containsAll(other : String) = toList().containsAll(other.toList())

fun List<String>.ofLength(length: Int) = filter { it.length == length }

fun List<String>.singleOfLength(length: Int) = ofLength(length).single()
