package com.droidgeeks.groceryapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.droidgeeks.groceryapp.room.DataBase
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroceryRepository() {

    suspend fun addGroceryItem(context: Context, groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            DataBase.getInstance(context).getGroceryDao().insert(groceryTable)
        }
    }

    suspend fun updateGroceryItem(context: Context, groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            DataBase.getInstance(context).getGroceryDao().update(groceryTable)
        }
    }

    fun allGroceryItems(context: Context): LiveData<List<GroceryTable>> {
        return DataBase.getInstance(context).getGroceryDao().allGroceries
    }

    suspend fun deleteGrocery(context: Context, groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            DataBase.getInstance(context).getGroceryDao().delete(groceryTable)
        }
    }


}