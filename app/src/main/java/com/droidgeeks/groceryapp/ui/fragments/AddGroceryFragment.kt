package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
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

    private val home_viewmodel: HomeViewModel by viewModels()

    lateinit var binding: FragmentAddGroceryBinding

    val compositeJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + compositeJob

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddGroceryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init_data(view)
    }

    private fun init_data(view: View) {
        navGraph = Navigation.findNavController(view)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkIntent()

        binding.callBack = this@AddGroceryFragment
        binding.toolbar.tv_toolbar.text = "Add Grocery"
    }

    private fun checkIntent() {
        if (arguments?.getString(AppConstant.GROCERY_DATA) != null) {
            val grocery = Gson().fromJson<GroceryTable>(
                arguments?.getString(AppConstant.GROCERY_DATA),
                GroceryTable::class.java
            )
            binding.listName.setText(grocery.list_name)
            binding.itemList.setText(grocery.items)

        }

    }

    override fun <T> getClickedObject() {

        home_viewmodel.ValidateCredentials(list_name.text.toString(), item_list.text.toString())
            .observe(this@AddGroceryFragment,
                androidx.lifecycle.Observer { result ->
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
                            home_viewmodel.insertGrocery(
                                groceryTable
                            )
                        }

                        navGraph.navigateUp()

                    } else {
                        when {
                            result == AppConstant.EMPTY_NAME_ITMES -> {
                                item_list_layout.error = AppConstant.EMPTY_ITMES
                                list_name_layout.error = AppConstant.EMPTY_NAME

                                list_name.disableError(list_name_layout)
                                item_list.disableError(item_list_layout)

                            }
                            result == AppConstant.EMPTY_NAME -> {
                                list_name_layout.error = AppConstant.EMPTY_NAME
                                item_list_layout.error = null

                                item_list.disableError(item_list_layout)

                            }
                            result == AppConstant.EMPTY_ITMES -> {
                                item_list_layout.error = AppConstant.EMPTY_ITMES
                                list_name_layout.error = null

                                list_name.disableError(list_name_layout)
                            }
                        }
                    }
                })
    }


}