package com.project.core.data.remote.response


import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    @SerializedName("articles")
    val articles: List<Article?>? = listOf(),
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("totalResults")
    val totalResults: Int? = 0,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("code")
    val code: String? = ""
) {
    data class Article(
        @SerializedName("author")
        val author: String? = "",
        @SerializedName("content")
        val content: String? = "",
        @SerializedName("description")
        val description: String? = "",
        @SerializedName("publishedAt")
        val publishedAt: String? = "",
        @SerializedName("source")
        val source: Source? = Source(),
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("url")
        val url: String? = "",
        @SerializedName("urlToImage")
        val urlToImage: String? = ""
    ) {
        data class Source(
            @SerializedName("id")
            val id: String? = "",
            @SerializedName("name")
            val name: String? = ""
        )
    }
}