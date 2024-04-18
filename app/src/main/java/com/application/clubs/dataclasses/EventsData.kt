package com.application.clubs.dataclasses

data class EventsData(
    val date:String,
    val time_zone:String,
    val title:String,
    val description:String,
    val game_info:List<Any>
)
