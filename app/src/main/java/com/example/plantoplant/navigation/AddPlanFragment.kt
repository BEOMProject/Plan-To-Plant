package com.example.plantoplant.navigation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plantoplant.R
import java.text.SimpleDateFormat
import java.util.*

class AddPlanFragment : Fragment() {

    private lateinit var btnDatePicker: Button
    private lateinit var btnAddPlan: Button
    private lateinit var addPlanTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_addplan, container, false)
        btnDatePicker = view.findViewById(R.id.SelectDate)
        btnAddPlan = view.findViewById(R.id.addplanButton)
        addPlanTextView = view.findViewById(R.id.editTextToaddplan)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLabel(myCalendar)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        btnAddPlan.setOnClickListener{
            val newText = addPlanTextView.text.toString()

            val textToShow = "$selectedDate: $newText"
            addPlanTextView.text = textToShow

            if (imageCounter < 5) {
                addPlanImageView.visibility = View.VISIBLE
                imageCounter++
            } else {
                addPlanImageView.visibility = View.GONE
            }
        }
            val AddPlanFragment = parentFragmentManager.findFragmentById(R.id.addPlanContainer) as TodayFragment
            AddPlanFragment.addText(newText)
        }


        return view
    }

    private fun updateLabel(myCalendar : Calendar){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat,Locale.KOREA)
        btnDatePicker.text = sdf.format(myCalendar.time)
    }
}