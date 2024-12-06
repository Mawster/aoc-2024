fun main() {
    fun part1(input: List<String>): Int {
        val positionsToCheck = findAllPositionsToCheckForCharacter(input, 'X')
        val horizontalMatches =
            positionsToCheck.sumOf { checkHorizontalFromPositionWithCheckString(it, input, "XMAS") }
        val verticalMatches =
            positionsToCheck.sumOf { checkVerticalFromPositionWithCheckString(it, input, "XMAS") }
        val diagonalMatches =
            positionsToCheck.sumOf { checkDiagonalFromPositionWithCheckString(it, input, "XMAS") }

        return horizontalMatches + verticalMatches + diagonalMatches
    }

    fun part2(input: List<String>): Int {
        val positionsToCheck = findAllPositionsToCheckForCharacter(input, 'A')

        val foundMases = positionsToCheck.filter {
            listOf(
                checkContainsStringInDiagonalDirection(Pair(it.first - 1, it.second - 1), input, "MAS", Direction.RIGHT_LOW),
                checkContainsStringInDiagonalDirection(Pair(it.first - 1, it.second + 1), input, "MAS", Direction.LEFT_LOW),
                checkContainsStringInDiagonalDirection(Pair(it.first + 1, it.second - 1), input, "MAS", Direction.RIGHT_UP),
                checkContainsStringInDiagonalDirection(Pair(it.first + 1, it.second + 1), input, "MAS", Direction.LEFT_UP),
            ).count { it } == 2
        }

        return foundMases.toHashSet().count()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun findAllPositionsToCheckForCharacter(
    input: List<String>,
    character: Char
): List<Pair<Int, Int>> {
    val foundIndices = mutableListOf<Pair<Int, Int>>()
    input.forEachIndexed { index, line ->
        var match = Regex(character.toString()).find(line)
        while (match != null) {
            foundIndices.add(Pair(index, match.range.first))
            match = match.next()
        }
    }
    return foundIndices
}

fun checkHorizontalFromPositionWithCheckString(
    position: Pair<Int, Int>,
    input: List<String>,
    checkString: String
): Int {
    val checkSize = checkString.length
    val lineToCheck = input[position.first]
    var matches = 0
    if (position.second + (checkSize - 1) < lineToCheck.length &&
        lineToCheck.substring(position.second, position.second + checkSize).contains(checkString)
    ) {
        matches++;
    }
    if (position.second - (checkSize - 1) >= 0 &&
        lineToCheck.substring(position.second - (checkSize - 1), position.second + 1)
            .contains(checkString.reversed())
    ) {
        matches++;
    }
    return matches
}

fun checkVerticalFromPositionWithCheckString(
    position: Pair<Int, Int>,
    input: List<String>,
    checkString: String
): Int {
    val checkSize = checkString.length
    val lineToCheck = input.map {
        it[position.second]
    }.joinToString("")
    var matches = 0
    if (position.first + (checkSize - 1) < lineToCheck.length &&
        lineToCheck.substring(position.first, position.first + checkSize).contains(checkString)
    ) {
        matches++;
    }
    if (position.first - (checkSize - 1) >= 0 &&
        lineToCheck.substring(position.first - checkSize + 1, position.first + 1)
            .contains(checkString.reversed())
    ) {
        matches++;
    }
    return matches
}

fun Pair<Int, Int>.valid(maxRangeVertical: Int, maxRangeHorizontal: Int): Boolean {
    return first in 0..maxRangeVertical && second in 0..maxRangeHorizontal
}

fun checkDiagonalFromPositionWithCheckString(
    position: Pair<Int, Int>,
    input: List<String>,
    checkString: String
): Int {
    val maxIndexHorizontal = input[0].length - 1
    val maxIndexVertical = input.size - 1
    val checkSize = checkString.length
    val indexMove = checkSize - 1
    val stringsToCheck = mutableListOf<String>()
    val leftUpperPosition = Pair(position.first - indexMove, position.second - indexMove)
    if (leftUpperPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
        val toCheck = (0..indexMove).map {
            input[position.first - it][position.second - it]
        }.joinToString("")
        stringsToCheck.add(toCheck)
    }
    val rightUpperPosition = Pair(position.first - indexMove, position.second + indexMove)
    if (rightUpperPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
        val toCheck = (0..indexMove).map {
            input[position.first - it][position.second + it]
        }.joinToString("")
        stringsToCheck.add(toCheck)
    }
    val leftLowerPosition = Pair(position.first + indexMove, position.second - indexMove)
    if (leftLowerPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
        val toCheck = (0..indexMove).map {
            input[position.first + it][position.second - it]
        }.joinToString("")
        stringsToCheck.add(toCheck)
    }
    val rightLowerPosition = Pair(position.first + indexMove, position.second + indexMove)
    if (rightLowerPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
        val toCheck = (0..indexMove).map {
            input[position.first + it][position.second + it]
        }.joinToString("")
        stringsToCheck.add(toCheck)
    }

    return stringsToCheck.count { it.contains(checkString) }
}

fun checkContainsStringInDiagonalDirection(
    position: Pair<Int, Int>,
    input: List<String>,
    checkString: String,
    direction: Direction
): Boolean {
    val maxIndexHorizontal = input[0].length - 1
    val maxIndexVertical = input.size - 1
    val checkSize = checkString.length
    val indexMove = checkSize - 1
    if(!position.valid(maxIndexVertical, maxIndexHorizontal)){
        return false
    }
    return when (direction) {
        Direction.LEFT_LOW -> {
            val leftLowerPosition = Pair(position.first + indexMove, position.second - indexMove)
            return if (leftLowerPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
                (0..indexMove).map {
                    input[position.first + it][position.second - it]
                }.joinToString("").contains(checkString)
            } else {
                false
            }
        }

        Direction.LEFT_UP -> {
            val leftUpperPosition = Pair(position.first - indexMove, position.second - indexMove)
            if (leftUpperPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
                (0..indexMove).map {
                    input[position.first - it][position.second - it]
                }.joinToString("").contains(checkString)
            } else {
                false
            }
        }

        Direction.RIGHT_LOW -> {
            val rightLowerPosition = Pair(position.first + indexMove, position.second + indexMove)
            if (rightLowerPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
                (0..indexMove).map {
                    input[position.first + it][position.second + it]
                }.joinToString("").contains(checkString)
            } else {
                false
            }
        }

        Direction.RIGHT_UP -> {
            val rightUpperPosition = Pair(position.first - indexMove, position.second + indexMove)
            if (rightUpperPosition.valid(maxIndexVertical, maxIndexHorizontal)) {
                (0..indexMove).map {
                    input[position.first - it][position.second + it]
                }.joinToString("").contains(checkString)
            } else {
                false
            }
        }
    }
}

enum class Direction {
    LEFT_UP, LEFT_LOW, RIGHT_UP, RIGHT_LOW
}