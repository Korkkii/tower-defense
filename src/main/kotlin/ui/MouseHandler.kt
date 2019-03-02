package ui

import game.Vector
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class MouseHandler : EventHandler<MouseEvent> {
    var mousePosition = Vector(0.0, 0.0)
        private set

    override fun handle(event: MouseEvent?) {
        event?.let {
            mousePosition = Vector(it.sceneX, it.sceneY) }
    }
}