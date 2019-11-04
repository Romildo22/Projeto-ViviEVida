package com.example.pvv.MenuLateral.Mapa

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pvv.Activitys.BaseActivity
import com.example.pvv.MenuLateral.Doacoes.Doacoes
import com.example.pvv.MenuLateral.Mapa.Constants.Constants.ERROR_DIALOG_REQUEST
import com.example.pvv.MenuLateral.Mapa.Constants.Constants.PERMISSIONS_REQUEST_ENABLE_GPS
import com.example.pvv.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class Mapa : BaseActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private var map: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val PERMISSION_CODE= 1000
    private var isFABOpen = false
    private var openFAB = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setUpToolbar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        inicializarComponentes()
        userLocation()
    }
    //---------------------------------------------------------------------------------------------------------------------------
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        mGoogleApiClient!!.connect()

        locais()  }
    //---------------------------------------------------------------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    private fun inicializarComponentes() {
        //Validar permissões
        permissao()
        //Mapa fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        // Configurar o objeto GoogleApiClient
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        //Verificar serviços
        verServicesOK()

        //---------------------------------------------------------------------------------------------------------------------------
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val fab1 = findViewById<FloatingActionButton>(R.id.fab1)
        val fab2 = findViewById<FloatingActionButton>(R.id.fab2)
        val fab3 = findViewById<FloatingActionButton>(R.id.fab3)


        fab1.visibility = View.INVISIBLE
        fab2.visibility = View.INVISIBLE
        fab3.visibility = View.INVISIBLE

        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
                fab1.visibility = View.VISIBLE
                fab2.visibility = View.VISIBLE
                fab3.visibility = View.VISIBLE
            } else {
                closeFABMenu()
                fab1.visibility = View.INVISIBLE
                fab2.visibility = View.INVISIBLE
                fab3.visibility = View.INVISIBLE
            }
        }
        fab1.setOnClickListener {
            if(!openFAB){
            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) //ACTION_LOCATION_SOURCE_SETTINGS
            startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS) //PERMISSIONS_REQUEST_ENABLE_GPS
            fab1.setImageDrawable(getDrawable(R.drawable.ic_location_on))
                Toast.makeText(this,"Ativar localização",Toast.LENGTH_SHORT).show()
            openFAB = true }
            else {
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) //ACTION_LOCATION_SOURCE_SETTINGS
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS) //PERMISSIONS_REQUEST_ENABLE_GPS
                fab1.setImageDrawable(getDrawable(R.drawable.ic_location_off))
                Toast.makeText(this,"Desativar localização", Toast.LENGTH_SHORT).show()
                openFAB = false }
        }
        fab2.setOnClickListener{ locais(); Toast.makeText(this,"Ponto inicial",Toast.LENGTH_SHORT).show() }
        fab3.setOnClickListener {
            val intent = Intent(context, Doacoes::class.java)
            startActivity(intent)
            Toast.makeText(this,"Doações",Toast.LENGTH_SHORT).show() }
    }
    //---------------------------------------------------------------------------------------------------------------------------
    fun permissao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //permission was not enable
                val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE) } } }
    //---------------------------------------------------------------------------------------------------------------------------
    fun verServicesOK(): Boolean {
        Log.d(TAG, "verServicesOK: verificar a versão do google services")
        val validar = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@Mapa)
        if (validar == ConnectionResult.SUCCESS) {
            Log.d(TAG, "verServicesOK: Google play services ok")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(validar)) {
            Log.d(TAG, "verServicesOK: Ocorreu um erro")
            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this@Mapa, validar, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this, "Você não pode acessar o mapa", Toast.LENGTH_SHORT).show()
        }
        return false }
    //---------------------------------------------------------------------------------------------------------------------------
    fun solicit(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        //ativar o gps
                        val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            3000,
                            10f,
                            locationListener)
                    }
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permissao negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        solicit(requestCode, permissions, grantResults);

        //solicitando permissões pela primeira vez
        for (permissaoResultado in grantResults) {
            //permission denied (negada)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //alerta
                alertaValidacaoPermissao()
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        3000,
                        10f,
                        locationListener
                    )
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------
    private fun alertaValidacaoPermissao() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissões Negadas")
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões")
        builder.setNegativeButton("Confirmar") { dialog, which -> finish() }
        builder.setPositiveButton("Ativar Localicação") { dialog, which ->
            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            onMapReady(map)
        }
        val dialog = builder.create()
        dialog.show() }
    //---------------------------------------------------------------------------------------------------------------------------
    override fun onConnected(bundle: Bundle?) {
        Toast.makeText(this, "Conectado ao Google Play services!", Toast.LENGTH_SHORT).show() }
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "Erro ao conectar: $connectionResult", Toast.LENGTH_SHORT).show() }
    override fun onConnectionSuspended(i: Int) {
        Toast.makeText(this, "Conexão Pausada (Conexão interrompida)", Toast.LENGTH_SHORT).show() }
    //---------------------------------------------------------------------------------------------------------------------------
    protected fun locais() {
        val fortaleza = LatLng(-3.768845, -38.516209) //FortalCity
        val local1 = LatLng(-3.747027, -38.471975) // Oficina do Senhor
        val local2 = LatLng(-3.749907, -38.551031) // GAPO
        val local3 = LatLng(-3.797869, -38.518958) // Casa de Apoio Sol Nascente
        val local4 = LatLng(-3.747861, -38.518159) // Lar amigos de Jesus
        val local5 = LatLng(-3.815627, -38.499804) // Hospital do coração de messejana.
        val local6 = LatLng(-3.722326, -38.527351) // Santa Casa da misericordia
        val local7 = LatLng(-3.724502, -38.536152) // Casa de Apoio Lar Nossa senhora de fátima
        val local8 = LatLng(-3.749883, -38.555167) // GEEON - Grupo de Educação e Estudos Oncológicos
        val local9 = LatLng(-3.762948, -38.532375) // Associação Peter Pan
        val local10 = LatLng(-3.739160, -38.499766) // Hospital Militar
        val local11 = LatLng(-3.747362, -38.553534) // Instituto do cancer do ceara

        //verifica se foram dadas as permissões de acesso
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10f,
                locationListener
            )
            map!!.addMarker(
                (MarkerOptions().position(local1).title("Oficina do Senhor").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                ))
            )
            map!!.addMarker(
                MarkerOptions().position(local2).title("Grupo de Apoio ao Paciente Onco-Hematológico").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local3).title("Casa de Apoio Sol Nascente").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local4).title("Lar amigos de Jesus").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local5).title("Hospital do coração de Messejana").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local6).title("Santa casa da misericórdia").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local7).title("Lar nossa senhora de fátima").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local8).title("Grupo de educação e estudos oncológicos").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local9).title("Associação Peter Pan").icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local10).title("Hospital militar de fortaleza").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )
            map!!.addMarker(
                MarkerOptions().position(local11).title("Instituto do câncer do ceará").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            )

            val cameraPosition = CameraPosition.Builder().target(fortaleza).zoom(12f).bearing(0f).build()
            val update = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map!!.animateCamera(update, object : GoogleMap.CancelableCallback {
                override fun onFinish() {}

                override fun onCancel() {}
            })
        }
        map!!.isMyLocationEnabled = true
    }
    //---------------------------------------------------------------------------------------------------------------------------
    fun userLocation() {
        //obj responsavel por gerenciar a localização do user
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {}

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_60))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_110))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_160)) }
    //---------------------------------------------------------------------------------------------------------------------------
    private fun closeFABMenu() {
        isFABOpen = false
        fab1.animate().translationY(0F)
        fab2.animate().translationY(0F)
        fab3.animate().translationY(0F) }
    //---------------------------------------------------------------------------------------------------------------------------
    override fun onStart() {
        super.onStart()
        //mGoogleApiClient!!.connect()
        userLocation() }
    //---------------------------------------------------------------------------------------------------------------------------
    override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect() }
    //---------------------------------------------------------------------------------------------------------------------------
    override fun onBackPressed() {
        finish() }
}