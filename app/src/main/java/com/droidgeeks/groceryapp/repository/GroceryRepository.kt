package com.droidgeeks.groceryapp.repository

import androidx.lifecycle.LiveData
import com.droidgeeks.groceryapp.BaseApplication
import com.droidgeeks.groceryapp.room.dao.GroceryDao
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroceryRepository(
    private val context: BaseApplication,
    private val groceryDao: GroceryDao
) {

    suspend fun addGroceryItem(groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            groceryDao.insert(groceryTable)
        }
    }

    suspend fun updateGroceryItem(groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            groceryDao.update(groceryTable)
        }
    }

    fun allGroceryItems(): LiveData<List<GroceryTable>> {
        return groceryDao.allGroceries
    }

    suspend fun deleteGrocery(groceryTable: GroceryTable) {
        withContext(Dispatchers.IO) {
            groceryDao.delete(groceryTable)
        }
    }


}