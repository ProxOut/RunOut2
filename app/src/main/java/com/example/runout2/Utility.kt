package com.example.runout2

import android.widget.LinearLayout

object Utility {

    // Función para establecer la altura de un LinearLayout
    // @param ly El LinearLayout al que se le va a establecer la altura
    // @param value El valor de la altura que se quiere establecer
    fun setHeightLinearLayout(ly: LinearLayout, value: Int){

        // Obtener los parámetros de diseño del LinearLayout
        val params: LinearLayout.LayoutParams = ly.layoutParams as LinearLayout.LayoutParams

        // Establecer la altura de los parámetros de diseño con el valor proporcionado
        params.height = value

        // Actualizar los parámetros de diseño del LinearLayout con los nuevos valores
        ly.layoutParams = params
    }
}