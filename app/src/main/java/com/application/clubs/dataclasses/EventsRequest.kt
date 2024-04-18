package com.application.clubs.dataclasses

data class EventsRequest(
    val team_id: Int,
    val start_date:String,
    val end_date:String
)
