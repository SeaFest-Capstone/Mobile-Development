package com.example.seafest.ui.scanner

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.FragmentScannerBinding
import com.example.seafest.ml.FreshnessModel2
import com.example.seafest.ml.Model
import com.example.seafest.ui.login.LoginActivity
import com.example.seafest.ui.main.MainActivity
import com.example.seafest.utils.getImageUri
import com.example.seafest.utils.reduceFileImage
import com.example.seafest.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ScannerFragment : Fragment() {

    private lateinit var bitmap: Bitmap
    private lateinit var hasil: String
    private lateinit var hasil2: String
    private lateinit var resize: Bitmap
    val imageSize = 250
    private lateinit var uid: String
    private val targetWidth = 800
    private val targetHeight = 700

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ScannerViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGallery.setOnClickListener { selectImage() }
        binding.btnCamera.setOnClickListener { startCamera() }
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token == "") {
                binding.btnScan.setOnClickListener {
                    showCustomPopup(
                        "Seafest",
                        "Anda Belum Login \nSilahkan Login terlebih dahulu"
                    )
                }
            } else {
                uid = user.uid.toString()
                binding.btnScan.setOnClickListener {
                    lifecycleScope.launch {
                        performPrediction()
                    }
                }
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireActivity())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage(currentImageUri)
        }
    }

    private fun showImage(uri: Uri?) {
        uri?.let {
            Log.d("Image URI", "showImage: $it")
            onActivityResult(100, Activity.RESULT_OK, Intent().apply { this.data = uri })
        }
    }

    private fun buttonClickable(isTrue: Boolean) {
        binding.btnScan.isClickable = isTrue
        if (!isTrue) {
            binding.btnScan.text = "Scanning..."
        } else {
            binding.btnScan.text = "Scan Ikan"
        }
        binding.btnGallery.isClickable = isTrue
        binding.btnCamera.isClickable = isTrue
        (requireActivity() as MainActivity).setBottomNavigationEnabled(isTrue)
    }

    private fun selectImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    private fun performPrediction() {
        if (!isResumed) {
            Log.d("ScannerFragment", "Fragment is not in resumed state. Skipping prediction.")
            return
        }
        buttonClickable(false)
        requireActivity().runOnUiThread {
            showLoading(true)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
//              Model 1
                val model = FreshnessModel2.newInstance(requireContext())

                val inputFeature0 =
                    TensorBuffer.createFixedSize(
                        intArrayOf(1, imageSize, imageSize, 3),
                        DataType.FLOAT32
                    )

                val intValues = IntArray(resize.width * resize.height)
                resize.getPixels(intValues, 0, resize.width, 0, 0, resize.width, resize.height)


                val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                byteBuffer.order(ByteOrder.nativeOrder())

                var pixel = 0

                for (i in 0 until imageSize) {
                    for (j in 0 until imageSize) {
                        val `val` = intValues[pixel++] // RGB
                        byteBuffer.putFloat(((`val` shr 16) and 0xFF).toFloat() * (1f / 255f))
                        byteBuffer.putFloat(((`val` shr 8) and 0xFF).toFloat() * (1f / 255f))
                        byteBuffer.putFloat((`val` and 0xFF).toFloat() * (1f / 255f))
                    }
                }
                inputFeature0.loadBuffer(byteBuffer)


                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                val confidences = outputFeature0.floatArray
                val threshold = 0.5

                var maxIdx = 0
                confidences.forEachIndexed { index, fl ->
                    Log.d("isi index", "$index")
                    Log.d("isi Fl", "$fl")
                    if (confidences[maxIdx] < fl) {
                        maxIdx = index
                    }
                }

                val predictedLabel = if (confidences[maxIdx] < threshold) {
                    "Segar"
                } else {
                    "Tidak Segar"
                }
                hasil = predictedLabel
                model.close()

//              Model 2
                val model2 = Model.newInstance(requireContext())
                val outputs2 = model2.process(inputFeature0)
                val outputFeature02 = outputs2.outputFeature0AsTensorBuffer
                val confidences2 = outputFeature02.floatArray
                var maxPos = 0
                var maxConfidence = 0f
                for (i in confidences2.indices) {
                    if (confidences2[i] > maxConfidence) {
                        maxConfidence = confidences2[i]
                        maxPos = i
                    }
                }

                val labels1 =
                    requireContext().assets.open("labels.txt").bufferedReader().readLines()

                hasil2 = labels1[maxPos]

                withContext(Dispatchers.Main) {
                    Log.d("user id", "$uid")
                    addScan(uid)
                }

                model2.close()


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    requireActivity().runOnUiThread {
                        buttonClickable(true)
                        showLoading(false)
                        showCustomPopup2(
                            "Error",
                            "Terjadi kesalahan saat memproses gambar ",
                            R.drawable.check_x
                        )
                        Log.d("Error", "${e.message}")
                        Log.e("Error", "Error during image processing", e)
                    }
                }
            }

        }
    }

    private fun addScan(id: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            currentImageUri?.let { uri ->
                showLoading(true)
                val imgFile = withContext(Dispatchers.IO) {
                    uriToFile(
                        uri,
                        requireActivity()
                    ).reduceFileImage()
                }
                viewModel.addScan(imgFile, id, hasil, "Ikan $hasil2")
                    .observe(requireActivity()) { result ->
                        Log.d("upload", "Button viewmodel")
                        Log.d("result", "ini result $result")
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                }

                                is ResultState.Success -> {
                                    showLoading(false)

                                    buttonClickable(true)
                                    val iconCheck = R.drawable.check_v
                                    result.data.message?.let {
                                        showCustomPopup2(
                                            "Hasil scan",
                                            "Ikan $hasil2 \nStatus Kesegaran: $hasil",
                                            R.drawable.logo_full_apk
                                        )

                                    }
                                }

                                is ResultState.Error -> {
                                    showLoading(false)
                                    buttonClickable(true)
                                    val errorResponse = result.message
                                    showCustomPopup2("Error", "$errorResponse", R.drawable.check_x)
                                }
                            }
                        }
                    }
            } ?: run {
                Log.d("uri", "uri = $currentImageUri")
            }
        }
    }


    private fun resizeBitmap(inputBitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var width = inputBitmap.width
        var height = inputBitmap.height

        val aspectRatio: Float = width.toFloat() / height.toFloat()

        // Resizing if needed
        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(inputBitmap, width, height, true)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                try {
                    currentImageUri = uri
                    bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

                    // Check if the bitmap size is too large, and resize if needed
                    if (bitmap.byteCount > MAX_BITMAP_SIZE_BYTES) {
                        bitmap = resizeBitmap(bitmap, targetWidth, targetHeight)
                    }
                    resize = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)
                    binding.hasilGambar.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Log.e("Error", "Error loading bitmap: ${e.message}")
                }
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    private fun showCustomPopup(title: String, message: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)
        imagePopup.setImageResource(R.drawable.logo_full_apk)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Login"
        popupButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 1).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    private fun showCustomPopup2(title: String, message: String, image: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
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

            val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
            buttonYes.visibility = View.GONE

            val window = dialog.window
            val layoutParams = window?.attributes
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            layoutParams?.width = (screenWidth * 1).toInt()
            window?.attributes = layoutParams

            dialog.show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val MAX_BITMAP_SIZE_BYTES = 6 * 1024 * 1024

    }
}