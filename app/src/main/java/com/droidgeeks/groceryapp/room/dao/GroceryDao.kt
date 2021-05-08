package com.droidgeeks.groceryapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidgeeks.groceryapp.room.tables.GroceryTable

@Dao
interface GroceryDao {
    @Insert
    fun insert(note: GroceryTable)

    @Update
    fun update(note: GroceryTable)

    @Delete
    fun delete(note: GroceryTable)

    @Query("DELETE FROM grocery_table")
    fun deleteAllGroceries()

    @get:Query("SELECT * FROM grocery_table ORDER BY date DESC")
    val allGroceries: LiveData<List<GroceryTable>>
}