package com.mastertechsoftware.caredrivers.details

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.mastertechsoftware.caredrivers.R
import com.mastertechsoftware.caredrivers.data.models.Trip
import com.mastertechsoftware.caredrivers.databinding.ActivityTripDetailsBinding
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel
import com.mastertechsoftware.caredrivers.mainScreen.MainViewModel.Companion.convertMetersToMiles
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException

/**
 * Handle Trip Details. Trip is passed as part of a serialized intent extra
 */
class TripDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityTripDetailsBinding
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the trip from the serialized extra
        val tripString = intent.getStringExtra("Trip")
        if (tripString != null) {
            trip = Json.decodeFromString(
                    deserializer = Trip.serializer(),
                    string = tripString
            )
        }
        binding = ActivityTripDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_activity_trip_details)
        val backArrow = ContextCompat.getDrawable(this, R.drawable.back_button)
        if (backArrow != null) {
            backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            supportActionBar!!.setHomeAsUpIndicator(backArrow)
        }
        binding.cancelButton.setOnClickListener {
            val additionalButtons = mutableListOf<AdditionalButtonInfo>()
/*
                            // Umcomment to show additional buttons
            for (i in 0..5) {
                additionalButtons.add(AdditionalButtonInfo(R.string.dialog_title,
                        R.style.DialogConfirmationButton,
                        buttonClickListener = {
                            Timber.e("Got Button click")
                        }
                ),)
            }
*/
            showConfirmationDialog(
                    this,
                    DialogConfig(
                            R.string.dialog_title,
                            R.string.dialog_text,
                            R.string.dialog_ok,
                            R.string.dialog_cancel,
                            okButtonClickListener = {

                            },
                            cancelButtonClickListener = {

                            },
                            additionalButtons = additionalButtons
                    )

            )
        }
        trip?.let { trip ->
            try {
                // Format the start date
                binding.rideDate.text = MainViewModel.dayMonthFormatter.format(trip.plannedRoute.startsAt)
                // Get the start date and bold it
                val startEndTime = binding.root.context.getString(
                        R.string.time_range,
                        MainViewModel.convertAMPM(MainViewModel.timeFormatter.format(trip.plannedRoute.startsAt)),
                        MainViewModel.convertAMPM(MainViewModel.timeFormatter.format(trip.plannedRoute.endsAt))
                )
                val boldText = SpannableString(startEndTime)
                boldText.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.rideTime.setText(boldText, TextView.BufferType.SPANNABLE)
            } catch (e: ParseException) {
                Timber.e(e, "Problems parsing")
            }
            // Estimated Earnings
            binding.estimatedAmount.text =
                    NumberFormat.getCurrencyInstance().format(trip.estimatedEarnings)
            // Show/Hide series
            binding.seriesText.visibility = if (trip.inSeries == true) View.VISIBLE else View.GONE
            binding.waypoints.also {
                it.layoutManager = LinearLayoutManager(binding.root.context)
                it.adapter = DetailsAdapter(trip)
            }
            // Trip description with meters to miles conversion
            binding.tripInfo.text = getString(
                    R.string.trip_description,
                    trip.slug,
                    convertMetersToMiles(trip.plannedRoute.totalDistance),
                    trip.plannedRoute.totalTime
            )
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the up button click event here
                onBackPressed() // Or any other action
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        trip?.let { trip ->
            // Get the first location
            val location1 = trip.waypoints[0].location
            val firstLatLong = LatLng(location1.lat, location1.lng)
            // Turn the drawables into bitmaps
            val redBitmap =
                    getBitmapDescriptorFromShape(this.application.applicationContext, R.drawable.red_circle)
            val greenBitmap =
                    getBitmapDescriptorFromShape(this.application.applicationContext, R.drawable.green_circle)

            // Create a marker from the location & icon & title
            val markerOptions = MarkerOptions()
                    .position(firstLatLong)
                    .icon(greenBitmap)
                    .title(location1.streetAddress)
            googleMap.addMarker(markerOptions)
            var secondLocation: LatLng? = null
            // Get the 2nd/ending location
            if (trip.waypoints.size > 1) {
                val location2 = trip.waypoints[trip.waypoints.size - 1].location
                secondLocation = LatLng(location2.lat, location2.lng)
                val options = MarkerOptions()
                        .position(secondLocation)
                        .icon(redBitmap)
                        .title(location2.streetAddress)
                googleMap.addMarker(options)
            }
            // TODO: Calculate proper zoom level
            val cameraPosition = CameraPosition.Builder()
                    .target(firstLatLong)
                    .zoom(13f)
                    .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            // Since we don't have the proper zoom level, enable the controls so we can zoom in/out
            googleMap.uiSettings.isZoomControlsEnabled = true
        }
    }

    /**
     * Create a Bitmap from a Drawable
     * @param Drawable
     * @return Bitmap
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * Create a BitmapDescriptor from a drawable
     * @param context
     * @param shapeDrawableResId - Id of Drawable
     */
    private fun getBitmapDescriptorFromShape(context: Context?, shapeDrawableResId: Int): BitmapDescriptor {
        val shapeDrawable = ContextCompat.getDrawable(context!!, shapeDrawableResId)
        val bitmap = drawableToBitmap(shapeDrawable!!)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}