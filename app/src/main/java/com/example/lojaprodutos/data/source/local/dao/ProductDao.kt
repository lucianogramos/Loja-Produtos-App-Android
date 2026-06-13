package com.example.lojaprodutos.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lojaprodutos.data.source.local.entity.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: ProductEntity): Unit

    @Query("SELECT * FROM products")
    fun getAll(): List<ProductEntity>
}