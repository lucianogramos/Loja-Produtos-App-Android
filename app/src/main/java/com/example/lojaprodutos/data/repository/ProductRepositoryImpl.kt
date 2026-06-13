package com.example.lojaprodutos.data.repository

import com.example.lojaprodutos.data.mapper.toEntity
import com.example.lojaprodutos.data.mapper.toModel
import com.example.lojaprodutos.data.source.local.dao.ProductDao
import com.example.lojaprodutos.domain.model.Product
import com.example.lojaprodutos.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {
    override fun insertProduct(product: Product) {
        productDao.insert(product.toEntity())
    }

    override fun getAllProducts(): List<Product> {
        return productDao.getAll().map { it.toModel() }
    }

    override fun buyProducts(cart: List<Int>) {
        TODO("Not yet implemented")
    }
}