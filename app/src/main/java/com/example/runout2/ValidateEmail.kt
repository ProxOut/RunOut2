package com.example.runout2

import android.util.Patterns

class ValidateEmail {

    // Companion Object que contiene un método estático isValidEmail
    companion object {

        // Método estático isValidEmail que verifica si una cadena es un correo electrónico válido
        fun isValidEmail(email: CharSequence): Boolean {
            // Utiliza la expresión regular predefinida para correos electrónicos en Patterns.EMAIL_ADDRESS
            // y compara con el correo electrónico proporcionado
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

}