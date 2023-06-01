package com.example.plantoplant.navigation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantoplant.util.ServerCon
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
}