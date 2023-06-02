package com.example.plantoplant.navigation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Item(val date: String, val toDo: String)

class ToDoViewModel: ViewModel() {
    val itemsListData = MutableLiveData<ArrayList<Item>>()
    val itemClickEvent = MutableLiveData<Int>()
    val ids = ArrayList<Int>()
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