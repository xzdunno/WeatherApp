package app.illabo.dadatasuggestions.model

import app.illabo.dadatasuggestions.networking.ApiService
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReverseGeocodeRequest(
        lat: Double,
        lon: Double,
        count: Int = 10,
        language: String? = null
) : DadataRequest<DadataAddressResponse> {
    @SerializedName("lat")
    @Expose
    private var lat = lat

    @SerializedName("lon")
    @Expose
    private var lon = lon

    @SerializedName("count")
    @Expose
    private var count: Int = count

    @SerializedName("language")
    @Expose
    private var language: String? = language

    fun setLatitude(lat: Double): ReverseGeocodeRequest {
        this.lat = lat
        return this
    }

    fun setLongitude(lon: Double): ReverseGeocodeRequest {
        this.lon = lon
        return this
    }

    fun setCount(count: Int): ReverseGeocodeRequest {
        this.count = count
        return this
    }

    fun setLanguage(language: String?): ReverseGeocodeRequest {
        if (language != null && listOf("en", "ru").contains(language)) {
            this.language = language
        }
        return this
    }

    fun getLatitude() = lat
    fun getLongitude() = lon
    fun getCount() = count
    fun getLanguage() = language

    override fun buildCall(forClient: ApiService) = forClient.getRevGeocode(this)
}