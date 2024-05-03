package com.application.clubs.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.clubs.dataclasses.DataCard
import com.application.clubs.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecyclerViewAdapterDate(
    private val allDataList: MutableList<DataCard>,
    private val itemClickListener: OnItemClickListener,
    pos: Int
) :
    RecyclerView.Adapter<RecyclerViewAdapterDate.ViewHolder>() {
    private var selectedItem = pos
    private lateinit var contextAdapter: Context


    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return allDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_data, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val dateMonth: TextView = cardView.findViewById(R.id.date_month)
        val dateDay: TextView = cardView.findViewById(R.id.date_day)
        val dateDayOfWeek: TextView = cardView.findViewById(R.id.date_day_of_week)
        val dateStatus: ImageView = cardView.findViewById(R.id.status)

        dateMonth.text = allDataList[position].dateMonth
        dateDay.text = allDataList[position].dateDay
        dateDayOfWeek.text = allDataList[position].dateDayOfWeek

        when(allDataList[position].status){
            "training" -> {
                dateStatus.setImageResource(R.color.grey)
            }
            "game"->{
                dateStatus.setImageResource(R.color.red)
            }
            else->{
                dateStatus.setImageResource(R.color.transparent)
            }
        }

        // Устанавливаем обработчик нажатия
        cardView.setOnClickListener {

            val previousSelectedItem = selectedItem
            selectedItem = holder.adapterPosition
            // Обновляем вид предыдущей выбранной карточки, если она существует
            if (previousSelectedItem != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedItem)
            }
            // Обновляем вид текущей выбранной карточки
            notifyItemChanged(selectedItem)
            Log.d("debug", "${allDataList[position].dateDay} ${allDataList[position].dateMonth}")
            itemClickListener.onItemClick(position)

        }
        val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
        // Устанавливаем состояние выделения карточки
        if (position == selectedItem) {
            cardView.setBackgroundResource(R.drawable.back_for_cards_date_selected)
            layoutParams.height =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.selected_card_height)
            layoutParams.width =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.selected_card_width)
            val topMarginInPixels =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.vertical_space_height_null)
            layoutParams.topMargin = topMarginInPixels
        } else {
            cardView.setBackgroundResource(R.drawable.back_for_cards_date_unselected)
            layoutParams.height =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.default_card_height)
            layoutParams.width =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.default_card_width)
            val topMarginInPixels =
                contextAdapter.resources.getDimensionPixelSize(R.dimen.vertical_space_height)
            layoutParams.topMargin = topMarginInPixels

        }
        cardView.layoutParams = layoutParams
    }
}

fun getDataNow():String{
    val formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Получение текущей даты
    val currentDate = LocalDate.now()

    // Форматирование даты в строку
    return currentDate.format(formatterTime)
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}