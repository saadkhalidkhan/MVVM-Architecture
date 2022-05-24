package com.droidgeeks.groceryapp.interfaces

interface GenericAdapterCallback {
    fun <T> getClickedObject()

    fun setBottomNavVisibility(visibility: Int)
}