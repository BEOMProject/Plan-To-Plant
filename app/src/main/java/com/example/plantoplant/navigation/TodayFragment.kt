package com.example.plantoplant.navigation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantoplant.navigation.ToDoViewModel
import com.example.plantoplant.R
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
    //private val viewModel by viewModels<ToDoViewModel>()
    private lateinit var viewModel: ToDoViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        val recyclerView = binding.recyclerView
        val userId = arguments?.getString("email") ?: ""
        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        viewModel.addItem(Item("2023-05-24", "England"))

        val adapter = ToDoCustomAdapter(viewModel)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        viewModel.itemsListData.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }

        viewModel.itemClickEvent.observe(viewLifecycleOwner){
            ItemDialog(it).show(requireActivity().supportFragmentManager, "ItemDialog")
        }
        registerForContextMenu(recyclerView)

        Log.d("items", "${viewModel.items.size}")
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
}