package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droidgeeks.groceryapp.databinding.FragmentAllGroceriesBinding
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
class AllGroceriesFragment : Fragment(), CoroutineScope, GroceryAdapter.OnItemClickListener {

    lateinit var binding: FragmentAllGroceriesBinding

    lateinit var navGraph: NavController

    private val homeViewmodel: HomeViewModel by viewModels()
    lateinit var groceryAdapter: GroceryAdapter

    private val compositeJob = Job()

    lateinit var grocery: GroceryTable

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllGroceriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init_recycler(grocery_list)
        initData(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.itemsImg.setOnClickListener {
            setVisibility(View.GONE)
        }

        binding.btnEdit.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToAddGroceryFragment(Gson().toJson(grocery))
            navGraph.navigate(action)

            setVisibility(View.GONE)
        }
    }

    private fun initData(view: View) {
        navGraph = Navigation.findNavController(view)

        homeViewmodel.getAllGrocery()
            .observe(viewLifecycleOwner) { list ->
                initRecycler(list)
            }

    }

    private fun initRecycler(list: List<GroceryTable>) {

        val arrayList = ArrayList<GroceryTable>()
        for (grocery in list) {
            if (grocery.complete || grocery.home_delete) {
                arrayList.add(grocery)
            }
        }

        groceryAdapter = GroceryAdapter(arrayList, "all", this)
        binding.allGroceryRv.setHasFixedSize(true)
        binding.allGroceryRv.layoutManager = LinearLayoutManager(requireActivity())
        binding.allGroceryRv.adapter = groceryAdapter
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
                launch {
                    homeViewmodel.deleteGrocery(
                        groceryAdapter.groceryList[viewHolder.adapterPosition]
                    )
                }
                Toast.makeText(context!!, "Grocery Deleted!", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.allGroceryRv)
    }

    override fun onItemClick(grocery: GroceryTable) {

        this.grocery = grocery
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