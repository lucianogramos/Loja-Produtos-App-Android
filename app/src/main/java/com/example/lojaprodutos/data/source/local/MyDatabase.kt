package com.example.lojaprodutos.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lojaprodutos.data.source.local.dao.ProductDao
import com.example.lojaprodutos.data.source.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = MyDatabase::class.java,
                    name = "my_database.db"
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}
