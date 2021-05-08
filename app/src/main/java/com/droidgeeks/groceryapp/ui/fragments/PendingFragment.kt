package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidgeeks.groceryapp.databinding.FragmentPendingBinding
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.ui.GroceryAdapter
import com.droidgeeks.groceryapp.view_model.HomeViewModel
import kotlinx.android.synthetic.main.fragment_all_groceries.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PendingFragment : Fragment(), CoroutineScope, GroceryAdapter.OnItemClickListener {


    lateinit var binding: FragmentPendingBinding

    lateinit var navGraph: NavController

    lateinit var home_viewmodel: HomeViewModel
    lateinit var groceryAdapter: GroceryAdapter

    val compositeJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    var grocery_list: List<GroceryTable> = listOf<GroceryTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init_recycler(grocery_list)
        init_data(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.itemsImg.setOnClickListener {
            set_visibility(View.GONE)
        }
    }

    private fun init_data(view: View) {
        navGraph = Navigation.findNavController(view)
        home_viewmodel = ViewModelProviders.of(requireActivity()).get(HomeViewModel::class.java)

        home_viewmodel.getAllGrocery(requireContext())
            .observe(viewLifecycleOwner, Observer { list ->
                grocery_list = list
                init_recycler(list)
            })

    }

    private fun init_recycler(list: List<GroceryTable>) {
        val arrayList = ArrayList<GroceryTable>()
        for (grocery in list) {
            if (!grocery.complete && !grocery.home_delete) {
                arrayList.add(grocery)
            }
        }

        groceryAdapter = GroceryAdapter(arrayList, "home", this)
        binding.groceryRv.setHasFixedSize(true)
        binding.groceryRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.groceryRv.adapter = groceryAdapter
        groceryAdapter.notifyDataSetChanged()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                if (direction == ItemTouchHelper.LEFT) {

                    val grocery = groceryAdapter.groceryList[viewHolder.adapterPosition]
                    grocery.home_delete = true

                    launch {
                        home_viewmodel.updateGrocery(
                            context!!,
                            grocery
                        )
                    }
                    init_recycler(grocery_list)
                    Toast.makeText(context!!, "Grocery Deleted!", Toast.LENGTH_SHORT).show()
                } else {
                    val grocery = groceryAdapter.groceryList[viewHolder.adapterPosition]
                    grocery.complete = true

                    launch {
                        home_viewmodel.updateGrocery(
                            context!!,
                            grocery
                        )
                    }
                    init_recycler(grocery_list)
                    Toast.makeText(context!!, "Grocery Completed!", Toast.LENGTH_SHORT).show()
                }

            }
        }).attachToRecyclerView(binding.groceryRv)
    }

    override fun onItemClick(grocery: GroceryTable) {
        set_visibility(View.VISIBLE)

        tv_items.text = grocery.items
        tv_items.movementMethod = ScrollingMovementMethod()
    }

    private fun set_visibility(visibility: Int) {
        binding.itemView.visibility = visibility
        binding.tvItems.visibility = visibility
        binding.itemsImg.visibility = visibility
    }


}