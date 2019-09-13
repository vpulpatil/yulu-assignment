package co.yulu.assignment.network.responsehandlers.suggestedplaces

data class Venue(
    val beenHere: BeenHere,
    val categories: List<Category>,
    val contact: Contact,
    val hereNow: HereNow,
    val id: String,
    val location: Location,
    val name: String,
    val photos: Photos,
    val stats: Stats,
    val verified: Boolean
)