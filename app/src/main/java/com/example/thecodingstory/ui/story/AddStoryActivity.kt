package com.example.thecodingstory.ui.story

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.thecodingstory.api.config.ApiConfig
import com.example.thecodingstory.api.response.FileUploadResponse
import com.example.thecodingstory.database.UserPreference
import com.example.thecodingstory.databinding.ActivityAddStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null

    private fun setProgressBar(state: Boolean){
        binding.progressAdd.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProgressBar(false)

        userPreference = UserPreference(this)

        supportActionBar?.title = "Make Story"

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            uploadStory()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    private fun uploadStory() {
        setProgressBar(true)
        val descriptionText = binding.itemDescription.text.toString().trim()

        if (getFile != null && descriptionText.isNotEmpty()) {

            val file = reduceFileImage(getFile as File)

            val description = descriptionText.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val token = userPreference.getToken()!!
            val apiService = ApiConfig.getApiService(token)
            val uploadImageRequest = apiService.uploadImage(imageMultipart, description)
            uploadImageRequest.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@AddStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()

                            val moveIntent = Intent(this@AddStoryActivity, StoryActivity::class.java)
                            startActivity(moveIntent)
                            setProgressBar(false)
                            finish()
                        }
                    } else {
                        setProgressBar(false)
                        Toast.makeText(this@AddStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    setProgressBar(false)
                    Toast.makeText(this@AddStoryActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })

        } else if (descriptionText.isEmpty()){
            setProgressBar(false)
            Toast.makeText(this@AddStoryActivity, "Silakan tulis deskripsi terlebih dahulu.", Toast.LENGTH_SHORT).show()
        } else {
            setProgressBar(false)
            Toast.makeText(this@AddStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }
}
