package game.board

sealed class Direction(val x: Int, val y: Int)
object Up : Direction(0, -1) // Inverted Y axis
object Down : Direction(0, 1) // Inverted Y axis
object Left : Direction(-1, 0)
object Right : Direction(1, 0)
