package com.application.clubs

import android.util.Log
import com.application.clubs.api.ClubsEndpoints
import com.application.clubs.api.RetroFitInstance
import com.application.clubs.dataclasses.DataCard
import com.application.clubs.dataclasses.DataSchedule
import com.application.clubs.dataclasses.EventsData
import com.application.clubs.dataclasses.EventsRequest
import com.application.clubs.dataclasses.WorkplaceRequest
import com.application.clubs.utils.TOKEN
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RepositoryImp private constructor() {
    suspend fun workplaceRequestRetrofit(): MutableList<DataCard> {
        val workplaceCalls = RetroFitInstance.getInstance().create(ClubsEndpoints::class.java)

        val workplaceRequest = WorkplaceRequest(1)
        val token = TOKEN

        return try {
            val response = workplaceCalls.workplacePostData(workplaceRequest, token)
            // Обработка успешного ответа
            response.toString()
            eventsRequestRetrofit(response.start_season, response.end_season)
        } catch (e: Exception) {
            Log.d("debug", "error1:${e.message}")
            mutableListOf()
        }
    }

    private suspend fun eventsRequestRetrofit(start: String, end: String): MutableList<DataCard> {
        val eventsCalls = RetroFitInstance.getInstance().create(ClubsEndpoints::class.java)

        val eventsRequest = EventsRequest(1, start, end)
        val token = TOKEN

        return try {
            val response = eventsCalls.eventsPostData(eventsRequest, token)

            //val startTest = "2024-01-01 00:00:00"
            //val endTest = "2024-01-11 23:59:59"
            createList(start, end, response)
        } catch (e: Exception) {
            Log.d("debug", "error2:${e.message}")
            mutableListOf()
        }
    }

    companion object {
        private var INSTANCE: RepositoryImp? = null

        fun getInstance(): RepositoryImp = INSTANCE ?: kotlin.run {
            INSTANCE = RepositoryImp()
            INSTANCE!!
        }
    }

    private fun createList(startDateString: String, endDateString: String, respList:List<EventsData>): MutableList<DataCard> {
        val formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startDate = LocalDateTime.parse(startDateString, formatterTime)
        val endDate = LocalDateTime.parse(endDateString, formatterTime)
        val datesList = mutableListOf<DataCard>()

        var currentDateTime = startDate

        while (!currentDateTime.isAfter(endDate)) {
            var statusDay = ""

            val dateTime = currentDateTime

            val dayOfMonth = dateTime.dayOfMonth.toString()
            val month = dateTime.month.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } // Получаем месяц в виде строки
            val dayOfWeek = dateTime.dayOfWeek.toString().lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val list: MutableList<DataSchedule> = mutableListOf()
            for (i in respList.indices) {
                val dateLocalTime = changeDataTime(LocalDateTime.parse(respList[i].date, formatterTime), respList[i].time_zone)
                val dateLocal = dateLocalTime.toLocalDate()
                val currentDate = currentDateTime.toLocalDate()
                if (currentDate.isEqual(dateLocal)) {
                    val gameInfoCurrent = if (respList[i].game_info==null){
                        "null"
                    }else{
                        "game"
                    }
                    //timeZone Local Time или Home Time
                    val timeZone:String = if (respList[i].time_zone == "Europe/Minsk"){
                        "Home Time"
                    }else{
                        "Local Time"
                    }
                    //Получить время
                    list.add(
                        DataSchedule(
                            dateLocalTime.toLocalTime().toString(),
                            respList[i].title,
                            respList[i].description ?: "",
                            timeZone,
                            gameInfoCurrent
                        )
                    )
                }
            }

            statusDay = checkGameOrTraining(list)

            datesList.add(DataCard(month, dayOfMonth, dayOfWeek, list, statusDay))

            currentDateTime = currentDateTime.plusDays(1)
        }
        return datesList
    }

    private fun checkGameOrTraining(list:MutableList<DataSchedule>):String{
        if (list.isNotEmpty()) {
            for (data in list) {
                if (data.gameInfo != "null") {
                    return "game"
                }
            }
            return "training"
        } else {
            return ""
        }
    }

    private fun changeDataTime(dateTime: LocalDateTime, zoneId:String):LocalDateTime{
        val desiredZone = ZoneId.of(zoneId)

        val desiredZonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("UTC")).withZoneSameInstant(desiredZone)

        return desiredZonedDateTime.toLocalDateTime()
    }
}