package com.application.clubs.playbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.clubs.MainActivity
import com.application.clubs.R
import com.application.clubs.adapters.OnItemClickListenerPlaybook
import com.application.clubs.adapters.RecyclerViewAdapterPlaybook
import com.application.clubs.databinding.FragmentPlaybookBinding
import com.application.clubs.dataclasses.DataPlaybook


class PlaybookFragment : Fragment(), OnItemClickListenerPlaybook {
    private var _binding: FragmentPlaybookBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaybookViewModel by activityViewModels()
    private var arrayDataPlaybook:List<DataPlaybook> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaybookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataPlaybook.observe(viewLifecycleOwner) {
            endLoading()
            Log.d("debug", "it:$it")
            Log.d("debug", "it:${it[0].name}")
            binding.textViewPlaybook.text = getString(R.string.text_navigation_playbook)
            arrayDataPlaybook = it
            setRecyclerViewDate(arrayDataPlaybook)
        }

        viewModel.getPlaybookData()
    }

    private fun endLoading(){
        binding.lottieProgressBar.visibility = View.GONE
    }

    private fun setRecyclerViewDate(listPlaybook:List<DataPlaybook>){
        val adapter = RecyclerViewAdapterPlaybook(listPlaybook,this)

        binding.recyclerViewPlaybook.isNestedScrollingEnabled = false
        binding.recyclerViewPlaybook.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewPlaybook.adapter = adapter
    }

    override fun onItemClickPlaybook(positionOne: Int, positionTwo: Int) {
        Log.d("debug", "Нажатие на элемент внутреннего массива$positionOne, $$positionTwo!!!")
        val dataAction = arrayDataPlaybook[positionOne].actions[positionTwo]
        viewModel.setFullDescription(dataAction)
        viewModel.liveDataPlaybookDataAction.observe(viewLifecycleOwner) {
            // Переходим на следующий фрагмент только после обновления LiveData
            Log.d("debug", "Значение задали, переходим")
            (activity as MainActivity).navController.navigate(R.id.action_playbookFragment_to_playbookDescriptionFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}