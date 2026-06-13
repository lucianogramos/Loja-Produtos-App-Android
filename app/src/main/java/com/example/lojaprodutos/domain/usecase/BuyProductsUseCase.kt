package com.example.lojaprodutos.domain.usecase

import com.example.lojaprodutos.domain.model.Cart
import com.example.lojaprodutos.domain.repository.ProductRepository
import com.example.lojaprodutos.domain.services.DiscountService

class BuyProductsUseCase(
    private val productRepository: ProductRepository,
    private val discountService: DiscountService
) {
    data class Params(
        val cart: Cart,
        val couponCode: String?
    )

    suspend operator fun invoke(params: Params): Double {
        val cart = params.cart
        val couponCode = params.couponCode
        val totalPrice = discountService.applyDiscount(cart.getTotalPrice(), couponCode)
        productRepository.removeProducts(cart.products)
        return totalPrice
    }
}
