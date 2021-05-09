package com.droidgeeks.groceryapp.di

import androidx.room.Database
import com.droidgeeks.groceryapp.BaseApplication
import com.droidgeeks.groceryapp.room.DataBase
import com.droidgeeks.groceryapp.room.dao.GroceryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: BaseApplication): DataBase {
        return DataBase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideGroceryDao(database: DataBase): GroceryDao {
        return database.getGroceryDao()
    }

}