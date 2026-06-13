package com.example.lojaprodutos

import android.app.Application
import com.example.lojaprodutos.di.AppContainer

class ProductStoreApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}