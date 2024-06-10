package com.app.basket.team.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.basket.team.R

class RecyclerViewAdapterNames(
    private val listGameInfo: List<String>,
    private val itemClickListenerSchedule: OnItemClickListenerSchedule
    ) :
    RecyclerView.Adapter<RecyclerViewAdapterNames.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listGameInfo.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_data_game_info, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        if (listGameInfo.size==1){
            cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_white)
        }else{
            when (position) {
                0 -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_top_white)
                }
                listGameInfo.size-1 -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_bottom_white)
                }
                else -> {
                    cardView.setBackgroundResource(R.drawable.back_for_cards_schedule_center_white)
                }
            }
        }

        val name: TextView = cardView.findViewById(R.id.name)
        val nameString = listGameInfo[position]
        name.text = nameString

        checkNull(nameString, name)

        cardView.setOnClickListener {
            itemClickListenerSchedule.onItemClickSchedule(position)
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
