package com.app.basket.team

import android.util.Log
import com.app.basket.team.api.ClubsEndpoints
import com.app.basket.team.api.RetroFitInstance
import com.app.basket.team.dataclasses.DataCard
import com.app.basket.team.dataclasses.DataGame
import com.app.basket.team.dataclasses.DataSchedule
import com.app.basket.team.dataclasses.EventsData
import com.app.basket.team.dataclasses.EventsRequest
import com.app.basket.team.dataclasses.WorkplaceRequest
import com.app.basket.team.utils.TEAM_ID
import com.app.basket.team.utils.TOKEN
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class RepositoryCalendarImpl private constructor() {
    suspend fun workplaceRequestRetrofit(): Pair<Int, MutableList<DataCard>> {
        val workplaceCalls = RetroFitInstance.getInstance().create(ClubsEndpoints::class.java)

        val workplaceRequest = WorkplaceRequest(TEAM_ID)
        val token = TOKEN

        return try {
            val response = workplaceCalls.workplacePostData(workplaceRequest, token)
            // Обработка успешного ответа
            response.toString()
            eventsRequestRetrofit(response.start_season, response.end_season)
        } catch (e: Exception) {
            Log.d("debug", "error1:${e.message}")
            Pair(0, mutableListOf())
        }
    }

    private suspend fun eventsRequestRetrofit(
        start: String,
        end: String
    ): Pair<Int, MutableList<DataCard>> {
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
            Pair(0, mutableListOf())
        }
    }

    companion object {
        private var INSTANCE: RepositoryCalendarImpl? = null

        fun getInstance(): RepositoryCalendarImpl = INSTANCE ?: kotlin.run {
            INSTANCE = RepositoryCalendarImpl()
            INSTANCE!!
        }
    }

    private fun createList(
        startDateString: String,
        endDateString: String,
        respList: List<EventsData>
    ): Pair<Int, MutableList<DataCard>> {
        val formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startDate = LocalDateTime.parse(startDateString, formatterTime)
        val endDate = LocalDateTime.parse(endDateString, formatterTime)
        val datesList = mutableListOf<DataCard>()

        var currentDateTime = startDate

        while (!currentDateTime.isAfter(endDate)) {
            var statusDay = ""

            val dateTime = currentDateTime

            val dayOfMonth = dateTime.dayOfMonth.toString()
            val month = dateTime.month.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } // Получаем месяц в виде строки
            val dayOfWeek = dateTime.dayOfWeek.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val list: MutableList<DataSchedule> = mutableListOf()
            for (i in respList.indices) {
                val dateLocalTime = changeDataTime(
                    LocalDateTime.parse(respList[i].date, formatterTime),
                    respList[i].time_zone
                )
                val dateLocal = dateLocalTime.toLocalDate()
                val currentDate = currentDateTime.toLocalDate()
                if (currentDate.isEqual(dateLocal)) {
                    Log.d("debug", "respList[i].game_info:${respList[i].game_info}")
                    val gameInfoCurrent:List<DataGame> = if (respList[i].game_info == null) {
                        Log.d("debug", "Рельно null")
                        listOf()
                    } else {
                        Log.d("debug", "Пришел не null")
                        Log.d("debug", "")
                        //getDataGame(respList[i].game_info)
                        respList[i].game_info
                    }
                    //timeZone Local Time или Home Time
                    val timeZone: String = if (respList[i].time_zone == "Europe/Minsk") {
                        "Home Time"
                    } else {
                        "Local Time"
                    }
                    //Получить время
                    list.add(
                        DataSchedule(
                            dateLocalTime.toLocalTime().toString(),
                            respList[i].title,
                            respList[i].description ?: "",
                            timeZone,
                            respList[i].schedule?:"",
                            gameInfoCurrent
                        )
                    )
                }
            }

            statusDay = checkGameOrTraining(list)

            datesList.add(
                DataCard(
                    dateTime.toLocalDate().toString(),
                    month,
                    dayOfMonth,
                    dayOfWeek,
                    list,
                    statusDay
                )
            )

            currentDateTime = currentDateTime.plusDays(1)
        }


        return Pair(checkPositionDate(getDataNow(), datesList), datesList)
    }

    fun checkPositionDate(date: String, datesList: MutableList<DataCard>): Int {
        val dateFromList = datesList[0].fullData
        var selectPositionDate = 0
        Log.d("debug", "dateNow:$date")
        Log.d("debug", "dateFromList:$dateFromList")
        for (i in 0 until datesList.size) {
            if (date==datesList[i].fullData){
                selectPositionDate =  i
                break
            }
            else
                selectPositionDate = 0
        }
        Log.d("debug", "selectPositionDate:${selectPositionDate}")
        return  selectPositionDate
    }

    private fun getDataNow(): String {
        val formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Получение текущей даты
        val currentDate = LocalDate.now()

        // Форматирование даты в строку
        return currentDate.format(formatterTime)
    }

    private fun checkGameOrTraining(list: MutableList<DataSchedule>): String {
        if (list.isNotEmpty()) {
            for (data in list) {
                if (data.gameInfo.isNotEmpty()) {
                    return "game"
                }
            }
            return "training"
        } else {
            return ""
        }
    }

    fun convertLongToStringData(dataLong:Long):String{
        //val dateInMillis: Long = 1679932800000 // Пример

        // Преобразование DateInMillis в объект Date
        val date = Date(dataLong)

        // Форматирование даты в строку в формате "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun changeDataTime(dateTime: LocalDateTime, zoneId: String): LocalDateTime {
        val desiredZone = ZoneId.of(zoneId)

        val desiredZonedDateTime =
            ZonedDateTime.of(dateTime, ZoneId.of("UTC")).withZoneSameInstant(desiredZone)

        return desiredZonedDateTime.toLocalDateTime()
    }

    private fun getDataGame(list: List<DataGame>):List<DataGame>{
        Log.d("debug", "list:${list[0].title}")
        Log.d("debug", "list:${list[0].data}")
        return list
    }
}