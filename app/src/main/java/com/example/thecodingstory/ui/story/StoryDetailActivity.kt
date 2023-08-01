package com.example.thecodingstory.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.thecodingstory.R
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.api.response.Story
import com.example.thecodingstory.api.response.StoryResponse
import com.example.thecodingstory.database.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryDetailActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_detail)
        val id =  intent.getStringExtra("id")

        userPreference = UserPreference(this)
        fetchDetail(id!!)
    }

    private fun fetchDetail(id: String){
        val token = userPreference.getToken()!!
        val client = ApiConfig.getApiService(token).getStory(id)
        client.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        val story = responseBody.story
                        renderDetail(story)
                        supportActionBar?.title = "Story from ${story.name}"
                    }
                } else {
                    val toast = Toast.makeText(applicationContext,"Can Not Find Story", Toast.LENGTH_LONG)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                val message = t.message
                val toast = Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT)
                toast.show()
            }

        })

    }

    private fun renderDetail(story: Story){
        val photo = findViewById<ImageView>(R.id.det_photo)
        val username = findViewById<TextView>(R.id.det_username)
        val timestamp = findViewById<TextView>(R.id.det_timestamp)
        val description = findViewById<TextView>(R.id.det_description)

        Glide.with(this@StoryDetailActivity)
            .load(story.photoUrl)
            .into(photo)

        username.text = story.name
        timestamp.text = story.createdAt
        description.text = story.description

    }
}