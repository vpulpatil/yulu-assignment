package co.yulu.assignment.network.responsehandlers.suggestedplaces

data class Group(
    val items: List<Item>,
    val name: String,
    val type: String
)