package tecnm.edu.buho_1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.admin.PrincipalAdmin
import tecnm.edu.buho_1.databinding.ActivityLoginBinding
import tecnm.edu.buho_1.user.UserProfileActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btnSignUp: Button
    private lateinit var btnForgetPass: Button
    private lateinit var btnGoBack: ImageButton
    private lateinit var btnGoogle: Button

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ////Para enlazar los elementos de los layouts con las clases
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginButton.setOnClickListener {

            val mEmail = binding.txtCorreo1.text.toString()
            val mPassword = binding.txtPassword1.text.toString()
            when {
                mPassword.isEmpty() || mEmail.isEmpty() -> {
                    Toast.makeText(
                        this, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    signIn(mEmail, mPassword)
                    // después de que el usuario ha iniciado sesión con éxito

                }
            }
        }

        //boton Google
        btnGoogle = findViewById(R.id.btnGoogle)
        btnGoogle.setOnClickListener {
            signInn()
        }
        //firebase google
        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
        //Firebase Termina google
        btnSignUp = findViewById(R.id.signupButton)
        btnForgetPass = findViewById(R.id.forgetButton)
        btnGoBack = findViewById(R.id.back_to)

        forgetPass()
        registrarse()
        regresar()

    }

    //Ir a la actividad de olvidar Contraseña
    private fun forgetPass() {
        btnForgetPass.setOnClickListener {
            val intent = Intent(this, ResetPassActivity::class.java)
            startActivity(intent)
        }
    }

    //Ir a la actividad de Registrarse
    private fun registrarse() {
        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //Regresar a la página anterior
    private fun regresar() {

        btnGoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null)
            updateUI(currentUser)
    }

    // [END on_start_check_user]
    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    // [END onactivityresult]
    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    // [END auth_with_google]
    // [START signin]
    private fun signInn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // [END signin]

    private fun updateUI(user: FirebaseUser?) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    //verificar si es usuario o admin
    private fun signIn(email: String, password: String) {
        if (email == "babuhoapp1@gmail.com" && password == "Buho2023.") {
            val intent = Intent(this, PrincipalAdmin::class.java)
            startActivity(intent)
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithEmail:success")
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Email o contraseña o incorrectos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}