package com.erkankaplan.getlanipadressen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erkankaplan.getlanipadressen.models.IpAdreslerModel
import com.erkankaplan.getlanipadressen.databinding.ItemIpsRowBinding

class IpAdreslerAdapter(
    private var ipAdreslerList : MutableList<IpAdreslerModel>,
    private val itemClickListener : OnItemClickListener
) : RecyclerView.Adapter<IpAdreslerAdapter.IpAdreslerViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(ips : IpAdreslerModel)
    }


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : IpAdreslerViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ips_row, parent, false)
        //return IpAdreslerViewHolder(view)

        val binding = ItemIpsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IpAdreslerViewHolder(binding)
    }

    override fun onBindViewHolder(holder : IpAdreslerViewHolder, position : Int) {
        val urun = ipAdreslerList[position]
        holder.bind(urun)
    }

    override fun getItemCount() : Int {
        return ipAdreslerList.size
    }

    fun addUrun(ips : MutableList<IpAdreslerModel>) {
        ipAdreslerList = ips //.add(ips)
        notifyItemInserted(ipAdreslerList.size - 1) // Yalnızca eklenen öğeyi bildirir
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateUrunList(newList: List<IpAdreslerModel>) {
//        ipAdreslerList = newList
//        notifyDataSetChanged()
//    }

    inner class IpAdreslerViewHolder(private val binding : ItemIpsRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val ip = ipAdreslerList[position]
                    itemClickListener.onItemClick(ip)
                }
            }
        }


        fun bind(ips : IpAdreslerModel) {
            binding.urunAdiTextView.text = ips.ipAdres
            binding.depoTextView.text = ips.macAdres
        }
    }
}