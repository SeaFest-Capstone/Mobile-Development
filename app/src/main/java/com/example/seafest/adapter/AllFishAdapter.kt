package com.example.seafest.adapter

import androidx.paging.PagingDataAdapter
import com.example.seafest.data.local.FishEntity

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seafest.databinding.ItemsIkanBinding

class AllFishAdapter: PagingDataAdapter<FishEntity, AllFishAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsIkanBinding.inflate(LayoutInflater.from(parent.context),parent, false)
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

    inner class MyViewHolder(private  val binding: ItemsIkanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fish: FishEntity) {
            binding.tvNamaIkan.text = "${fish.nameFish}"
            binding.tvHargaIkan.text = "Rp${fish.price}"
            Glide.with(binding.imageViewIkan).load(fish.photoUrl)
                .into(binding.imageViewIkan)
        }
    }

    companion object {
        private const val KEY = "key"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FishEntity>() {
            override fun areItemsTheSame(oldItem: FishEntity, newItem: FishEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FishEntity, newItem: FishEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}