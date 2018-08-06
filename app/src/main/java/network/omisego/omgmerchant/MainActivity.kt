package network.omisego.omgmerchant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findNavController(R.id.nav_host).addOnNavigatedListener { controller, destination ->
            Log.i("Current Destination", destination.label?.toString())
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()
}
