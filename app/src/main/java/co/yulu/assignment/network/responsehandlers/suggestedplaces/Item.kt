package co.yulu.assignment.network.responsehandlers.suggestedplaces

import co.yulu.assignment.database.entity.Venue

data class Item(
    val reasons: Reasons,
    val referralId: String,
    val venue: Venue
)