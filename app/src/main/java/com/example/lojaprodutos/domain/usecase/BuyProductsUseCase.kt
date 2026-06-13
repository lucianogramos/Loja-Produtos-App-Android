package com.example.lojaprodutos.domain.usecase

import com.example.lojaprodutos.domain.repository.ProductRepository

class BuyProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(cart: List<Int>) {
        productRepository.buyProducts(cart)
    }
}
