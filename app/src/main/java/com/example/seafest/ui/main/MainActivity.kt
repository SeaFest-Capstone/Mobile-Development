package com.example.seafest.ui.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seafest.R
import com.example.seafest.databinding.ActivityMainBinding
import com.example.seafest.ui.cart.CartFragment
import com.example.seafest.ui.scanner.ScannerFragment
import com.example.seafest.ui.home.HomeFragment
import com.example.seafest.ui.profile.ProfileFragment

private var _binding:ActivityMainBinding? = null
private val binding get() = _binding!!

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.searchView.setupWithSearchBar(binding.iconSearch)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.scanner -> replaceFragment(ScannerFragment())
                R.id.cart -> replaceFragment(CartFragment())
            else ->{
                Toast.makeText(this,"Coming soon", Toast.LENGTH_SHORT).show()
            }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}