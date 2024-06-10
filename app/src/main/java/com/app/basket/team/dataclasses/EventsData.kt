package com.app.basket.team.dataclasses

data class EventsData(
    val date:String,
    val time_zone:String,
    val title:String,
    val description:String,
    val schedule:String,
    val game_info:List<DataGame>
)
