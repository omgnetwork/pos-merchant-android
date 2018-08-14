package network.omisego.omgmerchant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NavGraph", "------------ Start Logging... ------------")
        findNavController(R.id.nav_host).addOnNavigatedListener { controller, destination ->
            Log.d("NavGraph", destination.label?.toString() ?: "")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("Nav", "Navigate Up")
        return findNavController(R.id.nav_host).navigateUp()
    }

}
