package com.example.plantoplant.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentTodayBinding
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

class ToDoCustomAdapter(private val viewModel: ToDoViewModel): RecyclerView.Adapter<ToDoCustomAdapter.ViewHolder>(){
    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun setContents(pos: Int){
            val textView1 = view.findViewById<TextView>(R.id.textDate)
            val textView2 = view.findViewById<TextView>(R.id.textToDo)

            with(viewModel.items[pos]){
                textView1.text = date
                textView2.text = toDo
            }
        }
    }

    override fun getItemCount() = viewModel.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_layout, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener{
            viewModel.itemClickEvent.value = viewHolder.adapterPosition
        }
        view.setOnLongClickListener {
            viewModel.itemLongClick = viewHolder.adapterPosition
            false
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }
}