package com.example.runout2

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.runout2.LoginActivity.Companion.useremail
import com.example.runout2.Utility.setHeightLinearLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //varibales globales dentro de esta clase
    private lateinit var drawer: DrawerLayout //de tipo drawerlayout porque el activity main esta dentro de una etiqueta drawerlayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creamos una funcion donde vamos a inicializar todas las cuestiones de la toolbar
        initToolBar()
        //funcion para inicializar los diferentes objetos
        initObjects()
        //creamos una funcion donde vamos a inicializar las funciones de la cabecera
        initNavigationView()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        //super.onBackPressed() la cancelamos porque vamos a personalizar nosotros el codigo
        //preguntamos si el menu se encuentra desplegado y si es asi entonces lo vamos a cerrar
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else
            signOut()
    }

    private fun initToolBar(){
        //me creo una variable del tipo del xml para administrar dicho valor
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main )
        setSupportActionBar(toolbar)//es utilizado para configurar una barra de herramientas como la barra de acción de una actividad, lo que te permite integrar y personalizar la barra de acción según tus necesidades

        //en esta variable estoy contralando el elemento raiz del activity main
        drawer = findViewById(R.id.drawer_layout)

        // Crea un ActionBarDrawerToggle para permitir la apertura y cierre del NavigationDrawer
        // La ActionBarDrawerToggle vincula la apertura y el cierre del DrawerLayout con el botón de navegación de la barra de herramientas
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.bar_title, R.string.navigation_drawer_close
        )

        // Añade un DrawerListener al DrawerLayout para detectar eventos de apertura y cierre del NavigationDrawer
        drawer.addDrawerListener(toggle)

        // Sincroniza el estado actual del ActionBarDrawerToggle con el DrawerLayout
        // Esto se hace para asegurarse de que el icono del NavigationDrawer se muestre correctamente
        toggle.syncState()
    }

    private fun initNavigationView(){
        var navigationView: NavigationView = findViewById(R.id.nav_view)

        //le vamos a establecer que a los elementos del menu le podamos poner un listener cuando se le este haciendo click a uno de ellos
        navigationView.setNavigationItemSelectedListener(this)

        //inflamos la cabecera desde la actividad. nav_headrer_main se la inflamos al navegation view

        var headerView: View = LayoutInflater.from(this).inflate(R.layout.nav_header_main,navigationView,false)
         //para cargar datos de ususuario en el header lo borraremos y lo voveremos a poner
        navigationView.removeHeaderView(headerView)
        navigationView.addHeaderView(headerView)

        var tvUser: TextView = headerView.findViewById(R.id.tvUser)
        tvUser.text = useremail


    }

    private fun initObjects(){
        //aqui empezamos a inicializar todos los elementos de la interface

        //inicializamos al padre como al hijo
        var lyMap = findViewById<LinearLayout>(R.id.lyMap)
        var lyFragmentMap = findViewById<LinearLayout>(R.id.lyFragmentMap)


        var lyIntervalModeSpace = findViewById<LinearLayout>(R.id.lyIntervalModeSpace)//padre
        var lyIntervalMode = findViewById<LinearLayout>(R.id.lyIntervalMode) //hijo

        var lyChallengesSpace = findViewById<LinearLayout>(R.id.lyChallengesSpace)
        var lyChallenges = findViewById<LinearLayout>(R.id.lyChallenges)

        var lySettingsVolumesSpace = findViewById<LinearLayout>(R.id.lySettingsVolumesSpace)
        var lySettingsVolumes = findViewById<LinearLayout>(R.id.lySettingsVolumes)



        //vamos a hacer que nuestro padre lymap lo vamos a achicar utilizando la funcion de los utilitys
        //a los padres les damos altura cero
        setHeightLinearLayout(lyMap, 0)
        setHeightLinearLayout(lyIntervalModeSpace, 0)
        setHeightLinearLayout(lyChallengesSpace, 0)
        setHeightLinearLayout(lySettingsVolumesSpace, 0)

        // y a los hijos les damos la translacion vertical para que desaparezcan
        lyFragmentMap.translationY = -300f
        lyIntervalMode.translationY = -300f
        lyChallenges.translationY = -300f
        lySettingsVolumes.translationY = -300f
    }

    // Método para manejar el clic en el botón de cerrar sesión
    fun callSignOut(v: View) {
        signOut() // Llama al método signOut() para cerrar sesión
    }

    // Método privado para cerrar sesión
    private fun signOut() {
        useremail = "" // Borra el correo electrónico del usuario almacenado en la variable estática

        // Cierra la sesión del usuario en Firebase
        FirebaseAuth.getInstance().signOut() //en este punto se sale de la base de datos mas no de la ventana

        // Inicia una nueva actividad de LoginActivity para permitir que el usuario inicie sesión nuevamente
        startActivity(Intent(this, LoginActivity::class.java))
    }

    //aqui indicamos lo que haya que hacer con cada accion del menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        //usamos un when para consultar que es lo que se ha pulsado
        when(item.itemId){
            R.id.nav_item_record -> callRecordActivity()
            R.id.nav_item_signout -> signOut()
        }
        //con esta linea de codigo hacemos que cuando clickeemos en una de ellas el menu se cierre y no quede abierto
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    private fun callRecordActivity(){
        val intent = Intent(this, RecordActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SuspiciousIndentation")
    fun inflateVolumes(v: View){ //estas funciones siempre necesitan recibir este parametro View ya que son publicas y vienen de un objeto al que le estamos dando el onClick sino daria error
         //switch de volumen
        var swVolumes = findViewById<Switch>(R.id.swVolumes)

        //padre e hijo
        val lySettingsVolumesSpace = findViewById<LinearLayout>(R.id.lySettingsVolumesSpace)
        val lySettingsVolumes = findViewById<LinearLayout>(R.id.lySettingsVolumes)

        //preguntamos si esta checkeado el switch
        if (swVolumes.isChecked){
            //entonces desplegamos todo ese bloque de elementos
            //le cambiamos el color a las letras del switch
            ObjectAnimator.ofInt(swVolumes,"textColor", ContextCompat.getColor(this,R.color.orange)).apply { //ponemos int para el color porque el color es tipo int
                duration = 500 //en medio seg se hara la transicion de blanco a amarillo
                start()
            }



        var swIntervalMode = findViewById<Switch>(R.id.swIntervalMode)
        //variable con el valor de la altura que queremos poner
        var value = 400
        if (swIntervalMode.isChecked) value = 600

            setHeightLinearLayout(lySettingsVolumesSpace, value)
            ObjectAnimator.ofFloat( lySettingsVolumes, "translationY", 0f).apply {//seria float porque los linear layouts sus parametros estan en float
                duration = 500
                start()
            }

        }
        else{
            swVolumes.setTextColor(ContextCompat.getColor(this, R.color.white))
            setHeightLinearLayout(lySettingsVolumesSpace, 0)
            lySettingsVolumes.translationY = -300f
        }

    }

    fun startOrStopButtonClicked(v: View){
        //en esta funcion de abajo es de donde haremos la comprobacion de los permisos para despues invocar la funcion mangeRun()
        manageStartStop()
    }

    private fun manageStartStop() {

    }

}
































