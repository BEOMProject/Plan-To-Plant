package com.example.plantoplant.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantoplant.databinding.FragmentTodayBinding
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
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        //var view = LayoutInflater.from(activity).inflate(R.layout.fragment_today, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val binding = FragmentTodayBinding.inflate(inflater, container, false)
        val userId = arguments?.getString("email") ?: ""
        CoroutineScope(Dispatchers.IO).launch {
            val response = makeToDoResponse(userId)
            val jsons = JSONTokener(response).nextValue() as JSONArray
            withContext(Dispatchers.Main) {
                for (i in 0 until jsons.length()) {
                    val id = jsons.getJSONObject(i).getString("id")
                    val date = jsons.getJSONObject(i).getString("date")
                    binding.textView7.text = date.toString()
                    val toDo = jsons.getJSONObject(i).getString("toDo")
                    binding.textView8.text = toDo.toString()
                    val toDoCompleted = jsons.getJSONObject(i).getBoolean("toDoCompleted")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun makeToDoResponse(user_id: String): String {
        var response = ""

        try {
            val url = URL("http://localhost:8080/todos/all?user_id=$user_id")
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