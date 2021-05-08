package com.droidgeeks.groceryapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.droidgeeks.groceryapp.R
import com.droidgeeks.groceryapp.databinding.GroceryItemBinding
import com.droidgeeks.groceryapp.room.tables.GroceryTable


class GroceryAdapter(
    var groceryList: List<GroceryTable>,
    var callingID: String,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<GroceryAdapter.GroceryHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroceryHolder {
        val noteItemBinding: GroceryItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.grocery_item,
            parent,
            false
        )
        return GroceryHolder(noteItemBinding)
    }

    override fun onBindViewHolder(
        holder: GroceryHolder,
        position: Int
    ) {
        if (callingID == "home") {
            if (groceryList[position].complete || groceryList[position].home_delete) {
                holder.binding.cvParent.visibility = View.GONE
            } else {
                holder.binding.cvParent.visibility = View.VISIBLE
            }
        } else {
            if (groceryList[position].complete || groceryList[position].home_delete) {
                holder.binding.cvParent.visibility = View.VISIBLE
            } else {
                holder.binding.cvParent.visibility = View.GONE
            }
        }

        holder.binding.grocery = groceryList[position]

        holder.binding.rlParent.setOnClickListener(View.OnClickListener {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener!!.onItemClick(groceryList[position])
            }
        })
    }

    override fun getItemCount(): Int {
        return groceryList.size
    }

    inner class GroceryHolder(itemView: GroceryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: GroceryItemBinding = itemView

    }

    interface OnItemClickListener {
        fun onItemClick(note: GroceryTable)
    }

}
