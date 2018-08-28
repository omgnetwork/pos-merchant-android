package network.omisego.omgmerchant.pages.main.more.setting

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.databinding.ViewholderMoreMenuBinding
import network.omisego.omgmerchant.model.SettingMenu

class SettingAdapter(
    private val viewModel: SettingViewModel,
    private val itemList: List<SettingMenu>
) : RecyclerView.Adapter<SettingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binding = DataBindingUtil.inflate<ViewholderMoreMenuBinding>(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_more_menu,
            parent,
            false
        )
        return SettingViewHolder(binding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(viewHolder: SettingViewHolder, position: Int) {
        viewHolder.binding.menu = itemList[position]
        viewHolder.binding.viewmodel = viewModel
    }
}