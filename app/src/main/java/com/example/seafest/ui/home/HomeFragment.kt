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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.SeaFishAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.FragmentHomeBinding
import com.example.seafest.ui.list.ListActivity

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

        getData("Air-Asin")
        getData("Air-Tawar")
        getData("Air-Payau")

        binding?.ivArrowSeaFish?.setOnClickListener {
            intent("Air-Asin")
        }
        binding?.ivArrowFreshWater?.setOnClickListener {
            intent("Air-Tawar")
        }
        binding?.ivArrowBrackishWater?.setOnClickListener {
            intent("Air-Payau")
        }
    }

    private fun intent(id: String) {
        val intent = Intent(requireActivity(), ListActivity::class.java)
        intent.putExtra(KEY, id)
        startActivity(intent)
    }

    private fun getData(id: String) {
        homeViewModel.getFishByHabitat(id).observe(requireActivity()) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                    showLoading2(true)
                    showLoading3(true)
                }

                is ResultState.Success -> {
                    val adapterFish = SeaFishAdapter()
                    val fishResponse = result.data.listFish
                    if (id == "Air-Asin") {
                        showLoading(false)
                        binding?.rvIkanLaut?.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == "Air-Tawar") {
                        showLoading2(false)
                        binding?.rvIkanAirTawar?.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                        }
                        adapterFish.submitList(fishResponse)
                    }
                    if (id == "Air-Payau") {
                        showLoading3(false)
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
                    showCustomPopup("Error", errorMessage, iconError)
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
    private fun showLoading3(isLoading: Boolean) {
        binding?.progressIndicatorIkanAirPayau?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showCustomPopup(title:String,message: String, image: Int) {
        if (!isAdded || requireActivity().isFinishing) {
            // Fragment sudah di-Detach atau Activity sedang di-Finish, keluar dari metode
            return
        }
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

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 0.9).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    companion object {
        const val KEY = "key"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
