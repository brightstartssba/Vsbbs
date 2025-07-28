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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gallery.android.adapter.IOSMediaAdapter
import com.gallery.android.adapter.RecentDaysAdapter
import com.gallery.android.adapter.PeoplePetsAdapter
import com.gallery.android.databinding.ActivityMainBinding
import com.gallery.android.model.MediaItem
import com.gallery.android.viewmodel.MediaViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MediaViewModel
    private lateinit var iosMediaAdapter: IOSMediaAdapter
    private lateinit var recentDaysAdapter: RecentDaysAdapter
    private lateinit var peoplePetsAdapter: PeoplePetsAdapter
    private var isInSelectionMode = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            setupRecyclerViews()
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

        setupUIElements()
        checkPermissionsAndLoad()
    }

    private fun setupUIElements() {
        // Setup search button
        binding.searchButton.setOnClickListener {
            // TODO: Implement search functionality
        }

        // Setup select button
        binding.selectButton.setOnClickListener {
            toggleSelectionMode()
        }

        // Setup permission button
        binding.buttonPermission.setOnClickListener {
            requestMediaPermissions()
        }
    }

    private fun toggleSelectionMode() {
        isInSelectionMode = !isInSelectionMode
        binding.selectButton.text = if (isInSelectionMode) "Cancel" else "Select"
        iosMediaAdapter.setSelectionMode(isInSelectionMode)
    }

    private fun checkPermissionsAndLoad() {
        if (hasMediaPermissions()) {
            setupRecyclerViews()
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

    private fun setupRecyclerViews() {
        // Main photos grid with iOS-style staggered layout
        iosMediaAdapter = IOSMediaAdapter { mediaItem ->
            if (!isInSelectionMode) {
                openMediaViewer(mediaItem)
            }
        }
        
        binding.recyclerViewMain.apply {
            layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            adapter = iosMediaAdapter
        }

        // Recent Days horizontal list
        recentDaysAdapter = RecentDaysAdapter { mediaItems ->
            // Open media viewer for recent days
            if (mediaItems.isNotEmpty()) {
                openMediaViewer(mediaItems.first())
            }
        }
        
        binding.recyclerViewRecentDays.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentDaysAdapter
        }

        // People & Pets horizontal list
        peoplePetsAdapter = PeoplePetsAdapter { personName ->
            // TODO: Implement people/pets filtering
        }
        
        binding.recyclerViewPeoplePets.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = peoplePetsAdapter
        }
    }

    private fun loadMedia() {
        binding.progressBar.visibility = View.VISIBLE
        hideAllContent()
        
        lifecycleScope.launch {
            try {
                val mediaItems = viewModel.loadMediaItems(this@MainActivity)
                
                // Update item count
                binding.itemCount.text = getString(R.string.items_count, mediaItems.size)
                
                // Update main grid
                iosMediaAdapter.updateMedia(mediaItems)
                
                // Update recent days (group by date)
                val recentDaysData = groupMediaByRecentDays(mediaItems)
                recentDaysAdapter.updateData(recentDaysData)
                
                // Update people & pets (mock data for now)
                val peoplePetsData = createMockPeoplePetsData()
                peoplePetsAdapter.updateData(peoplePetsData)
                
                binding.progressBar.visibility = View.GONE
                if (mediaItems.isEmpty()) {
                    binding.textViewEmpty.visibility = View.VISIBLE
                } else {
                    showAllContent()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun groupMediaByRecentDays(mediaItems: List<MediaItem>): List<Pair<String, List<MediaItem>>> {
        // Group media by date and take top 3 recent days
        return mediaItems
            .groupBy { mediaItem ->
                // Convert timestamp to date string
                val date = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                    .format(java.util.Date(mediaItem.dateAdded * 1000))
                date
            }
            .toList()
            .take(3)
    }

    private fun createMockPeoplePetsData(): List<Pair<String, String>> {
        // Mock data for people & pets - in real app this would come from ML face detection
        return listOf(
            "Person 1" to "",
            "Person 2" to "",
            "Pet 1" to ""
        )
    }

    private fun openMediaViewer(mediaItem: MediaItem) {
        val intent = Intent(this, MediaViewerActivity::class.java).apply {
            putExtra("media_path", mediaItem.path)
            putExtra("media_type", mediaItem.type.name)
        }
        startActivity(intent)
    }

    private fun showPermissionDeniedUI() {
        hideAllContent()
        binding.progressBar.visibility = View.GONE
        binding.permissionLayout.visibility = View.VISIBLE
    }

    private fun hideAllContent() {
        binding.recyclerViewMain.visibility = View.GONE
        binding.recyclerViewRecentDays.visibility = View.GONE
        binding.recyclerViewPeoplePets.visibility = View.GONE
        binding.recentDaysTitle.visibility = View.GONE
        binding.peoplePetsTitle.visibility = View.GONE
        binding.textViewEmpty.visibility = View.GONE
        binding.permissionLayout.visibility = View.GONE
    }

    private fun showAllContent() {
        binding.recyclerViewMain.visibility = View.VISIBLE
        binding.recyclerViewRecentDays.visibility = View.VISIBLE
        binding.recyclerViewPeoplePets.visibility = View.VISIBLE
        binding.recentDaysTitle.visibility = View.VISIBLE
        binding.peoplePetsTitle.visibility = View.VISIBLE
    }
}