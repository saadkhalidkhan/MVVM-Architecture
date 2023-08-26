package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidgeeks.groceryapp.databinding.FragmentPendingBinding
import com.droidgeeks.groceryapp.interfaces.GenericAdapterCallback
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.ui.GroceryAdapter
import com.droidgeeks.groceryapp.view_model.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_all_groceries.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class PendingFragment(private val callback: GenericAdapterCallback) : Fragment(), CoroutineScope, GroceryAdapter.OnItemClickListener {


    lateinit var binding: FragmentPendingBinding

    lateinit var navGraph: NavController

    private val homeViewmodel: HomeViewModel by viewModels()
    lateinit var groceryAdapter: GroceryAdapter

    private val compositeJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    var groceryList: List<GroceryTable> = listOf()

    lateinit var grocery: GroceryTable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData(view)
    }

    private fun initData(view: View) {
        navGraph = Navigation.findNavController(view)

        homeViewmodel.getAllGrocery()
            .observe(viewLifecycleOwner) { list ->
                groceryList = list
                initRecycler(list)
            }

        binding.itemsImg.setOnClickListener {
            callback.setBottomNavVisibility(View.VISIBLE)
            setVisibility(View.GONE)
        }

        binding.btnEdit.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToAddGroceryFragment(Gson().toJson(grocery))
            navGraph.navigate(action)

            callback.setBottomNavVisibility(View.GONE)
            setVisibility(View.GONE)
        }

    }

    private fun initRecycler(list: List<GroceryTable>) {
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
                        homeViewmodel.updateGrocery(
                            grocery
                        )
                    }
                    initRecycler(groceryList)
                    Toast.makeText(context!!, "Grocery Deleted!", Toast.LENGTH_SHORT).show()
                } else {
                    val grocery = groceryAdapter.groceryList[viewHolder.adapterPosition]
                    grocery.complete = true

                    launch {
                        homeViewmodel.updateGrocery(
                            grocery
                        )
                    }
                    initRecycler(groceryList)
                    Toast.makeText(context!!, "Grocery Completed!", Toast.LENGTH_SHORT).show()
                }

            }
        }).attachToRecyclerView(binding.groceryRv)
    }

    override fun onItemClick(grocery: GroceryTable) {
        this.grocery = grocery
        callback.setBottomNavVisibility(View.GONE)
        setVisibility(View.VISIBLE)
        tv_items.text = grocery.items
        tv_items.movementMethod = ScrollingMovementMethod()
    }

    private fun setVisibility(visibility: Int) {
        binding.itemView.visibility = visibility
        binding.tvItems.visibility = visibility
        binding.itemsImg.visibility = visibility
        binding.btnEdit.visibility = visibility
    }


}