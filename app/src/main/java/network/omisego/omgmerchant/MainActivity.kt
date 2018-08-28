package network.omisego.omgmerchant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController
import network.omisego.omgmerchant.extensions.provideViewModel
import network.omisego.omgmerchant.pages.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NavGraph", "------------ Start Logging... ------------")

        findNavController(R.id.nav_host).addOnNavigatedListener { _, destination ->
            Log.d("NavGraph", destination.label?.toString() ?: "")
        }
        mainViewModel = provideViewModel()
        if (mainViewModel.hasCredential()) {
            mainViewModel.getTokens()
        }
    }

    override fun onBackPressed() {
//        val fm = supportFragmentManager
//        for (frag in fm.fragments) {
//            val f = frag.childFragmentManager.fragments[0].childFragmentManager.fragments[1]
//            fm.fragments[0].childFragmentManager.fragments[0].childFragmentManager.fragments[1].childFragmentManager.backStackEntryCount
//            if (f.isVisible) {
//                val childFm = f.childFragmentManager
//                if (childFm.backStackEntryCount > 0) {
//                    childFm.popBackStack()
//                    return
//                }
//            }
//        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }
}
