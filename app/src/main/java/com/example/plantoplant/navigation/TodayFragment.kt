package com.example.plantoplant.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentTodayBinding
import com.example.plantoplant.util.ServerCon
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class TodayFragment : Fragment() {
    //private val viewModel by viewModels<ToDoViewModel>()
    private lateinit var viewModel: ToDoViewModel
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // setting
        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
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
                val date = jsons.getJSONObject(i).getString("date").split("-")
                val toDo = jsons.getJSONObject(i).getString("toDo")
                viewModel.items.add(Item("${date[1]}-${date[2]}", toDo))
            }
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
            R.id.delete -> viewModel.deleteItem(viewModel.itemLongClick)
            R.id.edit -> viewModel.itemClickEvent.value = viewModel.itemLongClick
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
}