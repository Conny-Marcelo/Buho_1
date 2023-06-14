package tecnm.edu.buho_1.admin

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import tecnm.edu.buho_1.R


class BlankFragmentAdmin : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blank_admin, container, false)

        val retryButton: Button = view.findViewById(R.id.btnRetry)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragmentAdmin) as NavHostFragment
        navController = navHostFragment.navController

        retryButton.setOnClickListener {
            if (isInternetAvailable(requireContext())) { //Garantizar que la aplicación solo realice acciones que requieren una conexión a Internet cuando esté disponible
                navController.navigate(R.id.notificacionesAdmin)
            }
        }

        return view
    }


    //Método usado paar  verificar si el dispositivo tiene una conexión a Internet activa
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //Instancia del objeto
        if (connectivityManager != null) { //Comprobar si la instancia del objeto ConnectivityManager no es nula y para obtener información de la red activa
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                return true //Si la información de la red no es nula y la red está conectada
            }
        }
        return false
    }

}