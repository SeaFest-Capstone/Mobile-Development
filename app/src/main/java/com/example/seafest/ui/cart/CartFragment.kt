package com.example.seafest.ui.cart

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.CartAdapter
import com.example.seafest.adapter.CartItemListener
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.response.BookmarksItem
import com.example.seafest.databinding.FragmentCartBinding
import com.example.seafest.ui.login.LoginActivity
import com.example.seafest.utils.Helper

class CartFragment : Fragment(), CartItemListener {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPopup.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        setupCartAdapter()

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token == "") {
                binding.layoutNotLogin.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.cardView.visibility = View.GONE
            } else {
                binding.layoutNotLogin.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.cardView.visibility = View.VISIBLE
                user.uid?.let { getData(it) }
            }
        }

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token == "") {
                binding.layoutNotLogin.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.cardView.visibility = View.GONE
            } else {
                binding.layoutNotLogin.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.cardView.visibility = View.VISIBLE

            }
        }
    }



    private fun setupCartAdapter() {
        cartAdapter = CartAdapter()
        cartAdapter.setCartItemListener(this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = cartAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun getData(id: String) {
        try {
            viewModel.getCart(id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val fishResponse = result.data.bookmarks
                        if (fishResponse.isNullOrEmpty()) {
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            cartAdapter.submitList(fishResponse)
                        }
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        binding.layoutNotLogin.visibility = View.VISIBLE
                        binding.buttonPopup.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.cardView.visibility = View.GONE
                        binding.deskripsiPopup.text = "Keranjang anda kosong"
                    }
                }
            }
        } catch (e: Exception) {
            onResume()
        }
    }

    private fun deleteCart(id: String, fishIdCart: String) {
        try {
            viewModel.checkout(id, fishIdCart).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.buttonBayar.isClickable = false
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.buttonBayar.isClickable = true
                        showLoading(false)
                        val fishResponse = result.data
                        showCustomPopup(
                            "Success",
                            "Terima kasih telah memesan \nPesanan anda sedang kami proses",
                            R.drawable.logo_full_apk
                        )
                    }

                    is ResultState.Error -> {
                        binding.buttonBayar.isClickable = true
                        showLoading(false)
                        val iconError = R.drawable.check_x
                        val errorMessage = result.message
                        showCustomPopup("Error", errorMessage, iconError)
                    }
                }
            }
        } catch (e: Exception) {
            onResume()
        }
    }

    override fun onItemChecked(fish: BookmarksItem, isChecked: Boolean, price: Int) {
        if (isChecked) {
            updateTotalPrice(price)
            binding.buttonBayar.setOnClickListener {
                viewModel.getSession().observe(requireActivity()) {
                    deleteCart(it.uid!!, fish.fishData?.fishIdCart!!)
                }

            }
        } else {


        }

        Log.d("CartFragment", "Price of checked item: $price")

    }

    fun updateTotalPrice(price: Int) {
        binding.hargaIkan.text = Helper.formatRupiah(price)
    }

    private fun showCustomPopup(title: String, message: String, image: Int) {
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
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            viewModel.getSession().observe(viewLifecycleOwner){
                it.uid?.let { getData(it) }
            }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object;

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
