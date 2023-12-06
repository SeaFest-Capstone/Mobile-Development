package com.example.seafest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seafest.data.api.response.ListFishItem
import com.example.seafest.data.local.FishEntity
import com.example.seafest.databinding.ItemListsBinding
import com.example.seafest.utils.Helper

class ListVerticalAdapter: ListAdapter<ListFishItem, ListVerticalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListsBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fish = getItem(position)
        if (fish != null) {
            holder.bind(fish)
        }
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
//            intent.putExtra(KEY, user.login)
//            holder.itemView.context.startActivity(intent)
//        }
    }

    inner class MyViewHolder(private  val binding: ItemListsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fish: ListFishItem) {
            binding.tvNameFish.text = "${fish.nameFish}"
            binding.tvHargaIkan.text = fish.price?.let { Helper.formatRupiah(it) }
            binding.tvJenisIkan.text = "${fish.habitat}"
            binding.textViewDescription.text = "${fish.description}"
            Glide.with(binding.imageView).load(fish.photoUrl)
                .into(binding.imageView)
        }
    }

    companion object {
        private const val KEY = "key"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListFishItem>() {
            override fun areItemsTheSame(oldItem: ListFishItem, newItem: ListFishItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListFishItem, newItem: ListFishItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}