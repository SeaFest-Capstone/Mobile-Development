package com.example.seafest.ui.main



import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.seafest.R
import com.example.seafest.databinding.ActivityMainBinding
import com.example.seafest.ui.cart.CartFragment
import com.example.seafest.ui.scanner.ScannerFragment
import com.example.seafest.ui.home.HomeFragment
import com.example.seafest.ui.profile.ProfileFragment



class MainActivity : AppCompatActivity() {
    private var _binding:ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var selectedItemId: Int = R.id.home // Menyimpan ID item yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Cek apakah ada instance sebelumnya yang disimpan
        if (savedInstanceState != null) {
            selectedItemId = savedInstanceState.getInt(KEY_SELECTED_ITEM, R.id.home)
        }

        setupBottomNavigationView()

        // Restore fragment
        when (selectedItemId) {
            R.id.home -> replaceFragment(HomeFragment())
            R.id.profile -> replaceFragment(ProfileFragment())
            R.id.scanner -> replaceFragment(ScannerFragment())
            R.id.cart -> replaceFragment(CartFragment())
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            selectedItemId = it.itemId // Simpan ID item yang dipilih

            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.scanner -> replaceFragment(ScannerFragment())
                R.id.cart -> replaceFragment(CartFragment())
                else -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_ITEM, selectedItemId)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val KEY_SELECTED_ITEM = "selected_item"
    }
}
