package com.application.clubs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.clubs.dataclasses.DataSchedule
import com.application.clubs.R

class RecyclerViewAdapterSchedule(private val allDataList: MutableList<DataSchedule>) :
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

        val time: TextView = cardView.findViewById(R.id.time)
        val name: TextView = cardView.findViewById(R.id.name)
        val description: TextView = cardView.findViewById(R.id.description)
        val timeZone: TextView = cardView.findViewById(R.id.time_zone)

        time.text = allDataList[position].time
        name.text = allDataList[position].name
        description.text = allDataList[position].description
        timeZone.text = allDataList[position].timeZone
    }
}