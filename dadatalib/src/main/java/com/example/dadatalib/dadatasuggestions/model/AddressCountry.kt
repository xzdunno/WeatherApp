package com.illabo.dadatasuggestions.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddressCountry {
    @SerializedName("country")
    @Expose
    var country: String = "*"
}