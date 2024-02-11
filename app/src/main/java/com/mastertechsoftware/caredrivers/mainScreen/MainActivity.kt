package com.mastertechsoftware.caredrivers.mainScreen

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastertechsoftware.caredrivers.R
import com.mastertechsoftware.caredrivers.data.models.Trip
import com.mastertechsoftware.caredrivers.databinding.MainLayoutBinding
import com.mastertechsoftware.caredrivers.databinding.RecyclerLayoutBinding
import com.mastertechsoftware.caredrivers.details.TripDetailsActivity
import kotlinx.serialization.json.Json

/**
 * This is the starting point of the App
 */
class MainActivity : ComponentActivity() {
    private lateinit var binding: MainLayoutBinding
    private val viewModel: MainViewModel = MainViewModel()
    private lateinit var tripsAdapter: TripsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This is to just show the hamburger menu
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.syncState()

        // Make the icon white
        val icon: Drawable? = binding.toolbar.navigationIcon
        icon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        if (icon != null) {
            binding.toolbar.setNavigationIcon(icon)
        }
        // For now, disable the navigation icon as it does nothing
        binding.toolbar.setNavigationOnClickListener {

        }

        addProgress()
        // Listen for the event of the list of trips
        viewModel.currentTripList.observe(this) { tripList ->
            tripList?.let {
                tripsAdapter = TripsAdapter(it) { trip ->
                    startDetails(trip)
                }
                addMainView()
            }
        }
        // Load the trip list
        viewModel.loadTripList()
    }

    /**
     * Add Progress indicator
     */
    private fun addProgress() {
        val progressBar = ProgressBar(this)
        // Set layout parameters
        val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = android.view.Gravity.CENTER
        progressBar.layoutParams = layoutParams
        binding.container.addView(progressBar)
    }

    private fun addMainView() {
        binding.container.removeAllViews()
        // Add a Recycler view
        val recyclerLayoutBinding = RecyclerLayoutBinding.inflate(layoutInflater)
        binding.container.addView(recyclerLayoutBinding.root)
        recyclerLayoutBinding.trips.also {
            it.layoutManager = LinearLayoutManager(this)
        }
        recyclerLayoutBinding.trips.adapter = tripsAdapter
    }

    private fun startDetails(trip: Trip) {
        // Start the details activity
        val intent = Intent(this, TripDetailsActivity::class.java)
        intent.putExtra("Trip", Json.encodeToString(serializer = Trip.serializer(), trip))
        startActivity(intent)
    }


}

