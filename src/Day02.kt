import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { report ->
            if(isValid(report, true)){
                1.toInt()
            }else {
                0.toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {

        return input.sumOf { report ->
            if(isValid(report, false)){
                1.toInt()
            }else {
                0.toInt()
            }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)
    part2(testInput).println()

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun isValid(report: String, stepAlreadyRemoved: Boolean): Boolean {
    var currentOrder = Order.NONE
    val regex = Regex("\\d+")
    val values = regex.findAll(report)
        .map { it.value.toInt() }.toList()
    val valid = values.windowed(2,1,false)
        .indexOfFirst { ints: List<Int> ->
            val (left, right) = ints
            val orderValue = right - left
            val newOrder = when{
                orderValue>0 ->  Order.ASCENDING
                orderValue<0 -> Order.DESCENDING
                else -> Order.NONE
            }

            if(currentOrder == Order.NONE){
                currentOrder = newOrder
            }
            val hasRightStepping = abs(orderValue) in 1..3
            val isOrderRight = currentOrder == newOrder
            !hasRightStepping || !isOrderRight
        }

    return if(valid != -1){
        if(stepAlreadyRemoved){
            return false
        }
        IntRange(0, valid + 1).any {
            val reducedList = values.toMutableList()
            reducedList.removeAt(it)
            isValid(reducedList.joinToString(" "), true)
        }
    }else{
        true
    }
}

enum class Order {
    ASCENDING, DESCENDING, NONE
}