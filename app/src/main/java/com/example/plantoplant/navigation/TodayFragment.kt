package com.example.plantoplant.navigation

import android.annotation.SuppressLint
import android.graphics.Paint
import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentTodayBinding
import com.example.plantoplant.util.ServerCon
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment() {
    private lateinit var viewModel: ToDoViewModel
    private var toDoId: Int = 0
    private lateinit var userId: String
    private lateinit var localDate: LocalDate
    private lateinit var toDo: String
    private var toDoCompleted: Boolean = false

    private lateinit var sprout1: ImageView
    private lateinit var sprout2: ImageView
    private lateinit var sprout3: ImageView
    private lateinit var sprout4: ImageView
    private lateinit var sprout5: ImageView
    private lateinit var plant1: ImageView
    private lateinit var plant2: ImageView
    private lateinit var plant3: ImageView
    private lateinit var plant4: ImageView
    private lateinit var plant5: ImageView

    @SuppressLint("NotifyDataSetChanged", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // setting
        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        val recyclerView = binding.todayRecyclerView
        val view = LayoutInflater.from(activity).inflate(R.layout.item_layout, container, false)
        var textView = view.findViewById<TextView>(R.id.textToDo)
        userId = arguments?.getString("email") ?: ""
        viewModel = ViewModelProvider(this)[ToDoViewModel::class.java]
        val adapter = ToDoCustomAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        sprout1 = binding.todaySprout1
        sprout2 = binding.todaySprout2
        sprout3 = binding.todaySprout3
        sprout4 = binding.todaySprout4
        sprout5 = binding.todaySprout5
        plant1 = binding.todayPlant1
        plant2 = binding.todayPlant2
        plant3 = binding.todayPlant3
        plant4 = binding.todayPlant4
        plant5 = binding.todayPlant5

        // viewModel 데이터 추가를 위한 스레드
        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = makeToDoResponse(userId)
            val jsons = JSONTokener(response).nextValue() as JSONArray
            var countTodo: Int = 0
            var countCompletedTodo: Int = 0

            val currentDate = LocalDate.now() // 현재 날짜

            for (i in 0 until jsons.length()) {
                // 할 일 아이디
                viewModel.ids.add(jsons.getJSONObject(i).getInt("id"))
                toDoId = jsons.getJSONObject(i).getInt("id")
                // 날짜
                val subDate = jsons.getJSONObject(i).getString("date")
                localDate = LocalDate.parse(subDate, DateTimeFormatter.ISO_DATE)
                val date = subDate.split("-") as ArrayList<String>
                date.removeAt(0)
                // 할 일
                toDo = jsons.getJSONObject(i).getString("toDo")
                // 할 일 완료 여부
                toDoCompleted = jsons.getJSONObject(i).getBoolean("toDoCompleted")
                if (toDoCompleted) {
                    textView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                if (localDate == currentDate) { // 오늘 날짜와 같은 할 일인 경우
                    countTodo += 1 // 오늘 날짜와 같은 할 일 목록 개수 증가
                }
                val CompletedValue = jsons.getJSONObject(i).getBoolean("toDoCompleted")
                if (CompletedValue) {
                    if (localDate == currentDate) {
                        countCompletedTodo += 1 // 완료된 할 일 목록 개수 증가
                    }
                }
                viewModel.items.add(Item(toDoId, "${date[0]}-${date[1]}", toDo, toDoCompleted))
            }

            println("할일 목록 : $countTodo")
            when (countTodo) {
                0 -> {
                    sprout1.visibility = View.INVISIBLE
                    sprout2.visibility = View.INVISIBLE
                    sprout3.visibility = View.INVISIBLE
                    sprout4.visibility = View.INVISIBLE
                    sprout5.visibility = View.INVISIBLE
                }
                1 -> {
                    sprout1.visibility = View.VISIBLE
                    sprout2.visibility = View.INVISIBLE
                    sprout3.visibility = View.INVISIBLE
                    sprout4.visibility = View.INVISIBLE
                    sprout5.visibility = View.INVISIBLE
                }
                2 -> {
                    sprout1.visibility = View.VISIBLE
                    sprout2.visibility = View.VISIBLE
                    sprout3.visibility = View.INVISIBLE
                    sprout4.visibility = View.INVISIBLE
                    sprout5.visibility = View.INVISIBLE
                }
                3 -> {
                    sprout1.visibility = View.VISIBLE
                    sprout2.visibility = View.VISIBLE
                    sprout3.visibility = View.VISIBLE
                    sprout4.visibility = View.INVISIBLE
                    sprout5.visibility = View.INVISIBLE
                }
                4 -> {
                    sprout1.visibility = View.VISIBLE
                    sprout2.visibility = View.VISIBLE
                    sprout3.visibility = View.VISIBLE
                    sprout4.visibility = View.VISIBLE
                    sprout5.visibility = View.INVISIBLE
                }
                else -> {
                    sprout1.visibility = View.VISIBLE
                    sprout2.visibility = View.VISIBLE
                    sprout3.visibility = View.VISIBLE
                    sprout4.visibility = View.VISIBLE
                    sprout5.visibility = View.VISIBLE
                }
            }

            println("완료한 일 : $countCompletedTodo")
            when (countCompletedTodo) {
                1 -> {
                    sprout1.visibility = View.INVISIBLE
                    plant1.visibility = View.VISIBLE
                }
                2 -> {
                    sprout1.visibility = View.INVISIBLE
                    plant1.visibility = View.VISIBLE
                    sprout2.visibility = View.INVISIBLE
                    plant2.visibility = View.VISIBLE

                }
                3 -> {
                    sprout1.visibility = View.INVISIBLE
                    plant1.visibility = View.VISIBLE
                    sprout2.visibility = View.INVISIBLE
                    plant2.visibility = View.VISIBLE
                    sprout3.visibility = View.INVISIBLE
                    plant3.visibility = View.VISIBLE
                }
                4 -> {
                    sprout1.visibility = View.INVISIBLE
                    plant1.visibility = View.VISIBLE
                    sprout2.visibility = View.INVISIBLE
                    plant2.visibility = View.VISIBLE
                    sprout3.visibility = View.INVISIBLE
                    plant3.visibility = View.VISIBLE
                    sprout4.visibility = View.INVISIBLE
                    plant4.visibility = View.VISIBLE
                }
                5 -> {
                    sprout1.visibility = View.INVISIBLE
                    plant1.visibility = View.VISIBLE
                    sprout2.visibility = View.INVISIBLE
                    plant2.visibility = View.VISIBLE
                    sprout3.visibility = View.INVISIBLE
                    plant3.visibility = View.VISIBLE
                    sprout4.visibility = View.INVISIBLE
                    plant4.visibility = View.VISIBLE
                    sprout5.visibility = View.INVISIBLE
                    plant5.visibility = View.VISIBLE
                }
            }
            //정렬 코드
            viewModel.items.sortWith(compareBy<Item> { it.toDoCompleted }
                .thenBy { it.date[0].code }
                .thenBy { it.date[1].code }
                .thenBy { it.date[3].code }
                .thenBy { it.date[4].code })
        }

        // 메인 스레드 join
        runBlocking {
            job.join()
            job.cancel()
        }

        viewModel.itemsListData.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.itemClickEvent.observe(viewLifecycleOwner) {
            ItemDialog(it).show(requireActivity().supportFragmentManager, "ItemDialog")
        }

        viewModel.checkBoxClickEvent.observe(viewLifecycleOwner) {
            val idx = viewModel.checkBoxClickEvent.value!!
            val year = LocalDate.now().year
            val itemArray = viewModel.items[idx]
            val date = "$year-${itemArray.date}"
            localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            toDoId = itemArray.id
            toDo = itemArray.toDo
            toDoCompleted = itemArray.toDoCompleted
            CoroutineScope(Dispatchers.IO).launch {
                updateData(toDoId)
            }
            viewModel.items.sortWith(compareBy<Item> { it.toDoCompleted }
                .thenBy { it.date[0].code }
                .thenBy { it.date[1].code }
                .thenBy { it.date[3].code }
                .thenBy { it.date[4].code })
            adapter.notifyDataSetChanged()
        }

        registerForContextMenu(recyclerView)
        return binding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.ctx_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 일정 삭제 구현
            R.id.delete -> {
                val idx = viewModel.itemLongClick
                viewModel.items.removeAt(idx)
                toDoId = viewModel.ids[idx]
                CoroutineScope(Dispatchers.IO).launch {
                    deletePlanData(toDoId)
                }
                viewModel.ids.removeAt(idx)
                viewModel.itemsListData.value = viewModel.items
            }
            R.id.edit -> {
                viewModel.itemClickEvent.value = viewModel.itemLongClick
            }
            else -> return false
        }
        return true
    }

    private fun makeToDoResponse(user_id: String): String {
        var response = ""

        try {
            val con = ServerCon()
            val url = URL(con.url + "todos/all?user_id=$user_id")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/json")
            conn.connect()

            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            response = stringBuilder.toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return response
    }

    private suspend fun deletePlanData(toDoId: Int) {
        try {
            val con = ServerCon()
            val url = URL(con.url + "todos/delete")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()

            jsonObject.put("toDoId", toDoId)

            val outStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outStream.write(jsonObject.toString())
            outStream.flush()

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
                if (response == "1\n") {
                    Toast.makeText(requireContext(), "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(requireContext(), "오류발생!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun updateData(toDoId: Int) {
        try {
            val con = ServerCon()
            val url = URL(con.url + "todos/update")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()

            jsonObject.put("toDoId", toDoId)
            jsonObject.put("userId", userId)
            jsonObject.put("date", localDate)
            jsonObject.put("toDo", toDo)
            jsonObject.put("toDoVisibilityCalendar", false)
            jsonObject.put("toDoCompleted", toDoCompleted)

            val outStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outStream.write(jsonObject.toString())
            outStream.flush()

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
                if (response != "1\n") {
                    Toast.makeText(requireContext(), "오류 발생!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}