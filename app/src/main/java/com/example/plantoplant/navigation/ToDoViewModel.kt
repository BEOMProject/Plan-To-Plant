package com.example.plantoplant.navigation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

data class Item(val date: String, val toDo: String)

class ToDoViewModel: ViewModel() {
    val itemsListData = MutableLiveData<ArrayList<Item>>()
    val itemClickEvent = MutableLiveData<Int>()
    var itemLongClick = -1
    val items = ArrayList<Item>()

    init{
        CoroutineScope(Dispatchers.IO).launch {
            val response = makeToDoResponse("gicheol")
            val jsons = JSONTokener(response).nextValue() as JSONArray
            withContext(Dispatchers.Main) {
                for (i in 0 until jsons.length()) {
                    val date = jsons.getJSONObject(i).getString("date").split("-")
                    val toDo = jsons.getJSONObject(i).getString("toDo")
                    Log.d("ToDo", "${date[1]}-${date[2]}: $toDo")
                    addItem(Item("${date[1]}-${date[2]}", "$toDo"))
                    Log.d("items", "${items.size}")
                }
            }
        }
    }

    fun addItem(item: Item){
        items.add(item)
        itemsListData.value = items
    }

    fun updateItem(pos: Int, item: Item){
        items[pos] = item
        itemsListData.value = items
    }

    fun deleteItem(pos:Int){
        items.removeAt(pos)
        itemsListData.value = items
    }

    private fun makeToDoResponse(user_id: String): String {
        var response = ""

        try {
            val url = URL("http://192.168.200.173:8080/todos/all?user_id=$user_id")
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