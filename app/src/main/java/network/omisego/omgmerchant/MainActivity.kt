package network.omisego.omgmerchant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import network.omisego.omgmerchant.extensions.logi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        findNavController(R.id.nav_host).addOnNavigatedListener { controller, _ ->
            logi("NavGraph: current destination: ${controller.currentDestination?.label}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }

    override fun onBackPressed() {
        if (Navigation.findNavController(this, R.id.navBottomNavigationContainer).currentDestination?.id != R.id.loadingFragment) {
            super.onBackPressed()
        }
    }
}
