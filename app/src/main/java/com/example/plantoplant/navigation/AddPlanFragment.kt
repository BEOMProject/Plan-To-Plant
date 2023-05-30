package com.example.plantoplant.navigation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class AddPlanFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var addPlanButton: Button
    private lateinit var userId: String
    private lateinit var planDate: String
    private lateinit var plan: String
    private var addPlanOnCalendar: Boolean = false

    private lateinit var btnDatePicker: Button
    private lateinit var btnAddPlan: Button
    private lateinit var addPlanTextView: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userId = arguments?.getString("email") ?: ""
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_addplan, container, false)
        btnDatePicker = view.findViewById(R.id.SelectDate)
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments?.getString("email") ?: ""
        btnAddPlan = view.findViewById(R.id.addplanButton)
        addPlanTextView = view.findViewById(R.id.editTextToaddplan)

        btnDatePicker = view.findViewById(R.id.SelectDate)
        btnDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        addPlanButton = view.findViewById(R.id.addplanButton)
        addPlanButton.setOnClickListener {
            // userId = arguments?.getString("email") ?: ""
            plan = addPlanTextView.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                addPlanData()
            }
        }
    }

    private fun showDatePickerDialog() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            planDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(myCalendar.time)
            btnDatePicker.text = planDate
        }
        val currentYear = myCalendar.get(Calendar.YEAR)
        val currentMonth = myCalendar.get(Calendar.MONTH)
        val currentDay = myCalendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), datePicker, currentYear, currentMonth, currentDay)
        datePickerDialog.show()
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
        btnDatePicker.text = sdf.format(myCalendar.time)
    }

    private suspend fun addPlanData() {
        try {
            val url = URL("http://125.142.0.210:8080/todos/add")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()

            jsonObject.put("id", userId) // 얘를 id로 바꾸면 서버에서 addPlan 실패 로그가 뜸, user로 하면 URL NotFound가 뜸;;
            jsonObject.put("date", planDate)
            jsonObject.put("toDo", plan)
            jsonObject.put("toDoVisibilityCalendar", addPlanOnCalendar)

            // 서버로 값 전송
            val outStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outStream.write(jsonObject.toString())
            outStream.flush()

            // 서버에서 결과 받기
            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            inputStream.close()

            val response = stringBuilder.toString()

            withContext(Dispatchers.Main) {
                if (response == "1") {
                    // 추가 성공
                    Toast.makeText(requireContext(), "Plan added successfully", Toast.LENGTH_SHORT).show()
                } else if (response == "2") {
                    // 추가 실패
                    Toast.makeText(requireContext(), "Failed to add plan", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}