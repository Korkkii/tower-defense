package ui

import game.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.Rectangle

fun GraphicsContext.fillRectangle(rectangle: Rectangle) {
    this.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
}

fun rectangle(position: Vector, size: Vector): Rectangle = Rectangle(position.x, position.y, size.x, size.y)
