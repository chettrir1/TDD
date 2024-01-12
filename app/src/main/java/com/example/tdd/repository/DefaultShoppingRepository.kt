package com.example.tdd.repository

import androidx.lifecycle.LiveData
import com.example.tdd.data.local.ShoppingDao
import com.example.tdd.data.local.ShoppingItem
import com.example.tdd.data.remote.response.ImageResponse
import com.example.tdd.data.remote.response.PixaBayApi
import com.example.tdd.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixaBayApi: PixaBayApi
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItems(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageSearch: String): Resource<ImageResponse> {
        return try {
            val response = pixaBayApi.searchForImage(imageSearch)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown error Occurred", null)
            } else {
                Resource.error("An Unknown error Occurred", null)
            }

        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }
}