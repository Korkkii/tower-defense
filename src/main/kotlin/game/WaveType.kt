package game

import game.enemies.Enemy
import game.enemies.EnemyType
import kotlin.random.Random.Default.nextInt

private val basicWave = listOf(EnemyType.enemy)
private val fastWave = listOf(EnemyType.fastEnemy)
private val mixed = listOf(EnemyType.enemy, EnemyType.fastEnemy)
private val types = listOf(basicWave, fastWave, mixed)

fun createWave(path: List<PathSquare>, level: Int) = types[nextInt(0, 3)].flatMap { type ->
    val count = nextInt(1, 5)
    (0..count).map { Enemy(path, type, level) }
}
