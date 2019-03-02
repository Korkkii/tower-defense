package game

interface Observer {
    fun onNotify(event: Event)
}