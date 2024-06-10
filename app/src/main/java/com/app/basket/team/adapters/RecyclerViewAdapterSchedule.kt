package com.app.basket.team.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.basket.team.dataclasses.DataSchedule
import com.app.basket.team.R

class RecyclerViewAdapterSchedule(
    private val allDataList: MutableList<DataSchedule>,
    private val itemClickListenerSchedule: OnItemClickListenerSchedule
    ) :
    RecyclerView.Adapter<RecyclerViewAdapterSchedule.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return allDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_data_schedule, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        if (allDataList.size==1){
            cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_white)
        }else{
            when (position) {
                0 -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_top_white)
                }
                allDataList.size-1 -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_bottom_white)
                }
                else -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_center_white)
                }
            }
        }


        val time: TextView = cardView.findViewById(R.id.time)
        val name: TextView = cardView.findViewById(R.id.name)
        val description: TextView = cardView.findViewById(R.id.description)
        val timeZone: TextView = cardView.findViewById(R.id.time_zone)
        val imageNext: ImageView = cardView.findViewById(R.id.image_next)

        val timeString = allDataList[position].time
        val nameString = allDataList[position].name
        val descriptionString = allDataList[position].description
        val timeZoneString= allDataList[position].timeZone
        //val t = allDataList[position].gameInfo

        time.text = timeString
        name.text = nameString
        description.text = descriptionString
        timeZone.text = timeZoneString

        checkNull(timeString, time)
        checkNull(nameString, name)
        checkNull(descriptionString, description)
        checkNull(timeZoneString, timeZone)

        if (timeZoneString=="Home Time"){
            imageNext.visibility = View.VISIBLE

            cardView.setOnClickListener {
                itemClickListenerSchedule.onItemClickSchedule(position)
            }
        }else{
            imageNext.visibility = View.GONE
        }
    }

    private fun checkNull(valueStr:String, textView: TextView){
        //Log.d("debug", "valueStr:$valueStr")
        if (valueStr==""){
            textView.visibility = View.GONE
        }else{
            textView.visibility = View.VISIBLE
        }
    }
}

interface OnItemClickListenerSchedule {
    fun onItemClickSchedule(position: Int)
}