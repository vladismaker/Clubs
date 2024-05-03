package com.application.clubs.dataclasses

data class DataCard(
    var fullData: String,
    var dateMonth: String,
    var dateDay: String,
    var dateDayOfWeek: String,
    var listSchedule: MutableList<DataSchedule>,
    var status:String
)
