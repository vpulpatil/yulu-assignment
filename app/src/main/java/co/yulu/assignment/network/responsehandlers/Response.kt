package co.yulu.assignment.network.responsehandlers

data class Response(
    val confident: Boolean,
    val venues: List<Venue>
)