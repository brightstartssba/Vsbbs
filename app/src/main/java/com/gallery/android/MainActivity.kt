package com.gallery.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.gallery.android.adapter.MediaAdapter
import com.gallery.android.databinding.ActivityMainBinding
import com.gallery.android.model.MediaItem
import com.gallery.android.model.MediaType
import com.gallery.android.viewmodel.MediaViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MediaViewModel
    private lateinit var mediaAdapter: MediaAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            setupRecyclerView()
            loadMedia()
        } else {
            showPermissionDeniedUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MediaViewModel::class.java]

        setupTabLayout()
        checkPermissionsAndLoad()
        
        binding.buttonPermission.setOnClickListener {
            requestMediaPermissions()
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> filterMedia(MediaType.ALL)
                    1 -> filterMedia(MediaType.IMAGE)
                    2 -> filterMedia(MediaType.VIDEO)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun checkPermissionsAndLoad() {
        if (hasMediaPermissions()) {
            setupRecyclerView()
            loadMedia()
        } else {
            showPermissionDeniedUI()
        }
    }

    private fun hasMediaPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestMediaPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        requestPermissionLauncher.launch(permissions)
    }

    private fun setupRecyclerView() {
        mediaAdapter = MediaAdapter { mediaItem ->
            openMediaViewer(mediaItem)
        }
        
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = mediaAdapter
        }
    }

    private fun loadMedia() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val mediaItems = viewModel.loadMediaItems(this@MainActivity)
                mediaAdapter.updateMedia(mediaItems)
                
                binding.progressBar.visibility = View.GONE
                if (mediaItems.isEmpty()) {
                    binding.textViewEmpty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.textViewEmpty.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    private fun filterMedia(type: MediaType) {
        mediaAdapter.filterByType(type)
        
        val filteredCount = mediaAdapter.itemCount
        if (filteredCount == 0) {
            binding.textViewEmpty.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.textViewEmpty.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun openMediaViewer(mediaItem: MediaItem) {
        val intent = Intent(this, MediaViewerActivity::class.java).apply {
            putExtra("media_path", mediaItem.path)
            putExtra("media_type", mediaItem.type.name)
        }
        startActivity(intent)
    }

    private fun showPermissionDeniedUI() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.textViewEmpty.visibility = View.GONE
        binding.textViewPermission.visibility = View.VISIBLE
        binding.buttonPermission.visibility = View.VISIBLE
    }
}