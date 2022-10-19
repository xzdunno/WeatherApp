package app.illabo.dadatasuggestions

import android.util.Log
import app.illabo.dadatasuggestions.model.AddressSuggestionRequest
import app.illabo.dadatasuggestions.model.DadataAddressResponse
import app.illabo.dadatasuggestions.model.DadataRequest
import app.illabo.dadatasuggestions.model.ReverseGeocodeRequest
import app.illabo.dadatasuggestions.networking.ApiHelper
import app.illabo.dadatasuggestions.networking.BackoffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DadataSuggestions(private val token: String) {

    private val client = ApiHelper.getService(Constants.suggestionsAPIURL, token)

    fun suggest(query: String,
                count: Int = 10,
                language: String? = null,
                completion: ((DadataAddressResponse)-> Unit)? = null){
        performRequest(AddressSuggestionRequest(query, count, language), completion)
    }

    fun suggest(request: AddressSuggestionRequest,
                completion: ((DadataAddressResponse) -> Unit)? = null) {
        performRequest(request, completion)
    }

    // Geocode is an alias to suggest with count 1,
    // because suggestions with limit 1 returns extra parameters
    // including latitude and longitude for requested address.
    // Most of fields in response are the same.
    fun geocode(query: String, language: String? = null, completion: ((DadataAddressResponse)-> Unit)? = null) {
        performRequest(AddressSuggestionRequest(query, 1, language), completion)
    }

    fun reverseGeocode(latitude: Double,
                       longitude: Double,
                       count: Int = 10,
                       language: String? = null,
                       completion: ((DadataAddressResponse)-> Unit)? = null){
        performRequest(ReverseGeocodeRequest(latitude, longitude, count, language),completion)
    }

    fun reverseGeocode(request: ReverseGeocodeRequest, completion: ((DadataAddressResponse) -> Unit)? = null) {
        performRequest(request, completion)
    }

    private fun performRequest(request: DadataRequest<DadataAddressResponse>, completion: ((DadataAddressResponse)-> Unit)? = null) {
        if (client == null) { throw Throwable("Failed to create ApiService: is null") }
        request.buildCall(client).enqueue(object : BackoffCallback<DadataAddressResponse>(3, 300.0) {
            override fun onResponse(
                call: Call<DadataAddressResponse>,
                response: Response<DadataAddressResponse>
            ) {
                val resBody = response.body() ?: return
                CoroutineScope(Dispatchers.Default).launch {
                    completion?.let {
                        it(resBody)
                    }
                }
            }

            override fun onFailedAfterRetry(call: Call<DadataAddressResponse>, t: Throwable) {
                Log.e("Error calling api", t.message.toString())
            }
        })
    }

}