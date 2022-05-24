package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidgeeks.groceryapp.R
import com.droidgeeks.groceryapp.databinding.FragmentAddGroceryBinding
import com.droidgeeks.groceryapp.interfaces.GenericAdapterCallback
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.utility.AppConstant
import com.droidgeeks.groceryapp.utility.disableError
import com.droidgeeks.groceryapp.view_model.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_grocery.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AddGroceryFragment : Fragment(), GenericAdapterCallback, CoroutineScope {

    lateinit var navGraph: NavController

    private val homeViewmodel: HomeViewModel by viewModels()

    lateinit var binding: FragmentAddGroceryBinding

    private val compositeJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGroceryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(view)
    }

    private fun initData(view: View) {
        navGraph = Navigation.findNavController(view)

        checkIntent()

        binding.callBack = this@AddGroceryFragment
        binding.toolbar.tv_toolbar.text = resources.getString(R.string.add_grocery)

    }

    private fun checkIntent() {
        if (arguments?.getString(AppConstant.GROCERY_DATA) != null) {
            val grocery = Gson().fromJson(
                arguments?.getString(AppConstant.GROCERY_DATA),
                GroceryTable::class.java
            )
            binding.listName.setText(grocery.list_name)
            binding.itemList.setText(grocery.items)

        }

    }

    override fun <T> getClickedObject() {

        homeViewmodel.validateCredentials(list_name.text.toString(), item_list.text.toString())
            .observe(this@AddGroceryFragment) { result ->
                if (result?.equals(AppConstant.SUCCESS)!!) {
                    list_name_layout.error = null
                    item_list_layout.error = null
                    list_name.clearFocus()
                    item_list.clearFocus()

                    val groceryTable = GroceryTable()

                    groceryTable.list_name = list_name.text.toString().trim()
                    groceryTable.items = item_list.text.toString().trim()
                    groceryTable.date = Date()
                    groceryTable.complete = false
                    groceryTable.home_delete = false

                    launch {
                        homeViewmodel.insertGrocery(
                            groceryTable
                        )
                    }

                    navGraph.navigateUp()

                } else {
                    when (result) {
                        AppConstant.EMPTY_NAME_ITMES -> {
                            item_list_layout.error = AppConstant.EMPTY_ITMES
                            list_name_layout.error = AppConstant.EMPTY_NAME

                            list_name.disableError(list_name_layout)
                            item_list.disableError(item_list_layout)

                        }
                        AppConstant.EMPTY_NAME -> {
                            list_name_layout.error = AppConstant.EMPTY_NAME
                            item_list_layout.error = null

                            item_list.disableError(item_list_layout)

                        }
                        AppConstant.EMPTY_ITMES -> {
                            item_list_layout.error = AppConstant.EMPTY_ITMES
                            list_name_layout.error = null

                            list_name.disableError(list_name_layout)
                        }
                    }
                }
            }
    }

    override fun setBottomNavVisibility(visibility: Int) {

    }


}