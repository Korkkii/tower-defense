package game

// TODO: Refactor this into global object and subscriptions to take callback function instead of class
// Remove observer class at that point
class Publisher {
    private val eventSubscribers = mutableMapOf<Class<*>, MutableList<Observer>>()
    private val subscribersToAll = mutableListOf<Observer>()

    fun <T : Event> subscribeToEvent(event: Class<T>, observer: Observer) {
        val observers = eventSubscribers[event] ?: mutableListOf()
        observers += observer
        eventSubscribers[event] = observers
    }

    fun subscribeToAll(observer: Observer) {
        subscribersToAll += observer
    }

    fun publish(event: Event) {
        val interestedObservers = eventSubscribers[event::class.java]
        subscribersToAll.forEach { it.onNotify(event) }
        interestedObservers?.forEach { it.onNotify(event) }
    }
}
