package com.smart.app.ui.tutorials

@Suppress("unused")
class TutorialMapsActivity {
}


// Dummy1
/*
class TutorialMapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_reset_password_renew)
    }
}
*/
// Refer and original content below
/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.smart.app.R

class TutorialMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    // private val bottomSheetView by lazy { findViewById<ConstraintLayout>(R.id.bottomSheet) }
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_maps)

        /*
        lifecycleScope.launchWhenCreated {
          val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
          val googleMap: GoogleMap? = mapFragment?.awaitMap()
            }
         */

        // Camera 이벤트 수집

        lifecycleScope.launchWhenCreated {
          googleMap.cameraMoveEvents().collect {
            print("Received camera move event")
          }
        }



//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
//        setBottomSheetVisibility(true)


//        findViewById<FloatingActionButton>(R.id.mytestfb).setOnClickListener {
//            ModalBottomSheetDialogFragment.newInstance()
//                .show(supportFragmentManager, ModalBottomSheetDialogFragment::class.java.canonicalName)
//        }
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap.apply {
//            val seoul = LatLng(37.564, 127.001)
//            addMarker(MarkerOptions().position(seoul).title(""))
//            setOnMarkerClickListener { marker ->
//                onMarkerClicked(marker)
//                false
//            }
//            setOnMapClickListener { setBottomSheetVisibility(false) }
//            moveCamera(CameraUpdateFactory.newLatLng(seoul))
//        }
//    }

    @Suppress("EmptyMethod")
    private fun onMarkerClicked(marker: Marker) {
        // bottomSheetView.findViewById<TextView>(R.id.city_name).text = marker.title
//        bottomSheetView.findViewById<TextView>(R.id.longitude).text = marker.position.longitude.toString()
//        bottomSheetView.findViewById<TextView>(R.id.latitude).text = marker.position.latitude.toString()

//        setBottomSheetVisibility(true)
    }

    @Suppress("EmptyMethod")
    private fun setBottomSheetVisibility(isVisible: Boolean) {
//        val updatedState = if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehavior.state = updatedState
    }
}
 */