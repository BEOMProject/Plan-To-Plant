package com.example.plantoplant.navigation

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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalenderFragment : Fragment() {

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
        //날짜 생성, 리스트 담기
        val dayList = dayInMonthArray(selectedDate)
        //어댑터 초기화
        val adapter = CalenderAdapter(dayList, requireContext())
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

}
