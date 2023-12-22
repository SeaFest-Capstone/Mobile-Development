package com.example.seafest.ui.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.seafest.R
import com.example.seafest.utils.getImageUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {
    private var currentImageUri: Uri? = null
    private var onImageSelectedListener: OnImageSelectedListener? = null
    private lateinit var editProfileActivity: EditProfileActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)
        val btnCamera: Button = rootView.findViewById(R.id.btn_camera_bottom_sheet)
        val btnGallery: Button = rootView.findViewById(R.id.btn_galeri_bottom_sheet)

        editProfileActivity = EditProfileActivity()

        btnCamera.setOnClickListener {
            Toast.makeText(requireContext(), "Kamera dipilih", Toast.LENGTH_SHORT).show()
        }

        btnGallery.setOnClickListener {

            startGallery()
        }
        btnCamera.setOnClickListener {
            startCamera()
        }

        return rootView
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            editProfileActivity.showImage()
            onImageSelectedListener?.onImageSelected(currentImageUri!!)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            editProfileActivity.showImage()
            onImageSelectedListener?.onImageSelected(currentImageUri!!)
        }
    }

    fun setOnImageSelectedListener(listener: OnImageSelectedListener) {
        onImageSelectedListener = listener
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
interface OnImageSelectedListener {
    fun onImageSelected(imageUri: Uri)
}


