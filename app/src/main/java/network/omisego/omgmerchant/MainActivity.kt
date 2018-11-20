package network.omisego.omgmerchant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController
import network.omisego.omgmerchant.extensions.logi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        Log.d("NavGraph", "------------ Start Logging... ------------")

        findNavController(R.id.nav_host).addOnNavigatedListener { controller, destination ->
            logi("NavGraph: current destination: ${controller.currentDestination?.label}")
            Log.d("NavGraph", destination.label?.toString() ?: "")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }
}
