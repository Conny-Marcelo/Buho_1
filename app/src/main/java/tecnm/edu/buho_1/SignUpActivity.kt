package tecnm.edu.buho_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.databinding.ActivitySignUpBinding
import tecnm.edu.buho_1.user.CheckEmailActivity
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnGoBack: ImageButton
    private lateinit var btnCheckBox: CheckBox

    private lateinit var conditions: Button

    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var txtConfPass: EditText

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Para enlazar los elementos de los layouts con las clases
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instancia de Firebase Authentication
        auth = Firebase.auth

        //Referenciando a un elemento en el XML
        btnLogin = findViewById(R.id.sesionIr)
        btnGoBack = findViewById(R.id.back_to)
        btnCheckBox = findViewById(R.id.checkBox)
        txtEmail = findViewById(R.id.txtEmail)
        txtPass = findViewById(R.id.txtPasss)
        txtConfPass = findViewById(R.id.txtConfPaswd)

        conditions = findViewById(R.id.ver_conditions)

        conditions.setOnClickListener {
            val url = "https://sites.google.com/view/politica-privacidad-buho/p%C3%A1gina-principal" // Reemplaza con el enlace deseado
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        //Registrar en la BD
        sign()

        //Regresar a la actividad anterior
        regresar()

        //Ir a la actividad de Inicio de sesión
        irLogin()

    }

    //Regresar a la página anterior
    private fun regresar() {

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    //Ir a la pantalla de Inicio de sesión
    private fun irLogin() {

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    //Crear la cuenta
    private fun createAccount(nicknam: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // val uid =user!!.uid
                    val map = hashMapOf(
                        "nickname" to nicknam,
                        "email" to email,
                        "password" to null
                    )

                    val db = Firebase.firestore

                    db.collection("users").document(email).set(map).addOnSuccessListener {
                        val intent = Intent(this, CheckEmailActivity::class.java)
                        this.startActivity(intent)
                    }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar la informacion",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this, "No se pudo crear la cuenta. Vuelva a intertarlo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //Método que se usa al dar clic en el botón de registrarse
    private fun sign() {

        binding.registrarsebtn.setOnClickListener {

            val mEmail = binding.txtEmail.text.toString()
            val mPassword = binding.txtPasss.text.toString()
            val mRepeatPassword = binding.txtConfPaswd.text.toString()
            val nicknam = binding.txtName.text.toString()

            val passwordRegex = Pattern.compile(
                "(?=.*[-_@#$%^&+=./*()¿?¡!+&=~°<>{};,:])" +     // Al menos 1 carácter especial
                        ".{6,}"                        // Al menos 4 caracteres
            )

            if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(
                    this, "Correo electrónico requerido.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()) {
                Toast.makeText(
                    this, "Contraseña requerida.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mPassword != mRepeatPassword) {
                Toast.makeText(
                    this, "Confirmar contraseña.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (btnCheckBox.isChecked) {
                createAccount(nicknam, mEmail, mPassword)
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Error")
                    setMessage(
                        "Para completar tu registro debes aceptar los Términos de uso " +
                                "y el procesamiento de tus datos conforme a los dispuesto en Políticas de Privacidad"
                    )
                    setPositiveButton("Aceptar", null)
                }.show()

            }

        }
    }


}