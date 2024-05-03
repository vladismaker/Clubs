package com.application.clubs.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.clubs.R
import com.application.clubs.dataclasses.DataGameDescription
import com.application.clubs.dataclasses.DataPlaybook
import com.application.clubs.utils.URL
import com.squareup.picasso.Picasso

class RecyclerViewAdapterPlaybook(
    private val listDescription: List<DataPlaybook>,
    private val itemClickListenerPlaybook: OnItemClickListenerPlaybook
    ) :
    RecyclerView.Adapter<RecyclerViewAdapterPlaybook.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listDescription.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_playbook, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val name: TextView = cardView.findViewById(R.id.text_view_name)
        val recyclerView: RecyclerView = cardView.findViewById(R.id.recycler_view_playbook)

        val listNames:MutableList<String> = mutableListOf()

        for (element in listDescription[position].actions){
            listNames.add(element.name)
        }

        val adapterSchedule = RecyclerViewAdapterNamesPlaybook(listNames, itemClickListenerPlaybook, position)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapterSchedule

        val nameString = listDescription[position].name
        name.text = nameString

        checkNull(nameString, name)
    }

    private fun checkNull(valueStr:String, textView: TextView){
        //Log.d("debug", "valueStr:$valueStr")
        if (valueStr==""){
            textView.visibility = View.GONE
        }else{
            textView.visibility = View.VISIBLE
        }
    }

/*    override fun onItemClickSchedule(position: Int) {
        Log.d("debug", "Нажатие на элемент внутреннего массива")
    }*/
}
interface OnItemClickListenerPlaybook {
    fun onItemClickPlaybook(positionOne: Int, positionTwo: Int)
}
