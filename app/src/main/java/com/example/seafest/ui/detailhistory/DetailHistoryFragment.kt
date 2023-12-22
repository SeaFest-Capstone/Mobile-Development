package com.example.seafest.ui.detailhistory


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.data.api.response.ListScanItem
import com.example.seafest.databinding.FragmentDetailHistoryBinding
import java.util.Locale


class DetailHistoryFragment : DialogFragment() {
    private var _binding: FragmentDetailHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)

        var fishName = arguments?.getString("fishName")
        val fishImage = arguments?.getString("fishImage")
        val fishHabitat = arguments?.getString("fishHabitat")
        val fishBenefit = arguments?.getString("fishBenefit")
        val fishDescription = arguments?.getString("fishDescription")
        val fishStatus = arguments?.getString("fishStatus")

        if (fishName == "Ikan Kakap Merah" || fishName == "Ikan Longtail Tuna") {
            fishName = fishName.replaceFirst("Ikan ", "")
        }
        binding.textFishName.text = fishName
        binding.textHabitat.text = "/$fishHabitat"
        binding.textManfaat.text = fishBenefit
        binding.textDescription.text = fishDescription
        binding.textFreshness.text = fishStatus
        if (fishStatus?.lowercase(Locale.getDefault()) == "segar") {
            binding.textFreshness.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.pure_lime_green
                )
            )
        } else {
            binding.textFreshness.setTextColor(ContextCompat.getColor(requireActivity(),R.color.red))
        }
        Glide.with(requireContext()).load(fishImage)
            .into(binding.imageFish)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonOke.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(fish: ListScanItem): DetailHistoryFragment {
            val fragment = DetailHistoryFragment()
            val bundle = Bundle().apply {
                putString("fishName", fish.fishName)
                putString("fishImage", fish.photoUrl)
                putString("fishHabitat", fish.habitat)
                putString("fishBenefit", fish.benefit)
                putString("fishDescription", fish.description)
                putString("fishStatus", fish.fishStatus)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}