import kotlin.math.ceil

fun main() {

    data class Sfn(
        var value: Int? = null,
        var left: Sfn? = null,
        var right: Sfn? = null,
        var parent: Sfn? = null
    ) {
        override fun toString(): String {
            return if (value != null) value.toString() else "[$left,$right]"
        }

        val magnitude: Int get() = value ?: ((3 * left!!.magnitude) + (2 * right!!.magnitude))

        val rightMost get() : Sfn = right?.rightMost ?: this
        val leftMost get() : Sfn = left?.leftMost ?: this

        val numberOfParents: Int get() = parent?.numberOfParents?.plus(1) ?: 0

        fun add(other: Sfn) = Sfn(left = this, right = other).apply {
            left!!.parent = this
            right!!.parent = this
        }

        val firstRight: Sfn? get() = if (parent?.left === this) parent?.firstRight else parent?.left
        val firstLeft: Sfn? get() = if (parent?.right === this) parent?.firstLeft else parent?.right

        fun explode() {
            firstRight?.rightMost?.increase(left!!.value!!)
            firstLeft?.leftMost?.increase(right!!.value!!)
            left = null
            right = null
            value = 0
        }

        fun split() {
            left = Sfn(value = value!! / 2, parent = this)
            right = Sfn(value = ceil(value!!.toDouble() / 2).toInt(), parent = this)
            value = null
        }

        fun increase(amount: Int) {
            value = value!! + amount
        }

        fun firstToExplode(): Sfn? {
            if (numberOfParents == 4 && left != null && left!!.value != null && right != null && right!!.value != null) {
                return this
            }
            left?.firstToExplode()?.run {
                return this
            }
            return right?.firstToExplode()
        }

        fun firstToSplit(): Sfn? {
            value?.let {
                if (it > 9) {
                    return this
                }
            }
            left?.firstToSplit()?.run {
                return this
            }
            return right?.firstToSplit()
        }
    }

    fun Sfn.reduce(): Sfn {
        val firstToExplode = firstToExplode()
        if (firstToExplode != null) {
            firstToExplode.explode()
            return reduce()
        }
        val firstToSplit = firstToSplit()
        if (firstToSplit != null) {
            firstToSplit.split()
            return reduce()
        }
        return this
    }

    fun endPosition(input: String): Int {
        var unclosed = 1
        for (i in 1..input.length) {
            if (input[i] == '[') {
                unclosed++
            }
            if (input[i] == ']') {
                unclosed--
            }
            if (unclosed == 0) {
                return i + 1
            }
        }
        return -1
    }

    fun parse(input: String, parent: Sfn? = null): Sfn {
        if (!input.startsWith("[")) {
            return Sfn(value = input.toInt(), parent = parent)
        }
        val core = input.substring(1, input.length - 1)
        val left: Sfn
        val right: Sfn
        if (core.startsWith("[")) {
            val endPosition = endPosition(core)
            left = parse(core.substring(0, endPosition))
            right = parse(core.substring(endPosition + 1))
        } else {
            val (l, r) = core.split(",", limit = 2)
            left = Sfn(l.toInt())
            right = parse(r)
        }
        val sfn = Sfn(left = left, right = right, parent = parent)
        left.parent = sfn
        right.parent = sfn
        return sfn
    }

    fun part1(input: List<String>): Int {
        var currentLine = parse(input.first())
        input.drop(1).forEach { line ->
            currentLine = currentLine.add(parse(line))
            currentLine.reduce()
        }
        return currentLine.magnitude
    }

    fun part2(input: List<String>): Int {
        val magnitudes = mutableListOf<Int>()
        input.forEach { l1 ->
            input.forEach { l2 ->
                if (l1 != l2) {
                    magnitudes.add(parse(l1).add(parse(l2)).reduce().magnitude)
                }
            }
        }
        return magnitudes.reduce(Math::max)
    }

    val testInput = readInput("Day18_test")
    verifyEquals(part1(testInput), 4140)

    val input = readInput("Day18")
    println("Part 1 = " + part1(input))

    verifyEquals(part2(testInput), 3993)
    println("Part 2 = " + part2(input))
}

