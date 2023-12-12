package com.example.seafest.ui.list

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.ListVerticalAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ListViewmodel> {
        ViewModelFactory.getInstance(this@ListActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val id = intent.getIntExtra(KEY, 0)
        if (id == 1111) {
            binding.titleList.setText("Ikan Laut")
        } else if (id == 1112) {
            binding.titleList.setText("Ikan Air Tawar")
        } else if (id == 1113) {
            binding.titleList.setText("Ikan Air Payau")
        } else {

        }
        Log.d("inites", "nilai Id = $id")
        getData(id)
    }


    private fun getData(idHabitat: Int?) {
        viewModel.getFishByHabitat(idHabitat).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    val adapterFish = ListVerticalAdapter()
                    val fishResponse = result.data.listFish
                    binding.rvListIkan.apply {
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = adapterFish
                        setHasFixedSize(true)
                    }
                    showLoading(false)
                    adapterFish.submitList(fishResponse)
                    Log.d("coba Log", "Ini data List Activity $fishResponse")
                }

                is ResultState.Error -> {
                    showLoading(false)

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    companion object {
        const val KEY = "key"
    }


}