package app.illabo.dadatasuggestions.networking

import app.illabo.dadatasuggestions.Constants
import app.illabo.dadatasuggestions.model.AddressSuggestionRequest
import app.illabo.dadatasuggestions.model.DadataAddressResponse
import app.illabo.dadatasuggestions.model.ReverseGeocodeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Constants.addressEndpoint)
    fun getSuggestions(@Body request: AddressSuggestionRequest): Call<DadataAddressResponse>

    @POST(Constants.revGeocodeEndpoint)
    fun getRevGeocode(@Body request: ReverseGeocodeRequest): Call<DadataAddressResponse>
}