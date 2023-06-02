package com.example.plantoplant.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.CalenderAdapter
import com.example.plantoplant.R
import com.example.plantoplant.ToDoMonths
import com.example.plantoplant.util.ServerCon
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalenderFragment : Fragment() {

    lateinit var userId: String

    private lateinit var minusmonth : ImageButton
    private lateinit var plusmonth : ImageButton
    private lateinit var selectedDate: LocalDate //년월   변수
    private lateinit var monthYearText: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_calendar, container, false)
        selectedDate = LocalDate.now() //현재 날짜

        monthYearText = view.findViewById(R.id.monthYearText)
        recyclerView = view.findViewById(R.id.recyclerView)

        setMonthView() //화면 설정
        //이전달 버튼
        minusmonth = view.findViewById(R.id.previousMonth)
        plusmonth = view.findViewById(R.id.nextMonth)

        minusmonth.setOnClickListener{
            selectedDate = selectedDate.minusMonths(1) //현재 월 -1
            setMonthView()
        }
        //다음달 버튼
        plusmonth.setOnClickListener{
            selectedDate = selectedDate.plusMonths(1) //현재 월 +1
            setMonthView()
        }

        return view
    }


    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate) //년월 텍스트뷰
        userId = arguments?.getString("email") ?: "" // 유저 아이디

        val year = selectedDate.year
        val month = selectedDate.month.value
        val selectedDay = selectedDate.dayOfMonth
        var toDoArray = ArrayList<ToDoMonths>()

        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = async { (getCalendarTodo(userId, year, month, selectedDay)) }
            val jsonResult = response.await()
            toDoArray = parseJson(jsonResult)
        }

        runBlocking {
            job.join()
            job.cancel()
        }

        //날짜 생성, 리스트 담기
        val dayList = dayInMonthArray(selectedDate)
        //어댑터 초기화
        val adapter = CalenderAdapter(dayList, toDoArray, requireContext())
        //레이아웃 설정(열 7개)
        val manager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 7)
        //레이아웃 적용
        recyclerView.layoutManager = manager
        //어댑터 적용
        recyclerView.adapter = adapter
    }
    //날짜 타입 설정
    private fun monthYearFromDate(date: LocalDate): String{
        var formatter = DateTimeFormatter.ofPattern("yyyy MM") //월 단위 어떻게 뜰지 MM월 yy년으로 변경 가능

        //받은 날짜 해당 포맷으로 변경
        return date.format(formatter)
    }

    //날짜 생성
    private fun dayInMonthArray(date: LocalDate): ArrayList<String>{
        var dayList = ArrayList<String>()
        var yearMonth = YearMonth.from(date)
        //해당 월 마지막 날짜 가져오기
        var lastDay = yearMonth.lengthOfMonth()
        //해달 월 첫번째 날 가져오기
        var firstDay = selectedDate.withDayOfMonth(1)
        //첫번째 날 요일 가져오기
        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..41){
            if (i <= dayOfWeek || i > (lastDay + dayOfWeek)){
                dayList.add("")
            }else{
                dayList.add((i - dayOfWeek).toString())
            }
        }

        return dayList
    }

    // 한 달치 할 일 목록 가져오기
    private fun getCalendarTodo(userId: String, year: Int, month: Int, selectedDay: Int): String {
        var response = ""

        try {
            val con = ServerCon()
            val url = URL(con.url + "calendar/months")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
            jsonObject.put("year", year)
            jsonObject.put("month", month)
            jsonObject.put("day", selectedDay)
            jsonObject.put("user_id", userId)

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
                stringBuilder.append(line).append('\n')
            }

            response = stringBuilder.toString()

            println(response)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }

    // json 파싱
    private fun parseJson(json: String): ArrayList<ToDoMonths> {
        var result = ArrayList<ToDoMonths>()

        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val tdm: JSONObject = jsonArray.getJSONObject(i)
                val toDoMonths = ToDoMonths(
                    tdm.getInt("id"),
                    LocalDate.parse(tdm.getString("date"), DateTimeFormatter.ISO_DATE),
                    tdm.getString("toDo"),
                    tdm.getBoolean("toDoCompleted")
                )

                result.add(toDoMonths)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return result
    }
}
