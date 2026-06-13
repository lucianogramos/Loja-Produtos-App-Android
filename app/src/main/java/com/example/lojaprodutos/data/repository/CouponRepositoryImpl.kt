package com.example.lojaprodutos.data.repository

import com.example.lojaprodutos.data.mapper.toEntity
import com.example.lojaprodutos.data.mapper.toModel
import com.example.lojaprodutos.data.source.local.dao.CouponDao
import com.example.lojaprodutos.domain.model.Coupon
import com.example.lojaprodutos.domain.repository.CouponRepository

class CouponRepositoryImpl(
    private val couponDao: CouponDao
) : CouponRepository {
    override suspend fun getCoupon(code: String): Coupon? {
        return couponDao.get(code)?.toModel()
    }

    override suspend fun insertCoupon(coupon: Coupon) {
        couponDao.insert(coupon.toEntity())
    }
}
