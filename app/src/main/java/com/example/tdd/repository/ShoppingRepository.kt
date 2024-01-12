package com.example.tdd.repository

import androidx.lifecycle.LiveData
import com.example.tdd.data.local.ShoppingItem
import com.example.tdd.data.remote.response.ImageResponse
import com.example.tdd.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageSearch: String): Resource<ImageResponse>
}