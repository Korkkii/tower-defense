package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Affine
import kotlin.reflect.KClass

fun <T, U> List<List<U>>.map(mappingFunction: (x: Int, y: Int, cellValue: U) -> T): List<List<T>> {
    return this.mapIndexed { columnIndex, cells ->
        cells.mapIndexed { rowIndex, cellValue ->
            mappingFunction(rowIndex, columnIndex, cellValue)
        }
    }
}

fun Circle.center(): Vector = Vector(this.centerX, this.centerY)

fun Circle(center: Vector, radius: Double) = Circle(center.x, center.y, radius)

fun Circle.contains(position: Vector) = this.contains(position.x, position.y)

fun GraphicsContext.fillCircle(circle: Circle) =
    this.fillOval(circle.centerX - circle.radius, circle.centerY - circle.radius, 2 * circle.radius, 2 * circle.radius)

fun GraphicsContext.strokeCircle(circle: Circle) =
    this.strokeOval(
        circle.centerX - circle.radius,
        circle.centerY - circle.radius,
        2 * circle.radius,
        2 * circle.radius
    )

fun MouseEvent.position() = Vector(this.sceneX, this.sceneY)

fun Affine.inverseTransform(point: Vector): Vector {
    return this.inverseTransform(point.toPoint2D()).toVector()
}

fun Color.withOpacity(opacity: Double): Color {
    return Color(this.red, this.green, this.blue, opacity)
}

fun <T : Any, U : T> Iterable<T>.find(type: KClass<U>): U? = filterIsInstance(type.java).firstOrNull()
