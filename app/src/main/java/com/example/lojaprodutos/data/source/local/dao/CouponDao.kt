package com.example.lojaprodutos.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lojaprodutos.data.source.local.entity.CouponEntity

@Dao
interface CouponDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coupon: CouponEntity)

    @Query("SELECT * FROM coupons WHERE code = :code")
    suspend fun get(code: String): CouponEntity?
}