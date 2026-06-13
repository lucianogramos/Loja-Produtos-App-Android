package com.example.lojaprodutos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lojaprodutos.ProductStoreApplication
import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.domain.usecase.AddProductUseCase
import com.example.lojaprodutos.domain.usecase.GetAllProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val productListState: StateFlow<List<Product>> = getAllProductsUseCase()
        .combine(_searchQuery) { products, query ->
            if (query.isEmpty()) {
                products
            } else {
                products.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addProduct(name: String, price: Double) {
        viewModelScope.launch {
            addProductUseCase(Product(name, price))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ProductStoreApplication
                return ProductViewModel(
                    getAllProductsUseCase = application.container.getAllProductsUseCase,
                    addProductUseCase = application.container.addProductUseCase
                ) as T
            }
        }
    }
}
