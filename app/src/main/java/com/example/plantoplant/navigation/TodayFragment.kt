package com.example.plantoplant.navigation

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.navigation.ToDoViewModel
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

class TodayFragment : Fragment() {
    //private val viewModel by viewModels<ToDoViewModel>()
    private lateinit var viewModel: ToDoViewModel
    private var toDoId: Int = 0
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // setting
        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        val recyclerView = binding.todayRecyclerView
        val userId = arguments?.getString("email") ?: ""
        viewModel = ViewModelProvider(this)[ToDoViewModel::class.java]
        val adapter = ToDoCustomAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        // viewModel 데이터 추가를 위한 스레드
        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = makeToDoResponse(userId)
            val jsons = JSONTokener(response).nextValue() as JSONArray
            for (i in 0 until jsons.length()) {
                viewModel.ids.add(jsons.getJSONObject(i).getInt("id"))
                val date = jsons.getJSONObject(i).getString("date").split("-") as ArrayList<String>
                date.removeAt(0)
                val toDo = jsons.getJSONObject(i).getString("toDo")
                viewModel.items.add(Item("${date[0]}-${date[1]}", toDo))
            }
            //정렬 코드
            viewModel.items.sortWith(compareBy<Item> { it.date[0].code }
                .thenBy { it.date[1].code }
                .thenBy { it.date[3].code }
                .thenBy { it.date[4].code })
        }
        // 메인 스레드 join
        runBlocking {
            job.join()
            job.cancel()
        }

        viewModel.itemsListData.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }

        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            ItemDialog(it).show(requireActivity().supportFragmentManager, "ItemDialog")
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
        when(item.itemId){
            // 일정 삭제 구현
            R.id.delete -> {
                val idx = viewModel.itemLongClick
                viewModel.items.removeAt(idx)
                toDoId = viewModel.ids[idx]
                CoroutineScope(Dispatchers.IO).launch {
                    deletePlanData()
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
        }
        catch (e: MalformedURLException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        } catch(e: FileNotFoundException){
            e.printStackTrace()
        }
        return response
    }

    private suspend fun deletePlanData(){
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

            withContext(Dispatchers.Main){
                if(response == "1\n") {
                    Toast.makeText(requireContext(), "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(requireContext(), "오류발생!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}