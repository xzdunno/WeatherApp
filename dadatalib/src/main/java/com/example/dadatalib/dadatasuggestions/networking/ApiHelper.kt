package app.illabo.dadatasuggestions.networking

object ApiHelper {
    fun getService(baseUrl: String, token: String): ApiService? =
        ClientInstance.getRetrofitInstance(baseUrl, token)?.create(ApiService::class.java)
}