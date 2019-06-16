package game

import kotlin.reflect.KClass

class Publisher {
    private val eventSubscribers = mutableMapOf<Class<*>, MutableList<(Event) -> Unit>>()
    private val subscribersToAll = mutableListOf<(Event) -> Unit>()

    fun <T : Event> subscribeToEvent(event: KClass<T>, callback: (T) -> Unit) {
        val eventClass = event.java
        val callbacks = eventSubscribers[eventClass] ?: mutableListOf()

        @Suppress("UNCHECKED_CAST")
        val eventCallback = callback as? (Event) -> Unit ?: return
        callbacks += eventCallback
        eventSubscribers[eventClass] = callbacks
    }

    fun subscribeToAll(callback: (Event) -> Unit) {
        subscribersToAll += callback
    }

    fun publish(event: Event) {
        val interestedObservers = eventSubscribers[event::class.java].orEmpty()
        subscribersToAll.forEach { it(event) }
        interestedObservers.forEach { it(event) }
    }
}
