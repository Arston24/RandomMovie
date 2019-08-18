package com.example.randommovie.network.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import retrofit2.Response
import com.example.randommovie.network.Api


abstract class BaseRepository<T>(@PublishedApi internal val service: Api) {
    abstract fun loadData(page: Int): LiveData<List<T>>

    inline fun <reified T : Any> fetchData(crossinline call: (Api) -> Deferred<Response<List<T>>>): LiveData<List<T>> {
        val result = MutableLiveData<List<T>>()

        CoroutineScope(Dispatchers.IO).launch {
            val request = call(service)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        result.value = response.body()
                    } else {
                        Log.d("Error occurred with code", "${response.code()}")
                    }
                }  catch (e: Throwable) {
                    Log.d("Error:", e.message)
                }
            }
        }

        return result
    }
}