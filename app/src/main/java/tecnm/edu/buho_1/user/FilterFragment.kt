package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import tecnm.edu.buho_1.R


class FilterFragment : Fragment() {

    private lateinit var rbtn: ImageButton

    private lateinit var watch_1: Button
    private lateinit var watch_2: Button
    private lateinit var watch_3: Button
    private lateinit var watch_4: Button
    private lateinit var watch_5: Button
    private lateinit var watch_6: Button
    private lateinit var watch_7: Button


    private lateinit var idWatch_1: SharedPreferences


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter, container, false)

        rbtn = view.findViewById(R.id.back_home)

        rbtn.setOnClickListener {
            onBackPressed()
        }
        idWatch_1 = requireActivity().getSharedPreferences("Watch",0)

        watch_1 = view.findViewById(R.id.btnSecurity)
        watch_1.setOnClickListener {
            val watch1 = "Seguridad Pública"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch1)
            editor.apply()

            onBackPressed()
        }

        watch_2 = view.findViewById(R.id.btnNational)
        watch_2.setOnClickListener {
            val watch2 = "Guardia Nacional"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch2)
            editor.apply()

            onBackPressed()
        }

        watch_3 = view.findViewById(R.id.btnPublic)
        watch_3.setOnClickListener {
            val watch3 = "Ministerio Público"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch3)
            editor.apply()

            onBackPressed()

        }
        watch_4 = view.findViewById(R.id.btnCruz)
        watch_4.setOnClickListener {
            val watch4 = "Cruz Roja"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch4)
            editor.apply()

            onBackPressed()
        }

        watch_5 = view.findViewById(R.id.btnCivil)
        watch_5.setOnClickListener {
            val watch5 = "Protección Civil"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch5)
            editor.apply()

            onBackPressed()
        }

        watch_6 = view.findViewById(R.id.btnFire)
        watch_6.setOnClickListener {
            val watch6 = "Bomberos"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch6)
            editor.apply()

            onBackPressed()
        }

        watch_7 = view.findViewById(R.id.all)
        watch_7.setOnClickListener {
            val watch7 = "Todo"
            val editor = idWatch_1.edit()
            editor.putString("idWatch1", watch7)
            editor.apply()

            onBackPressed()
        }


        return view
    }


    private fun onBackPressed() {
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("inicioFragment")
        if (myFragment != null && myFragment.isVisible) {
            // Estás en el Fragmento que deseas regresar
        } else {
            super.requireActivity().onBackPressed()
        }
    }

}