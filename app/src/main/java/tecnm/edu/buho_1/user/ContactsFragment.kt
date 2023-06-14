package tecnm.edu.buho_1.user

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import tecnm.edu.buho_1.R


class ContactsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contactos, container, false)
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) {
            val context = activity?.applicationContext
            val mensaje = "No tienes conexión a Internet!"
            val duracion = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, mensaje, duracion)
            toast.show()

        }

        return view
    }

}