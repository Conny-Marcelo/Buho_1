package tecnm.edu.buho_1.admin

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.databinding.ActivityPrincipalPublicacionesAdminnBinding

class PrincipalAdmin : AppCompatActivity() {
    var MODE_PRIVATE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPrincipalPublicacionesAdminnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationViewAdmin
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragmentAdmin) as NavHostFragment


        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

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