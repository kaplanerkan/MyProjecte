package com.kiosk.mysimpletodo.ui

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.kiosk.mysimpletodo.model.Todo

import com.google.android.material.timepicker.MaterialTimePicker
import com.kiosk.mysimpletodo.R
import com.kiosk.mysimpletodo.databinding.FragmentAddTodoBinding
import com.kiosk.mysimpletodo.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddTodoFragment : Fragment(R.layout.fragment_add_todo) {

    private lateinit var binding: FragmentAddTodoBinding
    private var spinnerItem = arrayOf<String>()
    private var actionBar: ActionBar? = null
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val args by navArgs<AddTodoFragmentArgs>()
    private var existTodo: Todo? = null
    private lateinit var timePicker: MaterialTimePicker
    private var alarmStartTime: Calendar? = null
    private lateinit var alarmManager: AlarmManagerCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAddTodoBinding.bind(view)

        setHasOptionsMenu(true)
        setupSpinner()
//        setupScheduleTimePicker()

        existTodo = args.todo
        existTodo?.let {
            (activity as AppCompatActivity).supportActionBar?.title = "Edit To-do"
            binding.etTitle.setText(it.title)
            binding.etDescription.setText(it.description)
            binding.spinnerPriority.setSelection(it.priority + 1)
        }
    }

    private fun setupSpinner() {
        spinnerItem = resources.getStringArray(R.array.choice_priority)

        val spinnerAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerItem
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                }
                return tv
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = spinnerAdapter
    }

//    private fun setupScheduleTimePicker() {
//        binding.switchScheduleTime.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked) {
//                timePicker.show(childFragmentManager, "timepicker")
//            } else {
//                binding.tvScheduleTime.text = null
//            }
//        }
//
//        timePicker = MaterialTimePicker.Builder()
//            .setTimeFormat(TimeFormat.CLOCK_12H)
//            .setTitleText("Select Schedule Time")
//            .build()
//
//        timePicker.addOnPositiveButtonClickListener {
//            alarmStartTime = Calendar.getInstance().apply {
//                set(Calendar.HOUR_OF_DAY, timePicker.hour)
//                set(Calendar.MINUTE, timePicker.minute)
//                set(Calendar.SECOND, 0)
//            }
//            val sdf = SimpleDateFormat("h:mm a")
//            binding.tvScheduleTime.text = sdf.format(alarmStartTime?.time)
//        }
//
//        timePicker.addOnCancelListener {
//            binding.switchScheduleTime.isChecked = false
//        }
//
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_todo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_save -> {
                val newTodo = if(existTodo != null) {
                    Todo(
                        id = existTodo!!.id,
                        title = binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        priority = binding.spinnerPriority.selectedItemPosition - 1,
                        createdAt = Calendar.getInstance().time
                    )
                } else {
                    Todo(
                        title = binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        priority = binding.spinnerPriority.selectedItemPosition - 1,
                        createdAt = Calendar.getInstance().time
                    )
                }



                sharedViewModel.onSaveNewTodo(newTodo)
                    .observe(viewLifecycleOwner, { isSuccessSaved ->
                        if(isSuccessSaved) {
                            findNavController().navigateUp()
                        } else {
                            "Failed to insert new todo!".showToast(requireContext())
                        }
                    })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}