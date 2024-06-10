package com.app.basket.team.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.basket.team.MainActivity
import com.app.basket.team.adapters.RecyclerViewAdapterDescriptionImage
import com.app.basket.team.adapters.RecyclerViewAdapterDescriptionVideo
import com.app.basket.team.databinding.FragmentCalendarFullDescriptionBinding
import com.app.basket.team.dataclasses.DataGameDescription

class CalendarFullDescriptionFragment : Fragment() {
    private var _binding: FragmentCalendarFullDescriptionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarFullDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataCalendarFullDescription.observe(viewLifecycleOwner) {
            Log.d("debug", "Изменился dataSchedule:${it}")
            if (it!=null){
                Log.d("debug", "title1:${it.title}")
                Log.d("debug", "title2:${it.data[0].link}")
                Log.d("debug", "title2:${it.data[0].description}")
                binding.textTitle.text = it.title

                setRecyclerView(it.data)
            }else{
                Log.d("debug", "Empty. Data:$it")
            }

        }

        binding.backButton.setOnClickListener {
            (activity as MainActivity).navController.popBackStack()
        }
    }

    private fun isVideo(mediaUrl: String): Boolean {
        val fileExtension = mediaUrl.substringAfterLast('.').lowercase()
        return fileExtension=="mp4" || fileExtension=="mov"
    }

    private fun setRecyclerView(list: List<DataGameDescription>) {
        val adapter = if (isVideo(list[0].link)){
            RecyclerViewAdapterDescriptionVideo(list, requireContext())
        }else{
            RecyclerViewAdapterDescriptionImage(list)
        }

        binding.recyclerViewDataGame.isNestedScrollingEnabled = false
        binding.recyclerViewDataGame.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewDataGame.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}