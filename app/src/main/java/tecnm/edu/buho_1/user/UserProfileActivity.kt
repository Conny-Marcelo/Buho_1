package tecnm.edu.buho_1.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.databinding.ActivityPerfilUsuarioBinding

@Suppress("DEPRECATION")
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilUsuarioBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var omitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Para enlazar los elementos de los layouts con las clases
        binding = ActivityPerfilUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instancia de Firebase Authentication
        auth = Firebase.auth

        //Actualizar Nickname
        updateUI()

        //Botón para que se guarden los cambios
        binding.updateProfileButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()

            updateProfile(name)

            val intent = Intent(this, PrincipalPublicationsActivity::class.java)
            this.startActivity(intent)

        }

        //Botón para omitir el cambio de nickname o guardado en la BD
        omitBtn = findViewById(R.id.omitButton)
        omit()

    }

    //Guardar los datos del usuario en la base de datos
    private fun updateProfile(name: String) {

        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val map = hashMapOf(
                        "nickname" to name,
                        "email" to user.email,
                        "password" to null
                    )

                    val db = Firebase.firestore

                    db.collection("users").document(user.email.toString()).set(map)
                        .addOnSuccessListener {

                            // Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Falló al guardar la información.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    Toast.makeText(
                        this, "Cambios guardados.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                }
            }


    }

    //Método para actualizar Nickname cuando se inicia con Google
    private fun updateUI() {
        val user = auth.currentUser

        if (user != null) {
            binding.emailTextView.text = user.email

            if (user.displayName != null) {
                binding.nameTextView.text = user.displayName
                binding.nameEditText.setText(user.displayName)
            }
        }

    }

    //Método para ir a la actividad principal publicaciones
    private fun omit() {
        omitBtn.setOnClickListener {
            val intent = Intent(this, PrincipalPublicationsActivity::class.java)
            this.startActivity(intent)
        }
    }


}