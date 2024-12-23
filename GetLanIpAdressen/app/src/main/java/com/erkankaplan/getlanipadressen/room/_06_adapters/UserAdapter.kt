package com.erkankaplan.getlanipadressen.room._06_adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erkankaplan.getlanipadressen.databinding.ItemUserBinding
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel

//Yontem 1
//class UserAdapter(
//    private val onDeleteClick: (UserEntityModel) -> Unit // Silme işlemi için bir lambda fonksiyonu ekliyoruz
//) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

// Yontem 2class UserAdapter(private val onItemClick: (UserEntityModel) -> Unit) :
//    ListAdapter<UserEntityModel, UserAdapter.UserViewHolder>(UserDiffCallback()) {
//
//    private var selectedPosition = RecyclerView.NO_POSITION // Seçilen pozisyonu tutan değişken
//
//    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: UserEntityModel, isSelected: Boolean, onItemClick: () -> Unit) {
//            binding.userId.text = user.id.toString()
//            binding.userName.text = user.name
//            binding.userAge.text = user.age.toString()
//
//            // Seçilen öğeyi farklı renkte göstermek için arka planı değiştiriyoruz
//            if (isSelected) {
//                binding.root.setBackgroundColor(Color.LTGRAY) // Seçili öğe rengi
//            } else {
//                binding.root.setBackgroundColor(Color.WHITE) // Varsayılan arka plan rengi
//            }
//
//            binding.root.setOnClickListener {
//                onItemClick() // Tıklandığında pozisyonu değiştirmek için onItemClick fonksiyonunu çağırıyoruz
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return UserViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        val actualPosition = holder.adapterPosition
//        if (actualPosition != RecyclerView.NO_POSITION) {
//            val user = getItem(actualPosition) // `getItem` metodu `ListAdapter` sınıfından gelir
//            val isSelected = position == selectedPosition
//            holder.bind(user, isSelected) {
//                onItemClick(user)
//                // Tıklanan pozisyonu güncelliyoruz
//                val previousPosition = selectedPosition
//                selectedPosition = position
//                notifyItemChanged(previousPosition)
//                notifyItemChanged(selectedPosition)
//            }
//        }
//    }
//
//    // Yontem 2
//    // Seçilen kullanıcıyı döndürmek için bir fonksiyon
//    fun getSelectedUser(): UserEntityModel? {
//        return if (selectedPosition != RecyclerView.NO_POSITION) {
//            getItem(selectedPosition)
//        } else {
//            null
//        }
//    }
//
//    // Yontem 2
//    // Seçilen kullanıcıyı silmek için bir fonksiyon
//    fun removeSelectedUser() {
//        if (selectedPosition != RecyclerView.NO_POSITION) {
//            val currentList = currentList.toMutableList()
//            currentList.removeAt(selectedPosition)
//            submitList(currentList)
//            // Son öğeyi seçili hale getirme
//            selectedPosition = if (currentList.isNotEmpty()) currentList.size - 1 else RecyclerView.NO_POSITION
//        }
//    }
//}




class UserAdapter(private val onItemClick: (UserModel) -> Unit) :
    ListAdapter<UserModel, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    private var selectedPosition = RecyclerView.NO_POSITION // Seçilen pozisyonu tutan değişken

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel, isSelected: Boolean, onItemClick: () -> Unit) {
            binding.userId.text = user.id.toString()
            binding.userName.text = user.userName
            binding.userAge.text = user.password

            // Seçilen öğeyi farklı renkte göstermek için arka planı değiştiriyoruz
            if (isSelected) {
                binding.root.setBackgroundColor(Color.LTGRAY) // Seçili öğe rengi
            } else {
                binding.root.setBackgroundColor(Color.WHITE) // Varsayılan arka plan rengi
            }

            binding.root.setOnClickListener {
                onItemClick() // Tıklandığında pozisyonu değiştirmek için onItemClick fonksiyonunu çağırıyoruz
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val actualPosition = holder.adapterPosition
        if (actualPosition != RecyclerView.NO_POSITION) {
            val user = getItem(actualPosition) // `getItem` metodu `ListAdapter` sınıfından gelir
            val isSelected = position == selectedPosition
            holder.bind(user, isSelected) {
                onItemClick(user)
                // Tıklanan pozisyonu güncelliyoruz
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }

    // Yontem 2
    // Seçilen kullanıcıyı döndürmek için bir fonksiyon
    fun getSelectedUser(): UserModel? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            getItem(selectedPosition)
        } else {
            null
        }
    }

    // Yontem 2
    // Seçilen kullanıcıyı silmek için bir fonksiyon
    fun removeSelectedUser() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            val currentList = currentList.toMutableList()
            currentList.removeAt(selectedPosition)
            submitList(currentList)
            // Son öğeyi seçili hale getirme
            selectedPosition = if (currentList.isNotEmpty()) currentList.size - 1 else RecyclerView.NO_POSITION
        }
    }



    fun updateUsers(users: List<UserModel>) {
        submitList(users)
    }

}



/***
Sonuç
    Bu adımlarla, UserAdapter sınıfınıza DiffUtil ve ListAdapter'ı entegre etmiş oldunuz.
    Bu, veri setinizi daha verimli güncellemenizi sağlayacak ve büyük liste değişikliklerinde
    performans iyileştirmesi sağlayacaktır.
 */
class UserDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }
}