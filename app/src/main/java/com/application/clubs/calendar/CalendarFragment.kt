package com.application.clubs.calendar

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.clubs.MainActivity
import com.application.clubs.MainActivityViewModel
import com.application.clubs.dataclasses.DataCard
import com.application.clubs.dataclasses.DataSchedule
import com.application.clubs.R
import com.application.clubs.adapters.OnItemClickListener
import com.application.clubs.adapters.OnItemClickListenerSchedule
import com.application.clubs.adapters.RecyclerViewAdapterDate
import com.application.clubs.adapters.RecyclerViewAdapterSchedule
import com.application.clubs.databinding.FragmentCalendarBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(), OnItemClickListener, OnItemClickListenerSchedule {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private var arrayAllData:MutableList<DataCard> = mutableListOf()
    private var arrayDataSchedule:MutableList<DataSchedule> = mutableListOf()

    //private lateinit var viewModel: CalendarViewModel
    private val viewModel: CalendarViewModel by activityViewModels()
    private val viewModelMainActivity: MainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("debugLC", "onCreateView")
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("debugLC", "onViewCreated")
        //viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        viewModelMainActivity.changeVisibilityBottomNavigation(true)

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
            endLoading()
            arrayAllData = it.second
            setRecyclerViewDate(view, it.second, it.first)
        }
        viewModel.liveDataArraySchedule.observe(viewLifecycleOwner) {
            arrayDataSchedule = it
            setRecyclerViewSchedule(view, arrayDataSchedule)
        }

        viewModel.getMyData()

        binding.calendarButton.setOnClickListener{
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTheme(R.style.MyDatePickerDialogTheme)
                    .build()
            // Установка слушателя выбора даты
            datePicker.addOnPositiveButtonClickListener { selection ->
                viewModel.changeData(selection)
            }

            // Показ календаря
            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")

        }
    }

    private fun endLoading(){
        binding.lottieProgressBar.visibility = View.GONE
        binding.calendarButton.visibility = View.VISIBLE
        binding.recyclerViewSchedule.visibility = View.VISIBLE
    }

    private fun setRecyclerViewDate(viewMy: View, arrayAllData: MutableList<DataCard>, startPosition: Int) {

        val recyclerView: RecyclerView = viewMy.findViewById(R.id.recycler_view_date)
        val adapterNewApp = RecyclerViewAdapterDate(arrayAllData, this, startPosition)
        recyclerView.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        layoutManager.scrollToPositionWithOffset(startPosition, 80)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapterNewApp
    }

    private fun setRecyclerViewSchedule(viewMy: View, arrayAllData: MutableList<DataSchedule>) {
        val recyclerView: RecyclerView = viewMy.findViewById(R.id.recycler_view_schedule)
        val adapterSchedule = RecyclerViewAdapterSchedule(arrayAllData, this)
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

    override fun onItemClickSchedule(position: Int) {
        Log.d("debug", "Нажатие на элемент списка")
        viewModel.setShortDescription(arrayDataSchedule[position])
        viewModel.liveDataCalendarShortDescription.observe(viewLifecycleOwner) {
            // Переходим на следующий фрагмент только после обновления LiveData
            Log.d("debug", "Значение задали, переходим")
            (activity as MainActivity).navController.navigate(R.id.action_calendarFragment_to_calendarShortDescriptionFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("debugLC", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debugLC", "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("debugLC", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("debugLC", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("debugLC", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("debugLC", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("debugLC", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("debugLC", "onDestroy")
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()

        Log.d("debugLC", "onDetach")
    }
}