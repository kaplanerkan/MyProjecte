package com.erkankaplan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.erkankaplan.databinding.ItemIpsRowBinding

class IpAdreslerAdapter2(
    private var ipAdreslerList : MutableList<IpAdreslerModel>,
    private val itemClickListener : OnItemClickListener
): RecyclerView.Adapter<IpAdreslerViewHolder>(){


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : IpAdreslerViewHolder {
       return IpAdreslerViewHolder(ItemIpsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() : Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder : IpAdreslerViewHolder, position : Int) {
        TODO("Not yet implemented")
    }


    // Update list using DiffUtil
    fun updateList(newList: MutableList<IpAdreslerModel>) {

        val diffCallback = IpAdreslerAdapterDiffCallBack(ipAdreslerList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        ipAdreslerList = newList
        diffResult.dispatchUpdatesTo(this)

    }


}