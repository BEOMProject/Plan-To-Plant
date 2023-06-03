package com.example.plantoplant.navigation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Item(val id: Int, val date: String, val toDo: String, var toDoCompleted: Boolean)

class ToDoViewModel: ViewModel() {
    val itemsListData = MutableLiveData<ArrayList<Item>>()
    val itemClickEvent = MutableLiveData<Int>()
    var checkBoxClickEvent = MutableLiveData<Int>()
    val ids = ArrayList<Int>()
    var itemLongClick = -1
    val items = ArrayList<Item>()
}