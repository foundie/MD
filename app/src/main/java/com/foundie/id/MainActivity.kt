package com.foundie.id

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.foundie.id.databinding.ItemCatalogBinding
import com.foundie.id.ui.navigation.HomeScreen
import com.foundie.id.ui.navigation.MainContent
import com.foundie.id.ui.navtheme.MyTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ItemCatalogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.title =  getString(R.string.hi_gorgeous)
        binding = ItemCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContent {
            MyTheme {
                // Set ActionBar
                supportActionBar?.apply {
                    title = getString(R.string.hi_gorgeous)
                }
                // Load HomeScreen
                MainContent()
            }
        }
    }
}
