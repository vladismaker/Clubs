package com.app.basket.team

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel:ViewModel() {

    val liveDataDayOff = MutableLiveData<Boolean>()

    fun changeVisibilityBottomNavigation(value:Boolean){
        liveDataDayOff.value = value
    }
}