package app.illabo.dadatasuggestions.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddressSuggestionConstraint {

    @SerializedName("region")
    @Expose
    var region: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("street_type_full")
    @Expose
    var streetTypeFull: String? = null

    @SerializedName("settlement_type_full")
    @Expose
    var settlementTypeFull: String? = null

    @SerializedName("city_district_type_full")
    @Expose
    var cityDistrictTypeFull: String? = null

    @SerializedName("city_type_full")
    @Expose
    var cityTypeFull: String? = null

    @SerializedName("area_type_full")
    @Expose
    var areaTypeFull: String? = null

    @SerializedName("region_type_full")
    @Expose
    var regionTypeFull: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = "*"

    @SerializedName("country_iso_code")
    @Expose
    var countryISOCode: String? = null

    @SerializedName("region_iso_code")
    @Expose
    var regionISOCode: String? = null

    @SerializedName("kladr_id")
    @Expose
    var kladrId: String? = null

    @SerializedName("region_fias_id")
    @Expose
    var regionFIASId: String? = null

    @SerializedName("area_fias_id")
    @Expose
    var areaFIASId: String? = null

    @SerializedName("city_fias_id")
    @Expose
    var cityFIASId: String? = null

    @SerializedName("settlement_fias_id")
    @Expose
    var settlementFIASId: String? = null

    @SerializedName("street_fias_id")
    @Expose
    var streetFIASId: String? = null
}