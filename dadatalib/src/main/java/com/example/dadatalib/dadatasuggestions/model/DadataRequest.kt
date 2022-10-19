package app.illabo.dadatasuggestions.model

import app.illabo.dadatasuggestions.networking.ApiService
import retrofit2.Call

interface DadataRequest<T> {
    fun buildCall(forClient: ApiService): Call<T>
}