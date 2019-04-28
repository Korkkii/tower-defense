package game

class Publisher {
    val subscribers = mutableListOf<Observer>()

    fun publish(event: Event) {
        subscribers.forEach { it.onNotify(event) }
    }
}
