package com.example.seafest.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.AllFishAdapter
import com.example.seafest.adapter.SeaFishAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.FragmentHomeBinding
import com.example.seafest.ui.list.ListActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getFish()
//        getData()
//        getSeaFish()
        getData(1111)
        getData(1112)
        getData(1113)

        binding.ivArrowSeaFish.setOnClickListener {
            intent(1111)
        }
        binding.ivArrowFreshWater.setOnClickListener {
            intent(1112)
        }
        binding.ivArrowBrackishWater.setOnClickListener {
            intent(1113)
        }
    }

    private fun intent(id: Int) {
        val intent = Intent(requireActivity(), ListActivity::class.java)
        intent.putExtra(KEY, id)
        startActivity(intent)
    }

    private fun getData(id: Int) {
        homeViewModel.getHomeFish(id).observe(requireActivity()) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tindakan yang dilakukan saat sedang memuat
                }
                is ResultState.Success -> {
                    val adapterFish = SeaFishAdapter()
                    val fishResponse = result.data.listFish
                    if (id == 1111) {
                        binding.rvIkanLaut.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == 1112) {
                        binding.rvIkanAirTawar.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == 1113) {
                        binding.rvIkanAirPayau.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                }

                is ResultState.Error -> {
                    val errorMessage = result.message
                    // Tindakan yang dilakukan saat mendapatkan hasil error
                    // Misalnya, menampilkan pesan kesalahan ke pengguna
                }
            }
        }
    }

//    private fun getSeaFish() {
//
//        val fishAdapter = AllFishAdapter()
//        binding.rvIkanLaut.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = fishAdapter
//            setHasFixedSize(true)
//        }
//        homeViewModel.getSeaFish.observe(viewLifecycleOwner) {
//            fishAdapter.submitData(lifecycle, it)
//            Log.d("HomeFragment", "Ini data ikan pakai paging2: $it")
////            progressIndicator?.visibility = View.VISIBLE
//        }
//    }

    companion object {
        const val KEY = "key"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
