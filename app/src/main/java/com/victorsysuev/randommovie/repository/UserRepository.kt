package com.victorsysuev.randommovie.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.victorsysuev.randommovie.network.Api
import timber.log.Timber


class UserRepository {
    private val apiClient = Api.create()
    private val apiKey: String = ru.arston.randommovie.BuildConfig.TMDB_API_KEY

    suspend fun authenticateUser(username: String, password: String): String {
        Timber.e("name $username pass $password")

        val requestToken = apiClient.getRequestToken(apiKey)
        val retMap: Map<String, Any> = Gson().fromJson(
            requestToken.execute().body()?.string(), object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )

        val res = apiClient.createSessionWithLogin(apiKey, username, password, retMap["request_token"].toString())
        val session = apiClient.createSession(apiKey, retMap["request_token"].toString())
        val sessionMap: Map<String, Any> = Gson().fromJson(
            session.execute().body()?.string(), object : TypeToken<HashMap<String?, Any?>?>() {}.type
        )

//        Timber.e("session ${session.execute().body()?.string()}")


        return "${sessionMap["success"]}"

    }


}