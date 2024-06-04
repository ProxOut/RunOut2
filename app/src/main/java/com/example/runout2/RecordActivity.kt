package com.example.runout2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        //creamos una variable con la que administrar el toolbar y ponemos las variables que queramos nosotros
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_record )
        setSupportActionBar(toolbar)

        //cambiar el titulo del toolbar desde java
        toolbar.title = getString(R.string.bar_title_record)

        //administrar el text color y background color de la toolbar
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.gray_dark))
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_light))

        //funcionalidades que permiten al boton poder desplazarce al HOME
        //crea un botoncito en la superior izquierda de ir atras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    // Esta función se llama cuando se presiona el botón de navegación de soporte (generalmente un botón de flecha para regresar).
    override fun onSupportNavigateUp(): Boolean {
        // Llama a la función onBackPressed() que maneja la acción de ir hacia atrás en la actividad.
        onBackPressed()
        // Devuelve el valor devuelto por la llamada a la función onSupportNavigateUp() de la superclase.
        return super.onSupportNavigateUp()
    }

    // Esta función se llama para inflar el menú de opciones en la barra de herramientas.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla (crea) el menú de opciones utilizando el archivo XML 'order_records_file' y lo agrega al menú proporcionado.
        menuInflater.inflate(R.menu.order_records_file, menu)
        // Devuelve true para indicar que el menú se ha inflado correctamente.
        return true //super.onCreateOptionsMenu(menu) --> Este comentario indica que la línea anterior es la implementación original que se está sobrescribiendo.
    }

    //aqui vamos a capturar que es lo que el usuario a pulsado mediante este parametro "(item: MenuItem)"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //creamos un when para pregunatar que tipo de id es el que se ha pulsado.
        when(item.itemId){
            R.id.orderby_date -> {//preguntamos
                //si el titulo que tiene es igual a la frase orderby_dateZA entonces
                if(item.title == getString(R.string.orderby_dateZA)){
                    //Le cambiamo y le ponemos la otra
                    item.title = getString(R.string.orderby_dateAZ)
                }else{
                    item.title = getString(R.string.orderby_dateZA)
                }
                return true //para no seguir actualizando el flujo de instrucciones
            }

            R.id.orderby_duration->{
                var option = getString(R.string.orderby_durationZA)
                if(item.title==getString(R.string.orderby_durationZA)){

                    item.title=getString(R.string.orderby_durationAZ)
                }else{
                    item.title=getString(R.string.orderby_durationZA)
                }
                return true
            }

            R.id.orderby_distance->{
                var option = getString(R.string.orderby_distanceZA)
                if(item.title==getString(R.string.orderby_distanceZA)){

                    item.title=getString(R.string.orderby_distanceAZ)
                }else{
                    item.title=getString(R.string.orderby_distanceZA)
                }
                return true
            }

            R.id.orderby_avgspeedZA->{
                var option = getString(R.string.orderby_avgspeedZA)
                if(item.title==getString(R.string.orderby_avgspeedZA)){

                    item.title=getString(R.string.orderby_avgspeedAZ)
                }else{
                    item.title=getString(R.string.orderby_avgspeedZA)
                }
                return true
            }
            R.id.orderby_maxpeed->{
                var option = getString(R.string.orderby_maxspeedZA)
                if(item.title==getString(R.string.orderby_maxspeedZA)){

                    item.title=getString(R.string.orderby_maxspeedAZ)
                }else{
                    item.title=getString(R.string.orderby_maxspeedZA)
                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}