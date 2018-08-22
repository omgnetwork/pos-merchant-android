package network.omisego.omgmerchant.base

import android.support.v7.util.DiffUtil

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LoadingDiffCallback(
    private val loadingItems: List<String>,
    private val contentItems: List<*>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return if (oldPos < contentItems.size && newPos < contentItems.size) {
            contentItems[oldPos] == contentItems[newPos]
        } else {
            false
        }
    }

    override fun getOldListSize() = loadingItems.size
    override fun getNewListSize() = contentItems.size

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val totalSize = contentItems.size + loadingItems.size
        return if (oldPos < contentItems.size && newPos < totalSize) {
            false
        } else {
            !(oldPos < totalSize && newPos < contentItems.size)
        }
    }
}
