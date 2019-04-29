package game

class Publisher {
    private val eventSubscribers = mutableMapOf<Event, MutableList<Observer>>()

    fun subscribeToEvent(event: Event, observer: Observer) {
        val observers = eventSubscribers[event] ?: mutableListOf()
        observers += observer
        eventSubscribers[event] = observers
    }

    fun publish(event: Event) {
        val interestedObservers = eventSubscribers[event]
        interestedObservers?.forEach { it.onNotify(event) }
    }
}
