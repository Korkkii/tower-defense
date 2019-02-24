package game

import javafx.scene.canvas.GraphicsContext

interface GameEntity {
    fun update()

    fun draw(graphics: GraphicsContext)
}