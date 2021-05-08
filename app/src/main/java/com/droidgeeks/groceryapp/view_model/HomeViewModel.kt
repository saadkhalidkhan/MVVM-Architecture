package com.droidgeeks.groceryapp.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.droidgeeks.groceryapp.repository.GroceryRepository
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_ITMES
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_NAME
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_NAME_ITMES
import com.droidgeeks.groceryapp.utility.AppConstant.SUCCESS
import com.droidgeeks.groceryapp.utility.SingleLiveEvent

class HomeViewModel : ViewModel() {

    lateinit var groceryRepository: GroceryRepository

    init {
        if (!::groceryRepository.isInitialized) {
            groceryRepository = GroceryRepository()
        }
    }

    fun ValidateCredentials(list_name: String, items: String): SingleLiveEvent<String> {

        val loginValidation: SingleLiveEvent<String> = SingleLiveEvent()

        if (list_name.isEmpty() && items.isEmpty()) {
            loginValidation.postValue(EMPTY_NAME_ITMES)
        } else if (list_name.isEmpty()) {
            loginValidation.postValue(EMPTY_NAME)
        } else if (items.isEmpty()) {
            loginValidation.postValue(EMPTY_ITMES)
        } else {
            loginValidation.postValue(SUCCESS)
        }
        return loginValidation
    }

    suspend fun insertGrocery(context: Context, groceryTable: GroceryTable) {
        groceryRepository.addGroceryItem(context, groceryTable)
    }

    suspend fun updateGrocery(context: Context, groceryTable: GroceryTable) {
        groceryRepository.updateGroceryItem(context, groceryTable)
    }

    suspend fun deleteGrocery(context: Context, groceryTable: GroceryTable) {
        groceryRepository.deleteGrocery(context, groceryTable)
    }

    fun getAllGrocery(context: Context): LiveData<List<GroceryTable>> {
        return groceryRepository.allGroceryItems(context)
    }


}