import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val locationIdStart = arrayListOf<Int>()
        val locationIdEnd = arrayListOf<Int>()
        val regex = Regex("\\d+")
        input.forEach {
            regex.findAll(it).forEachIndexed { index, matchResult ->
                if(index==0){
                    locationIdStart.add(matchResult.value.toInt())
                }else{
                    locationIdEnd.add(matchResult.value.toInt())
                }
            }
        }
        locationIdStart.sort()
        locationIdEnd.sort()
        var sumDistance = 0
        locationIdStart.forEachIndexed { index, location ->
            val endLocation = locationIdEnd[index]
            sumDistance += abs(endLocation - location)
        }
        return sumDistance
    }

    fun part2(input: List<String>): Int {
        val locationIdStart = arrayListOf<Int>()
        val locationIdEnd = hashMapOf<Int, Int>()
        val regex = Regex("\\d+")
        input.forEach {
            regex.findAll(it).forEachIndexed { index, matchResult ->
                if(index==0){
                    locationIdStart.add(matchResult.value.toInt())
                }else{
                    val locationId = matchResult.value.toInt()
                    val currentCount = locationIdEnd.getOrDefault(locationId, 0)
                    locationIdEnd[matchResult.value.toInt()] = currentCount +1
                }
            }
        }
        return locationIdStart.sumOf {
            it * locationIdEnd.getOrDefault(it, 0)
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
