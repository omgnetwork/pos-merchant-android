package network.omisego.omgmerchant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
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
