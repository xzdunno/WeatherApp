package app.illabo.dadatasuggestions.networking

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClientInstance {
    private var retrofit: Retrofit? = null
    fun getRetrofitInstance(baseUrl: String, token: String): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .setLenient()
                            .create()
                    )
                )
                .client(buildHttpClientWithTokenInjector(token))
                .build()
        }
        return retrofit
    }

    private fun buildHttpClientWithTokenInjector(token: String): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader(
                    "Authorization", "Token $token"
                )
                .addHeader(
                    "Content-Type", "application/json"
                )
                .method(request.method(), request.body()).build()
            chain.proceed(newRequest)
        }.build()
    }
}