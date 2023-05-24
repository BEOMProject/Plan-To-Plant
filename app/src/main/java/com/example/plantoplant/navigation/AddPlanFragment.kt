package com.example.plantoplant.navigation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plantoplant.R
import kotlinx.coroutines.*
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
    private lateinit var addPlanButton: Button
    private lateinit var userId: String
    private lateinit var planDate: String
    private lateinit var plan: String
    private var addPlanOnCalender: Boolean = false

    private lateinit var btnDatePicker: Button
    private lateinit var btnAddPlan: Button
    private lateinit var addPlanTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_addplan, container, false)
        btnDatePicker = view.findViewById(R.id.SelectDate)

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
        btnAddPlan = view.findViewById(R.id.addplanButton)
        addPlanTextView = view.findViewById(R.id.editTextToaddplan)

        addPlanButton = view.findViewById(R.id.addplanButton)
        addPlanButton.setOnClickListener {
            // 로그아웃 처리
            addPlanData()
        }
    }

    private fun updateLabel(myCalendar : Calendar){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat,Locale.KOREA)
        btnDatePicker.text = sdf.format(myCalendar.time)
    }
    private fun addPlanData():String {
        //userId = userName.text.toString()
        //planDate = myFormat.text.toString()
        //plan = passwordEdit.text.toString()
        //addPlanOnCalender = passwordReEdit.text.toString()

        var response = ""
        try {
            val url = URL("http://223.194.130.163:8080/todos/add")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
            jsonObject.put("id", userId)
            jsonObject.put("date", planDate)
            jsonObject.put("toDo", plan)
            jsonObject.put("toDoVisibilityCalendar", addPlanOnCalender)

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

            response = stringBuilder.toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }
}