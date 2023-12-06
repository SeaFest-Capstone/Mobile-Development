package com.example.seafest.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seafest.R
import com.example.seafest.databinding.FragmentCartBinding



class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}