package com.example.tdd.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tdd.data.local.ShoppingItem
import com.example.tdd.data.remote.response.ImageResponse
import com.example.tdd.other.Resource
import com.example.tdd.repository.ShoppingRepository

/**
 * FakeShoppingRepository class is created just...
 * ...to test the viewmodel class because viewmodel
 * ...takes repository as a parameter, with that
 * we can test if the viewmodel is properly responding
 * to the events coming from repository
 */
class FakeShoppingRepository : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldShowNetworkError = false

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    fun shouldShowNetworkError(value: Boolean) {
        shouldShowNetworkError = value
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageSearch: String): Resource<ImageResponse> {
        return if (shouldShowNetworkError) {
            Resource.error("Internet not Available!", null)
        } else {
            val response = ImageResponse(listOf(), 0, 0)
            Resource.success(response)
        }
    }
}