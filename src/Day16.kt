import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.nio.CharBuffer

private const val TYPE_LENGTH = 3
private const val VERSION_LENGTH = 3
private const val DATA_PART_LENGTH = 5
private const val LENGTH_LENGTH = 15
private const val SIZE_LENGTH = 11
private const val LENGTH_TYPE_LENGTH = 1

fun main() {

    data class Packet(
        val version: String,
        val type: String,
        val length: Int,
        val data: String? = null,
        val subPackets: List<Packet> = emptyList()
    ) {
        fun versionSum(): Int = Integer.parseInt(version, 2) + subPackets.sumOf { it.versionSum() }

        fun getValue(): Long = when (type) {
            "000" -> subPackets.sumOf { it.getValue() }
            "001" -> subPackets.map { it.getValue() }.reduce(Long::times)
            "010" -> subPackets.map { it.getValue() }.reduce(Math::min)
            "011" -> subPackets.map { it.getValue() }.reduce(Math::max)
            "100" -> BigInteger(data, 2).toLong()
            "101" -> if (subPackets[0].getValue() > subPackets[1].getValue()) 1 else 0
            "110" -> if (subPackets[0].getValue() < subPackets[1].getValue()) 1 else 0
            "111" -> if (subPackets[0].getValue() == subPackets[1].getValue()) 1 else 0
            else -> throw IllegalStateException("Unexpected type '$type'")
        }
    }

    fun Char.toBinary(): String = when (this) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> throw IllegalArgumentException("'$this' is not a valid hexadecimal character")
    }

    fun String.toBinary() = map(Char::toBinary).joinToString("")

    fun BufferedReader.readString(buffer: CharBuffer): String {
        read(buffer)
        buffer.flip()
        return buffer.toString()
    }

    fun BufferedReader.readInt(buffer: CharBuffer) = Integer.parseInt(readString(buffer), 2)

    fun buffer(size: Int) = CharBuffer.allocate(size)

    val versionBuffer = buffer(VERSION_LENGTH)
    val typeBuffer = buffer(TYPE_LENGTH)
    val dataBuffer = buffer(DATA_PART_LENGTH)
    val lengthBuffer = buffer(LENGTH_LENGTH)
    val sizeBuffer = buffer(SIZE_LENGTH)

    fun BufferedReader.readLiteralPacket(version: String, type: String): Packet {
        var foundLast = false
        var rawData = ""
        while (!foundLast) {
            val part = readString(dataBuffer)
            rawData += part.substring(1)
            if (part.first() == '0') {
                foundLast = true
            }
        }
        val packetLength = (6 + rawData.length * 1.25).toInt()
        return Packet(version, type, packetLength, data = rawData)
    }

    fun BufferedReader.readPacket(): Packet {
        val version = readString(versionBuffer)
        val type = readString(typeBuffer)
        if (type == "100") {
            return readLiteralPacket(version, type)
        }
        val subPackets = mutableListOf<Packet>()
        var bitsRead = 0
        val packetLength: Int
        if (readString(buffer(LENGTH_TYPE_LENGTH)) == "0") {
            val length = readInt(lengthBuffer)
            while (bitsRead != length) {
                val packet = readPacket()
                subPackets.add(packet)
                bitsRead += packet.length
            }
            packetLength = VERSION_LENGTH + TYPE_LENGTH + LENGTH_TYPE_LENGTH + LENGTH_LENGTH + length
        } else {
            val size = readInt(sizeBuffer)
            for (x in 0 until size) {
                val packet = readPacket()
                subPackets.add(packet)
                bitsRead += packet.length
            }
            packetLength = VERSION_LENGTH + TYPE_LENGTH + LENGTH_TYPE_LENGTH + SIZE_LENGTH + bitsRead
        }
        return Packet(version, type, packetLength, subPackets = subPackets)
    }

    fun parse(input: String): MutableList<Packet> {
        val reader = BufferedReader(InputStreamReader(input.byteInputStream()))

        val packets = mutableListOf<Packet>()

        var totalRead = 0
        while (totalRead < input.length - 8) {
            val packet = reader.readPacket()
            packets.add(packet)
            totalRead += packet.length
        }
        return packets
    }

    fun part1(input: String) = parse(input).sumOf { it.versionSum() }

    fun part2(input: String) = parse(input).sumOf { it.getValue() }

    verifyEquals(part1("D2FE28".toBinary()), 6)
    verifyEquals(part1("38006F45291200".toBinary()), 9)
    verifyEquals(part1("8A004A801A8002F478".toBinary()), 16)
    verifyEquals(part1("620080001611562C8802118E34".toBinary()), 12)
    verifyEquals(part1("C0015000016115A2E0802F182340".toBinary()), 23)
    verifyEquals(part1("A0016C880162017C3686B18A3D4780".toBinary()), 31)

    val input = readInput("Day16").first().toBinary()
    println("Part 1 = " + part1(input))

    verifyEquals(part2("C200B40A82".toBinary()), 3L)
    verifyEquals(part2("04005AC33890".toBinary()), 54L)
    verifyEquals(part2("880086C3E88112".toBinary()), 7L)
    verifyEquals(part2("CE00C43D881120".toBinary()), 9L)
    verifyEquals(part2("D8005AC2A8F0".toBinary()), 1L)
    verifyEquals(part2("F600BC2D8F".toBinary()), 0L)
    verifyEquals(part2("9C005AC2F8F0".toBinary()), 0L)
    verifyEquals(part2("9C0141080250320F1802104A08".toBinary()), 1L)

    println("Part 2 = " + part2(input))
}
