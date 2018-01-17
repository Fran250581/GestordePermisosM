package com.example.fran.gestordepermisosm

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private val PERMISOS_INICIALES = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    private val PERMISOS_CAMARA = arrayOf<String>(Manifest.permission.CAMERA)
    private val PERMISOS_CONTACTOS = arrayOf<String>(Manifest.permission.READ_CONTACTS)
    private val PERMISOS_LOCALIZACION = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
    private val PETICION_INICIAL = 123
    private val PETICION_CAMARA = PETICION_INICIAL + 1
    private val PETICION_CONTACTOS = PETICION_INICIAL + 2
    private val PETICION_LOCALIZACION = PETICION_INICIAL + 3

    private var localizacion: TextView? = null
    private var camara: TextView? = null
    private var internet: TextView? = null
    private var contactos: TextView? = null
    private var almacenamiento: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localizacion = findViewById(R.id.location_value) as TextView
        camara = findViewById(R.id.camera_value) as TextView
        internet = findViewById(R.id.internet_value) as TextView
        contactos = findViewById(R.id.contacts_value) as TextView
        almacenamiento = findViewById(R.id.storage_value) as TextView

        if (!hayPermisoLocalizacion() || !hayPermisoContactos()) {
            ActivityCompat.requestPermissions(this, PERMISOS_INICIALES, PETICION_INICIAL);
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarTabla()
    }

    private fun hayPermiso(perm: String): Boolean {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
    }

    private fun hayPermisoLocalizacion(): Boolean {
        return hayPermiso(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun hayPermisoCamara(): Boolean {
        return hayPermiso(Manifest.permission.CAMERA)
    }

    private fun hayPermisoContactos(): Boolean {
        return hayPermiso(Manifest.permission.READ_CONTACTS)
    }

    private fun actualizarTabla() {
        localizacion!!.setText(hayPermisoLocalizacion().toString())
        camara!!.setText(hayPermisoCamara().toString())
        internet!!.setText(hayPermiso(Manifest.permission.INTERNET).toString())
        contactos!!.setText(hayPermisoContactos().toString())
        almacenamiento!!.setText(hayPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())
    }

    private fun error() {
        Toast.makeText(this, R.string.toast_error, Toast.LENGTH_LONG).show()
    }

    private fun accionCamara() {
        Toast.makeText(this, R.string.toast_camara, Toast.LENGTH_SHORT).show()
    }

    private fun accionContactos() {
        Toast.makeText(this, R.string.toast_contactos, Toast.LENGTH_SHORT).show()
    }

    private fun accionLocalizacion() {
        Toast.makeText(this, R.string.toast_localizacion, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.camera -> {
                if (hayPermisoCamara()) {
                    accionCamara()
                } else {
                    ActivityCompat.requestPermissions(this, PERMISOS_CAMARA, PETICION_CAMARA)
                }
                return true
            }

            R.id.contacts -> {
                if (hayPermisoContactos()) {
                    accionContactos()
                } else {
                    ActivityCompat.requestPermissions(this, PERMISOS_CONTACTOS, PETICION_CONTACTOS)
                }
                return true
            }

            R.id.location -> {
                if (hayPermisoLocalizacion()) {
                    accionLocalizacion()
                } else {
                    ActivityCompat.requestPermissions(this, PERMISOS_LOCALIZACION, PETICION_LOCALIZACION)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        actualizarTabla()

        when (requestCode) {
            PETICION_CAMARA -> if (hayPermisoCamara()) {
                accionCamara()
            } else {
                error()
            }

            PETICION_CONTACTOS -> if (hayPermisoContactos()) {
                accionContactos()
            } else {
                error()
            }

            PETICION_LOCALIZACION -> if (hayPermisoLocalizacion()) {
                accionLocalizacion()
            } else {
                error()
            }
        }
    }

}
