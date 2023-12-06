package com.example.seafest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seafest.ViewModelFactory
import com.example.seafest.databinding.FragmentSearchBinding
import com.example.seafest.adapter.ListVerticalAdapter
import com.example.seafest.data.ResultState

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var fishAdapter: ListVerticalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle aksi submit pencarian
                if (!query.isNullOrBlank()) {
                    viewModel.searchFish(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle aksi perubahan teks pencarian
                return true
            }
        })

        observeSearchResults()
    }

    private fun setupRecyclerView() {
        fishAdapter = ListVerticalAdapter()
        binding.rvSearch.adapter = fishAdapter
        // Konfigurasi layout manager sesuai kebutuhan
        // binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Tambahkan konfigurasi layout manager lainnya jika diperlukan
    }

    private fun observeSearchResults() {
        viewModel.fishLiveData.observe(viewLifecycleOwner, { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    // Handle loading state
                }
                is ResultState.Success -> {
                    val fishResponse = resultState.data
                    // Update RecyclerView adapter with the new data
                    fishAdapter.submitList(resultState.data.listFish)
                }
                is ResultState.Error -> {
                    val errorMessage = resultState.message
                    // Handle error state
                    // Show error message to the user
                }
                else -> {}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

