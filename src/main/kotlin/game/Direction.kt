package game

sealed class Direction(val x: Int, val y: Int)
object Up : Direction(0, 1)
object Down : Direction(0, -1)
object Left : Direction(-1, 0)
object Right : Direction(1, 0)

val directions = listOf(Up, Down, Left, Right)
