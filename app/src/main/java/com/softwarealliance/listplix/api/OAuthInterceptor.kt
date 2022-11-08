package com.softwarealliance.listplix.api

import android.content.Context
import com.softwarealliance.listplix.utils.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor(context: Context): Interceptor {
    private val sessionManager = LocalStorage(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }


}