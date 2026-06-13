package com.example.lojaprodutos.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lojaprodutos.data.source.local.entity.ProductEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity)

    @Delete
    suspend fun delete(entities: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<ProductEntity>>
}
