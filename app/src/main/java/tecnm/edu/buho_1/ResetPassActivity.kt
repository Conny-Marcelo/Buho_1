package tecnm.edu.buho_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.databinding.ActivityResetPassBinding

class ResetPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Para enlazar los elementos de los layouts con las clases
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Enviar correo
        resetPasswd()

        //Regresar a la página anterior
        regresar()


    }

    //metodo para enviar el correo de reseteo de pswd
    private fun resetPasswd() {
        binding.enviar.setOnClickListener {
            val email = binding.recoveryPassword.text.toString()

            Firebase.auth.sendPasswordResetEmail(email)

                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Correo enviado exitosamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Esa dirección no es válida, no está asociada con una cuenta de usuario verificada.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    //Método para regresar a la página anterior
    private fun regresar() {
        binding.back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}