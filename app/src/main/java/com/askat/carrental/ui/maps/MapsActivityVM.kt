package com.askat.carrental.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.custemapp.damain.RepositoryImpl
import com.example.taxiapp.damain.models.Order
import com.example.taxiapp.damain.models.Orders
import com.example.taxiapp.damain.usecases.CreateOrderUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class MapsActivityVM:ViewModel() {
    private val repository = RepositoryImpl
    private val createOrderUseCase = CreateOrderUseCase(repository)


    private val _orders:MutableLiveData<Response<Orders>> = MutableLiveData()
    val orders :LiveData<Response<Orders>> get() = _orders


    private val _orderState:MutableLiveData<Response<Order>> = MutableLiveData()
    val orderState:LiveData<Response<Order>> get() = _orderState

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> get() = _error





    fun createOrder(order: Order) = viewModelScope.launch {
        try {
            val response = createOrderUseCase.execute(order)
            if (response.isSuccessful){
                _orderState.value = response
            }
        }catch (e:Exception){
            _error.value = e.message
        }
    }
}