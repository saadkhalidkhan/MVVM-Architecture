package com.droidgeeks.groceryapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.droidgeeks.groceryapp.room.dao.GroceryDao
import com.droidgeeks.groceryapp.room.tables.GroceryTable
import com.droidgeeks.groceryapp.utility.AppConstant.DatabaseName

@Database(
    entities = [GroceryTable::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    companion object {
        @Volatile
        private lateinit var instance: DataBase

        @Synchronized
        open fun getInstance(context: Context): DataBase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    DataBase::class.java,
                    DatabaseName
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return instance
        }
    }

    abstract fun getGroceryDao(): GroceryDao

}