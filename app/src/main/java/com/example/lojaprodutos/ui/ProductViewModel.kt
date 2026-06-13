package com.example.lojaprodutos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lojaprodutos.ProductStoreApplication
import com.example.lojaprodutos.domain.model.Cart
import com.example.lojaprodutos.domain.model.Coupon
import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.domain.usecase.AddCouponUseCase
import com.example.lojaprodutos.domain.usecase.AddProductUseCase
import com.example.lojaprodutos.domain.usecase.GetAllProductsUseCase
import com.example.lojaprodutos.domain.usecase.BuyProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val addCouponUseCase: AddCouponUseCase,
    private val buyProductsUseCase: BuyProductsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _cart = MutableStateFlow<List<Int>>(emptyList())
    val cart: StateFlow<List<Int>> = _cart.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _checkoutResult = MutableStateFlow<Double?>(null)
    val checkoutResult: StateFlow<Double?> = _checkoutResult.asStateFlow()

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
            addProductUseCase(Product(name = name, price = price))
        }
    }

    fun addToCart(product: Product) {
        _cart.update { it + product.id }
    }

    fun applyCoupon(code: String) {
        _appliedCoupon.value = code
    }

    fun addCoupon(code: String, tax: Double) {
        viewModelScope.launch {
            addCouponUseCase(Coupon(code, tax / 100.0))
        }
    }

    fun clearCheckoutResult() {
        _checkoutResult.value = null
    }

    fun checkout() {
        val currentProducts = productListState.value
        val cartIds = _cart.value
        val productsInCart = currentProducts.filter { it.id in cartIds }
        
        if (productsInCart.isNotEmpty()) {
            viewModelScope.launch {
                val totalPrice = buyProductsUseCase(
                    BuyProductsUseCase.Params(
                        cart = Cart(productsInCart),
                        couponCode = _appliedCoupon.value
                    )
                )
                _checkoutResult.value = totalPrice
                _cart.value = emptyList()
                _appliedCoupon.value = null
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ProductStoreApplication
                return ProductViewModel(
                    getAllProductsUseCase = application.container.getAllProductsUseCase,
                    addProductUseCase = application.container.addProductUseCase,
                    addCouponUseCase = application.container.addCouponUseCase,
                    buyProductsUseCase = application.container.buyProductsUseCase
                ) as T
            }
        }
    }
}
