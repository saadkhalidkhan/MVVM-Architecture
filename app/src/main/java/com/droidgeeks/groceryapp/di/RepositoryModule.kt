package com.droidgeeks.groceryapp.di

import com.droidgeeks.groceryapp.BaseApplication
import com.droidgeeks.groceryapp.repository.GroceryRepository
import com.droidgeeks.groceryapp.room.dao.GroceryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGroceryRepository(
        context: BaseApplication,
        groceryDao: GroceryDao
    ): GroceryRepository {
        return GroceryRepository(context, groceryDao)
    }

}