package co.yulu.assignment.network.responsehandlers.suggestedplaces

data class HereNow(
    val count: Int,
    val groups: List<Any>,
    val summary: String
)