package co.yulu.assignment.network.responsehandlers

data class Venue(
    val beenHere: BeenHere,
    val categories: List<Category>,
    val contact: Contact,
    val hasPerk: Boolean,
    val hereNow: HereNow,
    val id: String,
    val location: Location,
    val name: String,
    val referralId: String,
    val stats: Stats,
    val venueChains: List<Any>,
    val verified: Boolean
)