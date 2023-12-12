package com.example.seafest.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.SeaFishAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.FragmentHomeBinding
import com.example.seafest.ui.list.ListActivity
import com.example.seafest.ui.login.LoginActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData(1111)
        getData(1112)
        getData(1113)


        binding?.ivArrowSeaFish?.setOnClickListener {
            intent(1111)
        }
        binding?.ivArrowFreshWater?.setOnClickListener {
            intent(1112)
        }
        binding?.ivArrowBrackishWater?.setOnClickListener {
            intent(1113)
        }
    }

    private fun intent(id: Int) {
        val intent = Intent(requireActivity(), ListActivity::class.java)
        intent.putExtra(KEY, id)
        startActivity(intent)
    }

    private fun getData(id: Int) {
        homeViewModel.getFishByHabitat(id).observe(requireActivity()) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tindakan yang dilakukan saat sedang memuat
                    showLoading(true)
                    showLoading2(true)
                }

                is ResultState.Success -> {
                    val adapterFish = SeaFishAdapter()
                    val fishResponse = result.data.listFish
                    if (id == 1111) {
                        binding?.rvIkanLaut?.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        showLoading(false)
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == 1112) {
                        binding?.rvIkanAirTawar?.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        showLoading2(false)
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == 1113) {
                        binding?.rvIkanAirPayau?.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showLoading2(false)
                    val iconError = R.drawable.check_x
                    val errorMessage = result.message
                    showCustomPopup("Error", "Gagal terhubung \nCek koneksi internet anda", iconError)
//
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicatorIkanLaut?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading2(isLoading: Boolean) {
        binding?.progressIndicatorIkanAirTawar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showCustomPopup(title:String,message: String, image: Int) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Oke"
        popupButton.setOnClickListener {
            requireActivity().finish()
        }

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 0.9).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }
    private fun showAlert(message: String) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(message)
                setPositiveButton("Oke") { _, _ ->
                    requireActivity().finish()
                }
            create()
            show()
        }
    }
    companion object {
        const val KEY = "key"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
