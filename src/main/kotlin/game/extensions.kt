package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Circle

fun <T, U> List<List<U>>.map(mappingFunction: (x: Int, y: Int, cellValue: U) -> T ): List<List<T>> {
    return this.mapIndexed { columnIndex, cells ->
        cells.mapIndexed { rowIndex, cellValue ->
            mappingFunction(rowIndex, columnIndex, cellValue) } }
}

fun <T, U> List<List<U>>.flatMap(mappingFunction: (x: Int, y: Int, cellValue: U) -> T ): List<T> {
    return this.map(mappingFunction).flatten()
}

fun Circle.center(): Vector = Vector(this.centerX, this.centerY)

fun Circle(center: Vector, radius: Double) = Circle(center.x, center.y, radius)

fun GraphicsContext.fillCircle(circle: Circle) =
    this.fillOval(circle.centerX - circle.radius, circle.centerY - circle.radius, 2 * circle.radius, 2 * circle.radius)

fun GraphicsContext.strokeCircle(circle: Circle) =
    this.strokeOval(circle.centerX - circle.radius, circle.centerY - circle.radius, 2 * circle.radius, 2 * circle.radius)

fun MouseEvent.position() = Vector(this.sceneX, this.sceneY)