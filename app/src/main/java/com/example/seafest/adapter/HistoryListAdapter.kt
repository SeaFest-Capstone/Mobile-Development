package com.example.seafest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.data.api.response.ListScanItem
import com.example.seafest.databinding.ListHistoryBinding
import java.util.Locale


class HistoryListAdapter(
    private val fishList: List<ListScanItem?>?,
    private val onItemClick: (ListScanItem) -> Unit
) : ListAdapter<ListScanItem, HistoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = fishList?.get(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                try {
                    onItemClick(data)
                } catch (e: Exception) {
                    Log.d("recycler click", e.message.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(private val binding: ListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListScanItem) {
            binding.tvNameFish.text = data.fishName
            binding.tvJenisIkan.text = "/${data.habitat}"
            binding.tvStatusKesegaran.text = data.fishStatus
            val context = itemView.context
            if (data.fishStatus?.lowercase(Locale.getDefault()) == "segar") {
                binding.tvStatusKesegaran.setTextColor(ContextCompat.getColor(context, R.color.pure_lime_green))
            } else {
                binding.tvStatusKesegaran.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            Glide.with(binding.imageView).load(data.photoUrl).into(binding.imageView)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListScanItem>() {
            override fun areItemsTheSame(oldItem: ListScanItem, newItem: ListScanItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListScanItem, newItem: ListScanItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
