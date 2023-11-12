package com.wein_business.data.api

import com.wein_business.utils.Constants
import com.wein_business.utils.Constants.getBaseURL
import com.wein_business.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {
    private var api: ApiInterface? = null

    fun getApi(): ApiInterface {
        if (api == null) {
            api = buildRequestClient()
        }
        return api as ApiInterface
    }

    private fun buildRequestClient(): ApiInterface {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val requestInterceptor: Interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.addHeader("Content-Type", "application/json")
                requestBuilder.addHeader("Accept", "application/json")

                if (!SessionManager.isGuest)
                    requestBuilder.addHeader(
                        Constants.HEADER_AUTH,
                        Constants.HEADER_BEARER + SessionManager.userModel.token
                    )

                val request: Request = requestBuilder.build()
                return chain.proceed(request)
            }
        }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(TokenAuthenticator)
            .build()

        val retrofitbuilder = Retrofit.Builder()
            .baseUrl(getBaseURL())
            .addConverterFactory(
                GsonConverterFactory.create()
            )

        val retrofit = retrofitbuilder.client(okHttpClient).build()
        return retrofit.create(ApiInterface::class.java)
    }

}