package game

class Publisher {
    private val eventSubscribers = mutableMapOf<Event, MutableList<Observer>>()
    private val subscribersToAll = mutableListOf<Observer>()

    fun subscribeToEvent(event: Event, observer: Observer) {
        val observers = eventSubscribers[event] ?: mutableListOf()
        observers += observer
        eventSubscribers[event] = observers
    }

    fun subscribeToAll(observer: Observer) {
        subscribersToAll += observer
    }

    fun publish(event: Event) {
        val interestedObservers = eventSubscribers[event]
        subscribersToAll.forEach { it.onNotify(event) }
        interestedObservers?.forEach { it.onNotify(event) }
    }
}
