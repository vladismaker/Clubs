package com.app.basket.team.playbook

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.basket.team.MainActivity
import com.app.basket.team.adapters.RecyclerViewAdapterDescriptionImage
import com.app.basket.team.adapters.RecyclerViewAdapterDescriptionVideo
import com.app.basket.team.databinding.FragmentPlaybookDescriptionBinding
import com.app.basket.team.dataclasses.DataGameDescription

class PlaybookDescriptionFragment : Fragment() {
    private val viewModel: PlaybookViewModel by activityViewModels()
    private var _binding: FragmentPlaybookDescriptionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaybookDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataPlaybookDataAction.observe(viewLifecycleOwner) {
            Log.d("debug", "Изменился dataSchedule:${it}")
            if (it!=null){
                Log.d("debug", "name:${it.name}")
                Log.d("debug", "title2:${it.images}")
                binding.textPlaybookDescriptionName.text = it.name
                binding.textPlaybookDescriptionDescription.text = it.description
                Log.d("debug", "listImages:${it.images}")
                Log.d("debug", "listVideos:${it.videos}")
                setRecyclerView(it.images)
                setRecyclerViewVideo(it.videos)
            }else{
                Log.d("debug", "Empty. Data:$it")
            }

        }
        binding.backButton.setOnClickListener {
            (activity as MainActivity).navController.popBackStack()
        }
    }

    private fun setRecyclerView(listPlaybook:List<DataGameDescription>){
        val adapter = RecyclerViewAdapterDescriptionImage(listPlaybook)

        binding.recyclerViewPlaybookImages.isNestedScrollingEnabled = false
        binding.recyclerViewPlaybookImages.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaybookImages.adapter = adapter
    }

    private fun setRecyclerViewVideo(listPlaybook:List<DataGameDescription>){
        val adapter = RecyclerViewAdapterDescriptionVideo(listPlaybook,requireContext())

        binding.recyclerViewPlaybookVideos.isNestedScrollingEnabled = false
        binding.recyclerViewPlaybookVideos.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewPlaybookVideos.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}