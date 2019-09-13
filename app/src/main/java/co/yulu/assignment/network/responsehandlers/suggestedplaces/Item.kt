package co.yulu.assignment.network.responsehandlers.suggestedplaces

data class Item(
    val reasons: Reasons,
    val referralId: String,
    val venue: Venue
)