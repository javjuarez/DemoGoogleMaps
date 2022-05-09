package com.example.demogooglemaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.demogooglemaps.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var marker: Marker

    var latLng = LatLng(0.0, 0.0)
    var etiquetaRestaurante = ""

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        // Trata de acceder a la ubicación del GPS
        enableLocation()
    }

    fun click(view: View) {
        with(binding)
        {
            when (view.id) {
                imageBK.id -> {
                    latLng = LatLng(19.434572303926934, -99.14300387685742)
                    etiquetaRestaurante = "Burger King Plaza Juárez"
                }
                imageDominos.id -> {
                    latLng = LatLng(19.43009038950298, -99.16167399668625)
                    etiquetaRestaurante = "Domino's Pizza Reforma 222"
                }
                imageSushiRoll.id -> {
                    latLng = LatLng(19.449473580234457, -99.15187095332271)
                    etiquetaRestaurante = "Sushi Roll Forum Buenavista"
                }
                imageItalianis.id -> {
                    latLng = LatLng(19.40267309338354, -99.15348161191193)
                    etiquetaRestaurante = "Italianis Parque Delta"
                }
                else -> {}
            }
        }
        marker.remove()
        marker = map.addMarker(MarkerOptions().position(latLng).title(etiquetaRestaurante))!!
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
        marker.showInfoWindow()
    }

    private fun createMarker() {
        marker = map.addMarker(
            MarkerOptions()
                .position(LatLng(19.5173482, -99.0330213))
                .title("Marcador")
        )!!
    }

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) map.isMyLocationEnabled = true
        else requestLocationPermission()
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        if (ActivityCompat
                .shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
        ) {
            // Mostrar ventana de permisos
        } else {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                } else {
                    Toast.makeText(this, "Ve a ajustes para activar el permiso", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {}
        }
    }
}