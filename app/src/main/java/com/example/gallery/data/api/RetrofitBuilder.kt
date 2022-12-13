package com.example.gallery.data.api

import com.example.gallery.data.model.Tags
import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


object RetrofitBuilder {
    private const val BASE_URL = "https://api.flickr.com/"

    private fun getHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getHttpClient().build())
            .build()

    private val gson: Gson
        get() = GsonBuilder()
            .registerTypeAdapter(Tags::class.java, TagsDeserializer())
            .create()

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}

class TagsDeserializer : JsonDeserializer<Tags> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Tags {
        val data = json.asJsonPrimitive.asString.split(" ")
        return Tags(data)
    }
}
