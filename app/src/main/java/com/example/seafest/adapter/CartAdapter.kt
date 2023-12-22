package com.example.seafest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seafest.data.api.response.BookmarksItem
import com.example.seafest.databinding.ListCartBinding
import com.example.seafest.utils.Helper

class CartAdapter : ListAdapter<BookmarksItem, CartAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var cartItemListener: CartItemListener? = null
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    fun setCartItemListener(listener: CartItemListener) {
        this.cartItemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fish = getItem(position)
        if (fish != null) {
            holder.bind(fish, position == selectedItemPosition)
        }
    }

    inner class MyViewHolder(private val binding: ListCartBinding) :
        RecyclerView.ViewHolder(binding.root) {


        var counter: Int = 1

        init {
            binding.radioButton.setOnClickListener {
                updateSelectedItem(adapterPosition)
            }
            binding.btnIncrement.setOnClickListener {
                updateCounter(true)
            }

            binding.btnDecrement.setOnClickListener {
                updateCounter(false)
            }
        }

        fun bind(fish: BookmarksItem, b: Boolean) {
            binding.radioButton.isChecked = b
            binding.tvCounter.text = counter.toString()
            binding.tvHargaIkanCart.text =
                fish.fishData?.price?.let { Helper.formatRupiah(counter * it.toInt()) }
            binding.tvNamaIkanCart.text = fish.fishData?.nameFish

            Glide.with(binding.imageCartFish).load(fish.fishData?.photoUrl)
                .into(binding.imageCartFish)

            binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
                val fish = getItem(adapterPosition)
                val price = fish?.fishData?.price?.toInt()?.times(counter) ?: 0
                cartItemListener?.onItemChecked(fish, isChecked, price)
            }

        }

        private fun updateSelectedItem(position: Int) {
            selectedItemPosition = position
            notifyDataSetChanged()
        }

        private fun updateCounter(isIncrement: Boolean) {
            counter += if (isIncrement) 1 else -1

            if (counter >= 0) {
                binding.tvCounter.text = counter.toString()
                val fish = getItem(adapterPosition)
                val initialPrice = fish.fishData?.price?.toDouble() ?: 0
                val totalPrice = counter * initialPrice.toInt()
                binding.tvHargaIkanCart.text = Helper.formatRupiah(totalPrice)

                val isChecked = binding.radioButton.isChecked
                val price = fish.fishData?.price?.toInt()?.times(counter) ?: 0
                cartItemListener?.onItemChecked(fish, isChecked, price)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookmarksItem>() {
            override fun areItemsTheSame(oldItem: BookmarksItem, newItem: BookmarksItem): Boolean {
                return oldItem.fishData?.fishIdCart == newItem.fishData?.fishIdCart
            }

            override fun areContentsTheSame(
                oldItem: BookmarksItem,
                newItem: BookmarksItem
            ): Boolean {
                return oldItem.fishData?.fishIdCart == newItem.fishData?.fishIdCart
            }
        }
    }
}

interface CartItemListener {
    fun onItemChecked(fish: BookmarksItem, isChecked: Boolean, price: Int)
}
