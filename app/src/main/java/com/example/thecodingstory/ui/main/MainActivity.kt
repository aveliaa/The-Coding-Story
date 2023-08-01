package com.example.thecodingstory.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.thecodingstory.R
import com.example.thecodingstory.database.UserPreference
import com.example.thecodingstory.ui.story.StoryActivity
import com.example.thecodingstory.ui.user.LoginActivity

/*
divadiva
tester123
 */
class MainActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        userPreference = UserPreference(this)

        Handler().postDelayed({
            if(userPreference.isLogged()){
                val mainIntent = Intent(this@MainActivity, StoryActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            else{
                val mainIntent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }, 2000)
    }
}