package tecnm.edu.buho_1.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.MainActivity
import tecnm.edu.buho_1.R

class CountAdminFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_count_admin, container, false)
        val button1 = view.findViewById<View>(R.id.logOutButton) as Button
        button1.setOnClickListener {
            Firebase.auth.signOut()
            view.context.startActivity(Intent(view.context, MainActivity::class.java))
        }

        val ma = activity as PrincipalAdmin
        val sp = ma.getSharedPreferences("SP_", ma.MODE_PRIVATE)
        val editor = sp.edit()
        val swi = view.findViewById<Switch>(R.id.switchDarkMode)

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