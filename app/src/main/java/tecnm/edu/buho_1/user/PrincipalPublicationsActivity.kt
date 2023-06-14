package tecnm.edu.buho_1.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import tecnm.edu.buho_1.CommentActivity
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.databinding.ActivityPrincipalPublicacionesBinding

class PrincipalPublicationsActivity : AppCompatActivity() {

    var MODE_PRIVATE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflar el diseño de una actividad en Kotlin y establecerlo como la vista raíz de la actividad
        val binding = ActivityPrincipalPublicacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Configura la navegación de la aplicación para que el usuario pueda navegar por diferentes fragmentos
        // de la aplicación seleccionando las opciones de menú en el BottomNavigationView.

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        //Para que la barra de navegación no se suba a l usar el teclado
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setDayNight()

    }

    fun setDayNight() {
        val sp = getSharedPreferences("SP_", MODE_PRIVATE)
        val theme = sp.getInt("Theme", 1)
        if (theme == 0) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
    }


}