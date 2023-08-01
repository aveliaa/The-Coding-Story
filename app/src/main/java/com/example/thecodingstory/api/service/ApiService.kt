package com.example.thecodingstory.api.service

import com.example.thecodingstory.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories?location=1")
    fun getAllStoriesMap(): Call<ListStoryResponse>

    @GET("stories")
    suspend fun getAllStoriesPager(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListStoryResponse

    @GET("stories/{id}")
    fun getStory(
        @Path("id") id: String
    ): Call<StoryResponse>

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

}