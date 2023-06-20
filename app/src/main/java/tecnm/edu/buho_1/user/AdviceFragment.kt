package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import tecnm.edu.buho_1.R


class AdviceFragment : Fragment() {

    private lateinit var rbtn : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_advice, container, false)

        rbtn = view.findViewById(R.id.back_notify)

        rbtn.setOnClickListener {
            onBackPressed()
        }

        return view
    }

    private fun onBackPressed() {
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("notificacionesFragment")
        if (myFragment != null && myFragment.isVisible) {
            // Estás en el Fragmento que deseas regresar
        } else {
            super.requireActivity().onBackPressed()
        }
    }
}