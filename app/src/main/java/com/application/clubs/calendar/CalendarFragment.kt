package com.application.clubs.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.clubs.dataclasses.DataCard
import com.application.clubs.dataclasses.DataSchedule
import com.application.clubs.R
import com.application.clubs.adapters.OnItemClickListener
import com.application.clubs.adapters.RecyclerViewAdapter
import com.application.clubs.adapters.RecyclerViewAdapterSchedule
import com.application.clubs.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    var arrayAllData:MutableList<DataCard> = mutableListOf()

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        viewModel.liveDataDate.observe(viewLifecycleOwner) {
            binding.textViewDate.text = it
        }

        viewModel.liveDataDayOff.observe(viewLifecycleOwner) {
            if (it){
                binding.hint.visibility = View.VISIBLE
            }else{
                binding.hint.visibility = View.GONE
            }
        }

        viewModel.liveDataArrayDate.observe(viewLifecycleOwner) {
            arrayAllData = it
            setRecyclerViewDate(view, it)
        }
        viewModel.liveDataArraySchedule.observe(viewLifecycleOwner) {
            setRecyclerViewSchedule(view, it)
        }

        viewModel.getMyData()
    }

    private fun setRecyclerViewDate(viewMy: View, arrayAllData: MutableList<DataCard>) {

        val recyclerView: RecyclerView = viewMy.findViewById(R.id.recycler_view_date)
        val adapterNewApp = RecyclerViewAdapter(arrayAllData, this)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapterNewApp
    }

    private fun setRecyclerViewSchedule(viewMy: View, arrayAllData: MutableList<DataSchedule>) {
        val recyclerView: RecyclerView = viewMy.findViewById(R.id.recycler_view_schedule)
        val adapterSchedule = RecyclerViewAdapterSchedule(arrayAllData)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapterSchedule
    }

    override fun onItemClick(position: Int) {
        Log.d("debug", "Нажатие")
        viewModel.liveDataDate.value = "${arrayAllData[position].dateDay} ${arrayAllData[position].dateMonth}"
        viewModel.liveDataArraySchedule.value = arrayAllData[position].listSchedule
        if (arrayAllData[position].listSchedule.isNotEmpty()){
            viewModel.liveDataDayOff.value = false
        }else{
            viewModel.liveDataDayOff.value = true
        }
    }



    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}