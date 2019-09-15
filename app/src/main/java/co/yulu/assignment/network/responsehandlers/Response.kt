package co.yulu.assignment.network.responsehandlers

import co.yulu.assignment.database.entity.Venue

data class Response(
    val confident: Boolean,
    val venues: List<Venue>
)