package co.yulu.assignment.network.responsehandlers.suggestedplaces

import co.yulu.assignment.database.entity.BeenHere
import co.yulu.assignment.database.entity.Category
import co.yulu.assignment.database.entity.HereNow
import co.yulu.assignment.database.entity.Location
import co.yulu.assignment.database.entity.Photos
import co.yulu.assignment.database.entity.Stats

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