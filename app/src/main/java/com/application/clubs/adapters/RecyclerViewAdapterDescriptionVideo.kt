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
import com.application.clubs.R
import com.application.clubs.dataclasses.DataGameDescription
import com.application.clubs.utils.URL
import com.squareup.picasso.Picasso

class RecyclerViewAdapterDescriptionVideo(
    private val listDescription: List<DataGameDescription>,
    private val context: Context
    ) :
    RecyclerView.Adapter<RecyclerViewAdapterDescriptionVideo.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listDescription.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.card_data_description_video, parent, false) as LinearLayout
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val description: TextView = cardView.findViewById(R.id.text_view_description)
        val playerView: PlayerView = cardView.findViewById(R.id.player_view)

        val descriptionString = listDescription[position].description
        description.text = descriptionString

        val linkString = listDescription[position].link
        val fullLink = URL +linkString

        Log.d("debug", "Открываем видео\n$fullLink")

        playVideo(playerView, fullLink)

        checkNull(descriptionString, description)
    }

    private fun playVideo(playerView: PlayerView, fullLink:String){
        val player = ExoPlayer.Builder(context).build()
        playerView.player = player
        //val fullLink = "http://teammanager.dealko.org/videos/test.mp4"
        val mediaItem = MediaItem.fromUri(fullLink)
        player.setMediaItem(mediaItem)

        player.prepare()
        player.playWhenReady = false
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
