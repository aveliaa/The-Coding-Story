package com.example.thecodingstory.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.thecodingstory.R
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val TAG = "Register Activity"
    }

    private fun setProgressBar(state: Boolean){
        val progress: ProgressBar = findViewById(R.id.progress_register)
        progress.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)
        setProgressBar(false)

        val registerButton: Button = findViewById(R.id.register_button)
        registerButton.setOnClickListener(this)
    }

    private fun register(){

        val name = findViewById<EditText>(R.id.user_username).text.toString().trim()
        val email = findViewById<EditText>(R.id.user_email).text.toString().trim()
        val password = findViewById<EditText>(R.id.user_password).text.toString().trim()

        val client = ApiConfig.getApiService().postRegister(name, email, password)

        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        val registerMessage = responseBody.message
                        val toast = Toast.makeText(applicationContext,registerMessage,Toast.LENGTH_SHORT)
                        toast.show()

                        val moveIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        setProgressBar(false)
                        startActivity(moveIntent)
                        finish()
                    }

                } else {
                    val message = response.message()
                    Log.e(TAG, "onFailure (Successfull response) : $message")
                    val toast = Toast.makeText(applicationContext,"Invalid Input",Toast.LENGTH_SHORT)
                    toast.show()
                    setProgressBar(false)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                val message = t.message
                Log.e(TAG, "onFailure (Failed response) : $message")
                val toast = Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT)
                toast.show()
                setProgressBar(false)
            }



        })
    }



    override fun onClick(v: View?) {
        setProgressBar(true)
        register()
    }
}