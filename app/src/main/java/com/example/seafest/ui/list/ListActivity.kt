package com.example.seafest.ui.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.ListVerticalAdapter2
import com.example.seafest.adapter.LoadingStateAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityListBinding
import kotlinx.coroutines.launch

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

        val id = intent.getIntExtra(KEY, 0)
        if (id == 1111) {
            binding.titleList.setText("Ikan Laut")
        } else if (id == 1112) {
            binding.titleList.setText("Ikan Air Tawar")
        } else if (id == 1113) {
            binding.titleList.setText("Ikan Air Payau")
        } else {

        }
//        getData2(id)
        Log.d("inites", "nilai Id = $id")
        getData(id)
    }

    private fun getData(id: Int) {
        lifecycleScope.launch {
            val adapterFish = ListVerticalAdapter2()
            binding.rvListIkan.apply {
                layoutManager =
                    LinearLayoutManager(this@ListActivity)
                setHasFixedSize(true)
            }
            binding.rvListIkan.adapter = adapterFish.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapterFish.retry()
                }
            )
            viewModel.getFishPaging3(id).observe(this@ListActivity) {
                Log.d("ThisShit", "Ini data paging ku : $it")
                adapterFish.submitData(lifecycle, it)
            }
        }
    }
//    private fun getData2(id: Int) {
//        viewModel.getHomeFish(id).observe(this) { result ->
//            when (result) {
//                is ResultState.Loading -> {
//                    // Tindakan yang dilakukan saat sedang memuat
//                }
//
//                is ResultState.Success -> {
//                    val adapterFish = ListVerticalAdapter2()
//                    val fishResponse = result.data.listFish
//                    binding.rvListIkan.apply {
//                        layoutManager =
//                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                        adapter = adapterFish
//                        setHasFixedSize(true)
//                    }
////                    adapterFish.submitList(fishResponse)
//                    Log.d("coba Log", "Ini data List Activity $fishResponse")
//                }
//
//                else -> {}
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    companion object {
        const val KEY = "key"
    }


}