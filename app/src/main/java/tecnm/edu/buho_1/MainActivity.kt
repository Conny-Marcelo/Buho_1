package tecnm.edu.buho_1


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.user.PrincipalPublicationsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var btnRegistrarse: Button
    private lateinit var btnIngresar: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //Iniciar con tema de Splash Screen
        setTheme(R.style.Theme_Buho_1)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null){
            // El usuario ya ha iniciado sesión, inicia la actividad deseada
            val intent = Intent(this, PrincipalPublicationsActivity::class.java)
            startActivity(intent)
            finish() // Esto asegura que el usuario no pueda volver atrás a MainActivity
        }


        //Enlazar un elemento con la vista
        btnRegistrarse = findViewById(R.id.registrarbtn)
        btnIngresar = findViewById(R.id.loginbtn)

        //Ingresar
        login()

        //Registrarse
        signUp()


    }


    //Método para para ia a la actividad de login
    private fun login() {
        btnIngresar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    //Método para ir a la actividad de registrarse
    private fun signUp() {
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

}