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
import com.app.basket.team.R
import com.app.basket.team.adapters.OnItemClickListenerSchedule
import com.app.basket.team.adapters.RecyclerViewAdapterNames
import com.app.basket.team.databinding.FragmentCalendarShortDescriptionBinding
import com.app.basket.team.dataclasses.DataGame
import com.app.basket.team.utils.URL
import com.squareup.picasso.Picasso

class CalendarShortDescriptionFragment : Fragment(), OnItemClickListenerSchedule {
    private var _binding: FragmentCalendarShortDescriptionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by activityViewModels()
    private var arrayDataGameInfo:List<DataGame> = mutableListOf()
    //private val viewModel: SecondFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarShortDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDataCalendarShortDescription.observe(viewLifecycleOwner) {
            Log.d("debug", "Изменился dataSchedule:${it}")
            if (it!=null){
                Log.d("debug", "title1:${it.name}")
                Log.d("debug", "title2:${it.gameInfo[0].title}")
                arrayDataGameInfo = it.gameInfo
                binding.textNameGame.text = it.name

                val listNames:MutableList<String> = mutableListOf()
                for (i in 0 until it.gameInfo.size){
                    listNames.add(it.gameInfo[i].title)
                }

                //setRecyclerView(it.gameInfo)
                setRecyclerView(listNames)
                Log.d("debug", "it.schedule:${it.schedule}")
                val fullLink = URL +it.schedule
                Picasso.get().load(fullLink).into(binding.imageSchedule)
            }else{
                Log.d("debug", "Empty. Data:$it")
            }

        }

        binding.backButton.setOnClickListener {
            (activity as MainActivity).navController.popBackStack()
        }

/*        Log.d("debug", "Выполнился getDataForShortDescription()")
        val data = viewModel.getDataForShortDescription()
        if (data.isNotEmpty()){
            Log.d("debug", "title1:${data[0].title}")
            Log.d("debug", "title2:${data[1].title}")
        }else{
            Log.d("debug", "Empty. Data:$data")
        }*/
    }

    private fun setRecyclerView(list: List<String>) {
        //val recyclerView: RecyclerView = viewMy.findViewById(R.id.recycler_view_schedule)
        val adapterSchedule = RecyclerViewAdapterNames(list, this)
        binding.recyclerViewGameInfo.isNestedScrollingEnabled = false
        binding.recyclerViewGameInfo.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewGameInfo.adapter = adapterSchedule
    }

    override fun onItemClickSchedule(position: Int) {
        Log.d("debug", "Нажатие на name")
        viewModel.setFullDescription(arrayDataGameInfo[position])
        viewModel.liveDataCalendarFullDescription.observe(viewLifecycleOwner) {
            val f= it
            // Переходим на следующий фрагмент только после обновления LiveData
            Log.d("debug", "Значение задали, переходим")
            (activity as MainActivity).navController.navigate(R.id.action_calendarShortDescriptionFragment_to_calendarFullDescriptionFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}