fun main() {
    fun part1(input: String): Int {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        var instructionResult = 0
        var foundMatch = regex.find(input)

        while (foundMatch != null) {
            val (left, right) = foundMatch.groupValues.drop(1)
            instructionResult += left.toInt() * right.toInt()
            foundMatch = foundMatch.next()
        }

        return instructionResult
    }

    fun part2(input: String): Int {
        val instructionRegex = Regex("do\\(\\)|don't\\(\\)")
        var instructionResult = 0
        var controlInstructions = instructionRegex.findAll(input).map {
            Pair(it.groups[0]!!.value, it.groups[0]!!.range.last)
        }.toList()

        var currentInstruction: Pair<String, Int> = Pair("do()", 0)
        do {
            if (currentInstruction.first == "do()") {
                controlInstructions = controlInstructions.dropWhile { it.first == "do()" }
                if (controlInstructions.isEmpty()) {
                    instructionResult += part1(input.substring(currentInstruction.second))
                }else{
                    val nextInstruction = controlInstructions[0]
                    instructionResult += part1(input.substring(currentInstruction.second, nextInstruction.second))
                    currentInstruction = nextInstruction
                }
            }else {
                controlInstructions = controlInstructions.dropWhile { it.first == "don't()" }
                if(controlInstructions.isNotEmpty()){
                    currentInstruction = controlInstructions[0]
                }
            }
        } while (controlInstructions.isNotEmpty())

        return instructionResult
    }

    val testInput = readInputAsOneLine("Day03_test")
    check(part1(testInput) == 161)
    val testInput2 = readInputAsOneLine("Day03_test2")
    check(part2(testInput2) == 48)

    val input = readInputAsOneLine("Day03")
    part1(input).println()
    part2(input).println()
}
