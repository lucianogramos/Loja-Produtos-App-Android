package com.example.lojaprodutos.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojaprodutos.domain.model.Product
import kotlinx.coroutines.launch

data class StoreUiState(
    val products: List<Product> = emptyList(),
    val cart: MutableList<Int> = mutableListOf()
)

class StoreViewModel : ViewModel() {
    var uiState by mutableStateOf(StoreUiState())
        private set

    fun addProductToCart(id: Int) {
        val cart = uiState.cart
        cart.add(id)
        uiState = uiState.copy(cart = cart)
    }

    fun buyProducts() {
        viewModelScope.launch {
            // TODO: Criar lógica de compra dos produtos que estão no carrinho
        }
    }
}