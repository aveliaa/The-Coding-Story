package com.example.thecodingstory.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thecodingstory.R
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.database.UserPreference
import com.example.thecodingstory.ui.adapter.StoriesAdapter
import com.example.thecodingstory.ui.map.StoryMapsActivity
import com.example.thecodingstory.ui.user.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.thecodingstory.data.StoryRepository

class StoryActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "Story Activity"
    }

    private lateinit var userPreference: UserPreference
    private lateinit var rvStories: RecyclerView

    private fun setProgressBar(state: Boolean){
        val progress: ProgressBar = findViewById(R.id.progress)
        progress.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        userPreference = UserPreference(this)

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_story)
        setProgressBar(false)

        rvStories = findViewById(R.id.rv_stories)

        val appBar: MaterialToolbar = findViewById(R.id.topAppBar)
        appBar.setOnMenuItemClickListener {menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    userPreference.resetSession()
                    val moveIntent = Intent(this@StoryActivity,LoginActivity::class.java)
                    startActivity(moveIntent)
                    finish()
                    true
                }
                R.id.map -> {
                    val moveIntent = Intent(this@StoryActivity,StoryMapsActivity::class.java)
                    startActivity(moveIntent)
                    true
                }

                else -> {
                    false
                }
            }

        }

        val addStory: FloatingActionButton = findViewById(R.id.add_story)
        addStory.setOnClickListener {
            val moveIntent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(moveIntent)
            finish()
        }

        setProgressBar(false)
        getData(userPreference.getToken()!!)

    }

    private fun getData(token: String) {

        setProgressBar(true)

        val storyRepository = StoryRepository(ApiConfig.getApiService(token))
        val storyViewModel = StoryViewModel(storyRepository)

        rvStories.layoutManager = LinearLayoutManager(this@StoryActivity)

        val adapter = StoriesAdapter()
        rvStories.adapter = adapter

        setProgressBar(false)

        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

}