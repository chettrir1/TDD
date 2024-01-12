package com.example.tdd.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.data.local.ShoppingItem
import com.example.tdd.data.remote.response.ImageResponse
import com.example.tdd.other.Constants
import com.example.tdd.other.Event
import com.example.tdd.other.Resource
import com.example.tdd.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * here we can pass FakeShoppingRepository...
 * ...instead of ShoppingRepository to test
 * ...we don't have to create new viewmodel
 */
@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val image: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDatabase(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The field cannot be empty!",
                        null
                    )
                )
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Name field exeeds the character limit!",
                        null
                    )
                )
            )
            return
        }

        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Price field exeeds the character limit!",
                        null
                    )
                )
            )
            return
        }

        val amount =
            try {
                amountString.toInt()
            } catch (e: Exception) {
                _insertShoppingItemStatus.postValue(
                    Event(
                        Resource.error(
                            "Please enter valid amount",
                            null
                        )
                    )
                )
                return
            }

        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemIntoDatabase(shoppingItem)
        setCurrentUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageSearch: String) {

    }
}