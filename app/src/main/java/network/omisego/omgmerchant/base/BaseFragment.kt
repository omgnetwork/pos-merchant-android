package network.omisego.omgmerchant.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

/**
 * Enforce initialize the ViewModel and observe the live data in the right place
 */
abstract class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onProvideViewModel()
        onReceiveArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindDataBinding()
        onObserveLiveData()
    }

    /**
     * Assign arguments to local variable here.
     */
    open fun onReceiveArgs() {}

    /**
     * Initialize the ViewModel here
     */
    abstract fun onProvideViewModel()

    /**
     * Bind the data binding here
     */
    open fun onBindDataBinding() {}

    /**
     * Observe the live data here
     */
    abstract fun onObserveLiveData()
}
