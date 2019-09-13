package co.yulu.assignment.network.responsehandlers.suggestedplaces

data class BeenHere(
    val count: Int,
    val lastCheckinExpiredAt: Int,
    val marked: Boolean,
    val unconfirmedCount: Int
)