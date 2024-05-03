package com.application.clubs.dataclasses

data class DataSchedule(
    var time:String = "",
    var name: String = "",
    var description: String = "",
    var timeZone: String = "",
    var schedule: String = "",
    var gameInfo: List<DataGame> = listOf()
)
