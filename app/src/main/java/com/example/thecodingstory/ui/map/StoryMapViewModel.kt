package com.example.thecodingstory.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.api.response.ListStoryItem
import com.example.thecodingstory.api.response.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryMapViewModel : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    companion object{
        private const val TAG = "StoryMapViewModel"
    }

    fun findRestaurant(token : String) {

        val client = ApiConfig.getApiService(token).getAllStoriesMap()
        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _stories.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}