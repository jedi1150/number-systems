package ru.sandello.binaryconverter.numsys.model

public data class Radix(public val value: Int) {

    init {
        require(value > 2 || value < 36) {
            "Radix must be greater than 2 and smaller than 36"
        }
    }

    public companion object {
        @JvmField
        public val BIN: Radix = Radix(2)

        @JvmField
        public val OCT: Radix = Radix(8)

        @JvmField
        public val DEC: Radix = Radix(10)

        @JvmField
        public val HEX: Radix = Radix(16)
    }
}

private val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !arrayOf(Radix(1)).contains(radix) }
private val groupByThreeNumbers: List<Radix> = radixes.filter { listOf(Radix(3), Radix(7), Radix.OCT, Radix(9), Radix.DEC, Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }
private val groupByFourNumbers: List<Radix> = radixes.filter { !listOf(Radix(3), Radix(7), Radix.OCT, Radix(9), Radix.DEC, Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }

public fun Radix.groupLength(): Int = when (this) {
    in groupByThreeNumbers -> 3
    in groupByFourNumbers -> 4
    else -> 3
}

public fun NumberSystem.groupLength(): Int = radix.groupLength()
