package com.droidgeeks.groceryapp.room.tables

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.droidgeeks.groceryapp.utility.TimestampConverter
import java.util.*

@Entity(tableName = "grocery_table")
class GroceryTable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var list_name: String = ""
    var items: String = ""

    @NonNull
    @TypeConverters(TimestampConverter::class)
    lateinit var date: Date

    var complete: Boolean = false
    var home_delete: Boolean = false


}