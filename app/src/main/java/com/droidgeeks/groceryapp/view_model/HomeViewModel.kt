package com.droidgeeks.groceryapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.droidgeeks.groceryapp.repository.GroceryRepository
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_ITMES
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_NAME
import com.droidgeeks.groceryapp.utility.AppConstant.EMPTY_NAME_ITMES
import com.droidgeeks.groceryapp.utility.AppConstant.SUCCESS
import com.droidgeeks.groceryapp.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository
) : ViewModel() {

    fun validateCredentials(list_name: String, items: String): SingleLiveEvent<String> {

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

    suspend fun insertGrocery(groceryTable: GroceryTable) {
        groceryRepository.addGroceryItem(groceryTable)
    }

    suspend fun updateGrocery(groceryTable: GroceryTable) {
        groceryRepository.updateGroceryItem(groceryTable)
    }

    suspend fun deleteGrocery(groceryTable: GroceryTable) {
        groceryRepository.deleteGrocery(groceryTable)
    }

    fun getAllGrocery(): LiveData<List<GroceryTable>> {
        return groceryRepository.allGroceryItems()
    }


}