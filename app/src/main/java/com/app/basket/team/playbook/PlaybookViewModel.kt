package com.app.basket.team.playbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.basket.team.RepositoryPlaybookImpl
import com.app.basket.team.dataclasses.DataAction
import com.app.basket.team.dataclasses.DataPlaybook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaybookViewModel : ViewModel() {
    private var coroutine: Job? = null
    private val clubsPlaybookRepository = RepositoryPlaybookImpl.getInstance()

/*    val liveDataDate = MutableLiveData<String>()
    val liveDataDayOff = MutableLiveData<Boolean>()
    var liveDataArrayDate = MutableLiveData<Pair<Int, MutableList<DataCard>>>()
    val liveDataArraySchedule = MutableLiveData<MutableList<DataSchedule>>()
    val liveDataCalendarShortDescription = MutableLiveData<DataSchedule>()
    val liveDataCalendarFullDescription = MutableLiveData<DataGame>()*/


    val liveDataPlaybook = MutableLiveData<List<DataPlaybook>>()
    val liveDataPlaybookDataAction = MutableLiveData<DataAction>()

    fun getPlaybookData() {
        if (liveDataPlaybook.value == null) {
            Log.d("debugLC", "Данных Playbook нет")
            //Сделать запрос
            Log.d("debug", "Начало запроса()")
            coroutine = CoroutineScope(Dispatchers.IO).launch {
                val requestPlaybooks = async { clubsPlaybookRepository.playbookRequestRetrofit() }.await()
                Log.d("debug", "requestPlaybooks:$requestPlaybooks")

                withContext(Dispatchers.Main) {
                    liveDataPlaybook.value = requestPlaybooks
                }
            }
        } else {
            Log.d("debugLC", "Данные есть")
        }
    }

    fun setFullDescription(dataAction: DataAction) {
        Log.d("debug", "1:${liveDataPlaybookDataAction.value ?: "Пусто"}")
        liveDataPlaybookDataAction.value = dataAction
        Log.d("debug", "2:${liveDataPlaybookDataAction.value?.name ?: "Пусто"}")
    }

    /*fun changeData(dataLong: Long) {
        Log.d("debug", "value from liveData:${liveDataArrayDate.value?.first}")
        val dataString = clubsPlaybookRepository.convertLongToStringData(dataLong)
        Log.d("debug", "dataString:$dataString")
        //val list = liveDataArrayDate.value?.second
        val pair = liveDataArrayDate.value
        if (pair != null) {
            val position = clubsPlaybookRepository.checkPositionDate(dataString, pair.second)
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
        return dataCard ?:DataSchedule()
    }*/
}