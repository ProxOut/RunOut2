package com.example.runout2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

         // Encuentra la barra de herramientas (Toolbar) en el diseño y la asigna a una variable
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_terms)

        // Configura la barra de herramientas como la barra de acción de la actividad
        setSupportActionBar(toolbar)

        // Establece el título de la barra de herramientas usando una cadena de recursos
        toolbar.title= getString(R.string.bar_title_terms)
    }
}