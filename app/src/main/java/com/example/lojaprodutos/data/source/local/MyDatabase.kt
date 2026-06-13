package com.example.lojaprodutos.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lojaprodutos.data.source.local.dao.CouponDao
import com.example.lojaprodutos.data.source.local.dao.ProductDao
import com.example.lojaprodutos.data.source.local.entity.CouponEntity
import com.example.lojaprodutos.data.source.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, CouponEntity::class], version = 2, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getCouponDao(): CouponDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = MyDatabase::class.java,
                    name = "my_database.db"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}
