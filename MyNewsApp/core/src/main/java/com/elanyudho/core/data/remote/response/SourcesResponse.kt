package com.project.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("sources")
    val sources: List<Source?>? = listOf(),
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("code")
    val code: String? = ""

) {
    data class Source(
        @SerializedName("category")
        val category: String? = "",
        @SerializedName("country")
        val country: String? = "",
        @SerializedName("description")
        val description: String? = "",
        @SerializedName("id")
        val id: String? = "",
        @SerializedName("language")
        val language: String? = "",
        @SerializedName("name")
        val name: String? = "",
        @SerializedName("url")
        val url: String? = ""
    )
}