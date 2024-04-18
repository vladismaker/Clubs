package com.application.clubs.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.clubs.dataclasses.DataCard
import com.application.clubs.dataclasses.DataSchedule
import com.application.clubs.RepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {
    private var coroutine: Job? = null
    private val clubsRepository = RepositoryImp.getInstance()

    val liveDataDate = MutableLiveData<String>()
    val liveDataDayOff = MutableLiveData<Boolean>()
    val liveDataArrayDate = MutableLiveData<MutableList<DataCard>>()
    val liveDataArraySchedule = MutableLiveData<MutableList<DataSchedule>>()

    fun getMyData() {
        //Сделать запрос
        Log.d("debug", "Начало запроса()")
        coroutine = CoroutineScope(Dispatchers.IO).launch {
            val requestWorkplace = async { clubsRepository.workplaceRequestRetrofit() }.await()
            Log.d("debug", "requestWorkplace:$requestWorkplace")

            withContext(Dispatchers.Main){
                val selectedItem = 0
                if (requestWorkplace.isNotEmpty()) {
                    liveDataArrayDate.value = requestWorkplace
                    liveDataDate.value = "${requestWorkplace[selectedItem].dateDay} ${requestWorkplace[selectedItem].dateMonth}"
                    liveDataArraySchedule.value = requestWorkplace[selectedItem].listSchedule
                    liveDataDayOff.value = requestWorkplace[selectedItem].listSchedule.isEmpty()
                }
            }
        }
    }
}