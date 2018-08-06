package network.omisego.omgmerchant.pages.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgmerchant.R

class MainFragment : Fragment() {

    private var showSplash = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showSplash) {
            showSplash = false
            findNavController().navigate(R.id.action_main_to_splash)
        }

        tvMain.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_sign_in)
        }
    }
}
