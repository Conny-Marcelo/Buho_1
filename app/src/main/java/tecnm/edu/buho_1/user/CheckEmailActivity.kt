package tecnm.edu.buho_1.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.user.PrincipalPublicationsActivity
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.SignUpActivity
import tecnm.edu.buho_1.databinding.ActivityCheckEmailBinding

class CheckEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckEmailBinding

    private lateinit var btnGoBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Para enlazar los elementos de los layouts con las clases
        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instancia de Firebase Authentication
        auth = Firebase.auth

        //Regresar a la actividad anterior
        btnGoBack = findViewById(R.id.back_to)
        regresar()

        //Continuar con la creación de la cuenta
        continues()

    }

    //Enviar el correo
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Se ha enviado un correo de verifiación.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //Se manda llamar al metodo de enviar correo y (se accede a la aplicación o no avanza hasta verificar el correo)
    private fun continues() {

        sendEmailVerification()

        val user = auth.currentUser

        binding.continuarbtn.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {
            }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (user.isEmailVerified) {
                            val intent = Intent(this, PrincipalPublicationsActivity::class.java)
                            this.startActivity(intent)
                            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this, "Por favor verifica tu correo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }

    }

    //Regresar a la página anterior
    private fun regresar() {

        btnGoBack.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}