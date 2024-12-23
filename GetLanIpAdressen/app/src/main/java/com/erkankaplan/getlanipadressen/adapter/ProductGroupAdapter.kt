package com.erkankaplan.getlanipadressen.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erkankaplan.getlanipadressen.databinding.ListItemProductGroupsBinding
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel

class ProductGroupAdapter : ListAdapter<ProductGroupModel, ProductGroupAdapter.ProductGroupViewHolder>(
    ProductGroupDiffCallback()) {

    private val _itemClickLiveData = MutableLiveData<ProductGroupModel>()
    val itemClickOnProductGroupsLiveData : LiveData<ProductGroupModel> get() = _itemClickLiveData

    private var listener: OnItemClickListener? = null
    // Seçili pozisyonu izlemek için bir değişken
    private var selectedPosition = RecyclerView.NO_POSITION


    interface OnItemClickListener {
        fun onItemClick(productGroup : ProductGroupModel)
    }


    inner class ProductGroupViewHolder(val binding : ListItemProductGroupsBinding) : RecyclerView.ViewHolder(
        binding.root) {

        init {
            /**
            binbinding.root  oldugu zaman calismiyor, oncelik butona geciyor
            */
            binding.tvProductGroupName.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = getItem(position)
                    listener?.onItemClick(clickedItem)
                    _itemClickLiveData.value = clickedItem

                    // Seçili pozisyonu güncelle
                    val previousPosition = selectedPosition
                    selectedPosition = position

                    // Önceki ve yeni pozisyonu güncelle
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
            }
        }
    }
        }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ProductGroupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemProductGroupsBinding.inflate(inflater, parent, false)
        return ProductGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ProductGroupViewHolder, position : Int) {
        val productGroup = getItem(position)
        holder.binding.tvProductGroupName.text = productGroup.name

        // Eğer bu pozisyon seçili ise, arka plan rengini gri yap
        if (position == selectedPosition) {
            holder.binding.root.setBackgroundColor(Color.BLUE)
        } else {
            // Değilse varsayılan arka plan rengini kullan
            holder.binding.root.setBackgroundColor(Color.WHITE)
        }

    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
}


class ProductGroupDiffCallback : DiffUtil.ItemCallback<ProductGroupModel>() {
    override fun areItemsTheSame(oldItem : ProductGroupModel, newItem : ProductGroupModel) : Boolean {
        // Aynı item mi? (ID'ye göre kontrol)
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem : ProductGroupModel, newItem : ProductGroupModel) : Boolean {
        // İçerik aynı mı? (Verinin diğer alanlarına göre kontrol)
        return oldItem == newItem
    }
}