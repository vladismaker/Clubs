package com.application.clubs.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.clubs.R
import com.application.clubs.dataclasses.DataGameDescription
import com.application.clubs.utils.URL
import com.squareup.picasso.Picasso

class RecyclerViewAdapterDescriptionImage(
    private val listDescription: List<DataGameDescription>
    ) :
    RecyclerView.Adapter<RecyclerViewAdapterDescriptionImage.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listDescription.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_data_description, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val description: TextView = cardView.findViewById(R.id.text_view_description)
        val image: ImageView = cardView.findViewById(R.id.image_description)

        val descriptionString = listDescription[position].description
        description.text = descriptionString

        val linkString = listDescription[position].link
        val fullLink = URL +linkString

        Picasso.get().load(fullLink).into(image)

        Log.d("debug", "linkString:$linkString")

        checkNull(descriptionString, description, position)
    }

    private fun checkNull(valueStr:String, textView: TextView, position: Int){
        //Log.d("debug", "valueStr:$valueStr")
        if (valueStr==""){
            val numberString = "${position+1}"
            textView.text = numberString
        }
    }
}
