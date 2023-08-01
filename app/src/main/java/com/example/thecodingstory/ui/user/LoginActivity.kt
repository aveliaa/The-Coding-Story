package com.example.thecodingstory.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.thecodingstory.R
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.api.response.LoginResponse
import com.example.thecodingstory.database.UserPreference
import com.example.thecodingstory.ui.story.StoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userPreference: UserPreference

    companion object{
        private const val TAG = "Login Activity"
    }

    private fun setProgressBar(state: Boolean){
        val progress: ProgressBar = findViewById(R.id.progress_login)
        progress.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        setProgressBar(false)

        val moveToRegisterActivity: TextView = findViewById(R.id.item_register_redirect)
        moveToRegisterActivity.setOnClickListener(this)

        val loginButton: Button = findViewById(R.id.login_button)
        loginButton.setOnClickListener(this)

        userPreference = UserPreference(this)
    }

    private fun login(){

        val email = findViewById<EditText>(R.id.user_email).text.toString().trim()
        val password = findViewById<EditText>(R.id.user_password).text.toString().trim()

        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null ) {

                        val userData = responseBody.loginResult

                        val toast = Toast.makeText(applicationContext,"Login Success",Toast.LENGTH_SHORT)
                        toast.show()

                        userPreference.setSession(userData.token,userData.userId,userData.name)

                        val moveIntent = Intent(this@LoginActivity, StoryActivity::class.java)
                        setProgressBar(false)
                        startActivity(moveIntent)
                        finish()
                    }
                } else {
                    setProgressBar(false)
                    val message = response.message()
                    val toast = Toast.makeText(applicationContext,"Can Not Find User", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val message = t.message
                val toast = Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT)
                toast.show()
            }
        })
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.item_register_redirect -> {
                val moveIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.login_button -> {
                setProgressBar(true)
                login()
            }
        }

    }


}