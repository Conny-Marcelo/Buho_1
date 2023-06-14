package tecnm.edu.buho_1.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.CommentActivity
import tecnm.edu.buho_1.MainActivity
import tecnm.edu.buho_1.R

class MyCountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailUserTextView: TextView
    private lateinit var nicknameUserTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mi_cuenta, container, false)

        emailUserTextView = view.findViewById(R.id.emailTextViewCount)
        nicknameUserTextView = view.findViewById(R.id.nameTextViewCount)

        auth = FirebaseAuth.getInstance()

        //Cerrar sesión
        val boton3 = view.findViewById<View>(R.id.logOutButtom) as Button
        boton3.setOnClickListener {
            Firebase.auth.signOut()
            view.context.startActivity(Intent(view.context, MainActivity::class.java))
        }

        //Obtener nombre de usuario y correo de la colección
        val db = Firebase.firestore
        val usersCollectionRef = db.collection("users")
        val user = auth.currentUser
        val userId = user?.email.toString()
        usersCollectionRef.document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                val username = document.getString("nickname")
                val email = document.getString("email")
                emailUserTextView.text = "$email"
                nicknameUserTextView.text =  "$username"
            }
        }


        val ma = activity as PrincipalPublicationsActivity

        val sp = ma.getSharedPreferences("SP_", ma.MODE_PRIVATE)

        val editor = sp.edit()
        val swi = view.findViewById<Switch>(R.id.switchMode)

        val theme = sp.getInt("Theme", 1)
        swi.isChecked = theme != 1

        swi.setOnClickListener {
            if (swi.isChecked) {
                editor.putInt("Theme", 0)
            } else {
                editor.putInt("Theme", 1)
            }
            editor.commit()
            ma.setDayNight()
        }

        return view
    }

}