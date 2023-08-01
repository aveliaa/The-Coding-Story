package com.example.thecodingstory.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.thecodingstory.R
import com.example.thecodingstory.api.response.ListStoryItem
import com.example.thecodingstory.database.UserPreference
import com.example.thecodingstory.databinding.ActivityStoryMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val mapViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[StoryMapViewModel::class.java]

        val userPreference = UserPreference(this)
        mapViewModel.findRestaurant(userPreference.getToken()!!)

        mapViewModel.stories.observe(this) { stories ->
            setLocation(stories)
        }

    }


    private fun setLocation(stories:List<ListStoryItem>){

        for (story in stories){


            if(story.lat != null){

                val location = LatLng(story.lat as Double,story.lon as Double)
                mMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(story.name)
                        .snippet(cutDescription(story.description))
                )

                boundsBuilder.include(location)


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        }

    }

    private fun cutDescription(input: String):String{
        return if(input.length > 15){
            input.substring(0,14) + "..."
        } else {
            input
        }
    }
}