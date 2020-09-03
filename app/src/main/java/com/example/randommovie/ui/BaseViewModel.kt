package com.example.randommovie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T> : ViewModel(){
    abstract fun getDataFromRetrofit(page: Int): LiveData<List<T>>
}