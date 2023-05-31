package com.example.plantoplant.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.example.plantoplant.navigation.ToDoViewModel
import com.example.plantoplant.R
import com.example.plantoplant.databinding.ItemDialogLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ItemDialog(private val itemPos: Int = -1): BottomSheetDialogFragment() {
    private val viewModel by activityViewModels<ToDoViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextDate = view.findViewById<EditText>(R.id.editDate)
        val editTextToDo = view.findViewById<EditText>(R.id.editToDo)
        val btn = view.findViewById<Button>(R.id.buttonOK)

        if(itemPos >= 0){
            with(viewModel.items[itemPos]){
                editTextDate.setText(date)
                editTextToDo.setText(toDo)
            }
        }

        btn.setOnClickListener{
            val item = Item(editTextDate.text.toString(),
                editTextToDo.text.toString())
            if(itemPos < 0) viewModel.addItem(item)
            else viewModel.updateItem(itemPos, item)
            dismiss()
        }
    }
}