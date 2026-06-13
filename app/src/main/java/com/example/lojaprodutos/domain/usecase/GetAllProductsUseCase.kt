package com.example.lojaprodutos.domain.usecase

import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.domain.repository.ProductRepository

class GetAllProductsUseCase(
    private val productRepository: ProductRepository
) {
    operator fun invoke(id: Int): List<Product> {
        return productRepository.getAllProducts()
    }
}
