package com.app.basket.team.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.basket.team.dataclasses.DataCard
import com.app.basket.team.dataclasses.DataSchedule
import com.app.basket.team.RepositoryCalendarImpl
import com.app.basket.team.dataclasses.DataGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private var coroutine: Job? = null
    private val clubsRepository = RepositoryCalendarImpl.getInstance()

    val liveDataDate = MutableLiveData<String>()
    val liveDataDayOff = MutableLiveData<Boolean>()
    var liveDataArrayDate = MutableLiveData<Pair<Int, MutableList<DataCard>>>()
    val liveDataArraySchedule = MutableLiveData<MutableList<DataSchedule>>()
    val liveDataCalendarShortDescription = MutableLiveData<DataSchedule>()
    val liveDataCalendarFullDescription = MutableLiveData<DataGame>()

    fun getMyData() {
        if (liveDataArrayDate.value == null) {
            Log.d("debugLC", "Данных нет")
            //Сделать запрос
            Log.d("debug", "Начало запроса()")
            coroutine = CoroutineScope(Dispatchers.IO).launch {
                val requestWorkplace = async { clubsRepository.workplaceRequestRetrofit() }.await()
                Log.d("debug", "requestWorkplace:$requestWorkplace")

                withContext(Dispatchers.Main) {
                    val selectedItem = requestWorkplace.first
                    val list = requestWorkplace.second
                    if (list.isNotEmpty()) {
                        liveDataArrayDate.value = requestWorkplace
                        liveDataDate.value =
                            "${list[selectedItem].dateDay} ${list[selectedItem].dateMonth}"
                        liveDataArraySchedule.value = list[selectedItem].listSchedule
                        liveDataDayOff.value = list[selectedItem].listSchedule.isEmpty()
                    }
                }
            }
        } else {
            Log.d("debugLC", "Данные есть")
        }
    }

    fun changeData(dataLong: Long) {
        Log.d("debug", "value from liveData:${liveDataArrayDate.value?.first}")
        val dataString = clubsRepository.convertLongToStringData(dataLong)
        Log.d("debug", "dataString:$dataString")
        //val list = liveDataArrayDate.value?.second
        val pair = liveDataArrayDate.value
        if (pair != null) {
            val position = clubsRepository.checkPositionDate(dataString, pair.second)
            val list = pair.second
            if (list.isNotEmpty()) {
                liveDataArrayDate.value = Pair(position, list)
                liveDataDate.value = "${list[position].dateDay} ${list[position].dateMonth}"
                liveDataArraySchedule.value = list[position].listSchedule
                liveDataDayOff.value = list[position].listSchedule.isEmpty()
            }
        }
    }

    fun setShortDescription(dataSchedule: DataSchedule) {
        Log.d("debug", "1:${liveDataCalendarShortDescription.value ?: "Пусто"}")
        liveDataCalendarShortDescription.value = dataSchedule
        Log.d("debug", "2:${liveDataCalendarShortDescription.value?.name ?: "Пусто"}")
    }

    fun setFullDescription(dataGame: DataGame) {
        Log.d("debug", "1:${liveDataCalendarFullDescription.value ?: "Пусто"}")
        liveDataCalendarFullDescription.value = dataGame
        Log.d("debug", "2:${liveDataCalendarFullDescription.value?.title ?: "Пусто"}")
    }

    fun getDataForShortDescription(): DataSchedule {
        val dataCard = liveDataCalendarShortDescription.value
        return dataCard ?: DataSchedule()
    }
}