package com.example.plantoplant.navigation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plantoplant.R
import java.text.SimpleDateFormat
import java.util.*

class AddPlanFragment : Fragment() {

    private lateinit var tvDatePicker: TextView
    private lateinit var btnDatePicker: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_addplan, container, false)

        tvDatePicker = view.findViewById(R.id.adddatetextView)
        btnDatePicker = view.findViewById(R.id.SelectDate)

        val mycalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            mycalendar.set(Calendar.YEAR, year)
            mycalendar.set(Calendar.MONTH, month)
            mycalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLabel(mycalendar)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                mycalendar.get(Calendar.YEAR),
                mycalendar.get(Calendar.MONTH),
                mycalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        return view
    }

    private fun updateLabel(myCalendar : Calendar){
        val myformat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myformat,Locale.KOREA)
        tvDatePicker.setText(sdf.format(myCalendar.time))
    }
}