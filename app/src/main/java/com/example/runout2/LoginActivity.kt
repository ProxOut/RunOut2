package com.example.runout2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    //preparamos las variables a necesitar

    companion object { // Sirve para definir miembros estáticos dentro de una clase, es decir, métodos o propiedades que pueden ser accedidos directamente desde la clase sin necesidad de instanciar un objeto de esa clase.
        lateinit var useremail: String //lateinit se utiliza para indicar que las variables no se inicializarán de inmediato, sino que se inicializarán más tarde en el código.
        lateinit var providerSession: String
    }

    private var email by Delegates.notNull<String>() //delegate.notnull nos aseguramos de que ello no sea nulo
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var lyTerms: LinearLayout

    //variable para la autenticacion de la base de datos
    private lateinit var mAuth: FirebaseAuth

    private val getResult=registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
    ){result->

        if(result.resultCode==Activity.RESULT_OK){

            try{
                val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account=task.getResult(ApiException::class.java)!!

                if(account!=null){
                    email=account.email!!
                    val credential=GoogleAuthProvider.getCredential(account.idToken,null)

                    mAuth.signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful)goHome(email,"Google")
                        else Toast.makeText(
                            this,
                            "Error en la conexión con Google",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }catch(e:ApiException){
                Toast.makeText(this,"Error en la conexión con Google",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //ocultamos el linear layout de terminos y condiciones

        lyTerms = findViewById(R.id.lyTerms)
        lyTerms.visibility = View.INVISIBLE

        //asiganamos las variables que hemos creado a los elementos de nuestro activity

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        //llamamos a la funcion .getInstance. tendremos una instancia con la cual operar
        mAuth = FirebaseAuth.getInstance()

        //parte para  confirmar que el email esta bien escrito para poder dejar seguir al usuario.
        manageButtonLogin()
        etEmail.doOnTextChanged { text, start, before, count -> manageButtonLogin() }//cda vez que cambie el texto en el edittext del email vamos a llamar a la funcion de arriba para verificar si con ese cambio le podemos click al botn de inicio de sesion
        etPassword.doOnTextChanged { text, start, before, count -> manageButtonLogin() }//Poner las conciones que yo quiera para la contraseña
    }

    //creamos esto para preguntar de que si ya tenemos un usuario creado dirigirlo directamente al home
    public override fun onStart() {
        super.onStart()

        var currentUser =
            FirebaseAuth.getInstance().currentUser //si hay usuario lo dirije directamente a home
        if (currentUser != null) goHome(currentUser.email.toString(), currentUser.providerId)

    }

    //esta funcion es para cuando se presione el boton de retroceso no se salga de la app si no que vaya a la pagina de inicio
    override fun onBackPressed() {
        super.onBackPressed()
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    private fun manageButtonLogin() {
        var tvLogin =
            findViewById<TextView>(R.id.tvLogin) //posibilidad para que el boton de inicio de sesion pueda ser activado o desactivado
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        if (TextUtils.isEmpty(password) || ValidateEmail.isValidEmail(email) == false) {

            tvLogin.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.gray
                )
            ) //cambia el boton a gris cuando este desactivado
            tvLogin.isEnabled = false
        } else {
            tvLogin.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.green
                )
            ) //cambia el boton a gris cuando este desactivado
            tvLogin.isEnabled = true
        }

    }

    //funciones para poder iniciar sesion
    fun login(view: View) { //funcion publica donde llamamos a la funcion privada donde estara el codigo
        loginUser()
    }

    private fun loginUser() {//funcion privada
        //vamos a darle valores a las cadenas de texto que no pueden ser nulas
        //asignamos a las cadenas el valor que tengan los edit text correspondientes
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        //codigo principal de inicio de sesion
        mAuth.signInWithEmailAndPassword(email, password)//les pasamos los dos parametros
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) goHome(
                    email,
                    "email"
                ) //si esta bien lo mandamos a la pantalla principal
                else { //si no
                    if (lyTerms.visibility == View.INVISIBLE) lyTerms.visibility =
                        View.VISIBLE //hacemos visibles las condiciones para que se registre
                    else { //si no esta invisible, cuando este visible
                        var cbAcept =
                            findViewById<CheckBox>(R.id.cbAcept) //hacemos que acepte los terminos
                        if (cbAcept.isChecked) register() //llamamos a la funcion register cuando este marcado
                    }
                }
            }


    }

    //funcion para que se dirija a la pantalla principal
    private fun goHome(email: String, provider: String) { //se necesitan estos parametros para acceder a la pantalla principal

        useremail = email
        providerSession = provider

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    //funcion pra registrar al usuario
    private fun register() {
        email =
            etEmail.text.toString()//no necesitamos ningun parametro porque esos valores los capturamos de los edit text
        password = etPassword.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email,
            password
        )//creamos un usuario de acuerdo a estos valores para guardarlo en la base de datos
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    //vamos a guardar datos en la base de datos
                    //fecha de registro accediendo a la fecha de hoy
                    var dateRegister = SimpleDateFormat("dd/mm/yyyy").format(Date())
                    //instancia que nos permite acceder a la base de datos
                    var dbRegister = FirebaseFirestore.getInstance()
                    //y utilizando esta variable puedo acceder a las colecciones
                    dbRegister.collection("users").document(email).set(
                        hashMapOf(
                            "user" to email,
                            "dateRegister" to dateRegister
                        )
                    )

                    goHome(email, "email")
                } else {
                    Toast.makeText(this, "oh no algo ha salido mal :(", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun goTerms(v: View) {
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)

    }

    fun forgotPassword(v: View) {
        //startActivity(Intent(this,ForgotPasswordActivity::class)) //esto es lo mismo que tener lo de arriba pero simplificado
        resetPassword()

    }

    private fun resetPassword() {
        //guardamos el email en una variable
        var e = etEmail.text.toString()
        if (!TextUtils.isEmpty(e)) { //con  la libreria TextUtils y su carcateristica preguntamos si la variable de arriba tiene algo
            mAuth.sendPasswordResetEmail(e) //si tiene algo le mandamos un email de reseteo
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email enaviado a $e", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "No se encontró un usuario con ese correo :(",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

        } else Toast.makeText(this, "Indica un Email ", Toast.LENGTH_SHORT).show()
    }

    //hacemos la llamada a la funcion que me va a servir para hacer el llamado de inicio de sesion con google
    fun callSignInGoogle(view:View){
        signInGoogle()
    }

    private fun signInGoogle(){
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient=GoogleSignIn.getClient(this,gso)
        googleSignInClient.signOut()

        val signInIntent=googleSignInClient.signInIntent
        getResult.launch(signInIntent)

    }

}








































