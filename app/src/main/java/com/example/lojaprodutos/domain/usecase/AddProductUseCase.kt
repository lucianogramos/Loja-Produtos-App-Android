package com.example.lojaprodutos.domain.usecase

import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.domain.repository.ProductRepository

class AddProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.insertProduct(product)
    }
}
