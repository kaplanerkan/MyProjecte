package com.erkankaplan.adapter

import androidx.recyclerview.widget.DiffUtil

class IpAdreslerAdapterDiffCallBack (
    private val oldList: MutableList<IpAdreslerModel>,
    private val newList: MutableList<IpAdreslerModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare unique IDs
        return oldList[oldItemPosition].ipAdres == newList[newItemPosition].ipAdres
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare contents
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}