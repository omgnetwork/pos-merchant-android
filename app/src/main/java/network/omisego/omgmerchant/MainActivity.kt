package network.omisego.omgmerchant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController
import network.omisego.omgmerchant.extensions.logi
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NavGraph", "------------ Start Logging... ------------")

        findNavController(R.id.nav_host).addOnNavigatedListener { controller, destination ->
            logi("NavGraph: current destination: ${controller.currentDestination?.label}")
            Log.d("NavGraph", destination.label?.toString() ?: "")
        }
        mainViewModel = provideViewModel()
        if (mainViewModel.hasCredential()) {
            mainViewModel.getTokens()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }
}
