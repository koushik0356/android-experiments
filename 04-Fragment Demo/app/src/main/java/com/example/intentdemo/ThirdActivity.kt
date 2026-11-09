package com.example.intentdemo

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.intentdemo.R

/**
 * ThirdActivity serves as the Dashboard Control Panel.
 * It provides shortcuts to system features, background services, and specialized lab activities.
 */
class ThirdActivity : AppCompatActivity() {

    // Receiver for dynamic system events like Airplane Mode changes
    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isOn = intent?.getBooleanExtra("state", false) ?: false
            val statusText = if (isOn) "ON" else "OFF"
            Toast.makeText(context, "Airplane Mode is $statusText", Toast.LENGTH_SHORT).show()
        }
    }

    // Handles the response for notification permission request (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, "Notification permission denied. Media controls may not be visible.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thirdactivity)

        // Handle window insets for edge-to-edge support while keeping bottom nav accessible
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_navigation)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        checkAndRequestNotificationPermission()
        initializeDashboardControls()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initializeDashboardControls() {
        // Initialize Bottom Navigation through our custom helper
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        NavHelper.setupBottomNav(this, bottomNavView, R.id.nav_dashboard)

        // -- System Information and Intent Shortcuts --
        findViewById<Button>(R.id.btn_show_battery).setOnClickListener { showDetailedBatteryDialog() }
        
        findViewById<Button>(R.id.btn_open_contacts).setOnClickListener { 
            startActivity(Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI)) 
        }
        
        findViewById<Button>(R.id.btn_open_dialer).setOnClickListener { 
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:"))) 
        }
        
        findViewById<Button>(R.id.btn_open_google).setOnClickListener { 
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))) 
        }
        
        findViewById<Button>(R.id.btn_open_camera).setOnClickListener { 
            startActivity(Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)) 
        }
        
        // -- Background Service Controls --
        findViewById<Button>(R.id.btn_start_service).setOnClickListener { 
            startService(Intent(this, Demoservices::class.java)) 
        }
        
        findViewById<Button>(R.id.btn_stop_service).setOnClickListener { 
            stopService(Intent(this, Demoservices::class.java)) 
        }
        
        findViewById<Button>(R.id.btn_downloader_worker).setOnClickListener {
            val periodicWorkRequest = PeriodicWorkRequestBuilder<DownloaderWorker>(15, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this).enqueue(periodicWorkRequest)
            Toast.makeText(this, "15-Min Background Worker Scheduled", Toast.LENGTH_SHORT).show()
        }

        // -- Specialized Laboratory Access --
        findViewById<Button>(R.id.btn_go_to_fragment_lab).setOnClickListener {
            startActivity(Intent(this, FragmentDemoActivity::class.java))
        }

        // -- Media Playback Controls --
        findViewById<Button>(R.id.btn_select_play).setOnClickListener { showMusicSelectionDialog() }
        
        findViewById<Button>(R.id.btn_resume).setOnClickListener { 
            sendMusicControlAction("PLAY_PAUSE") 
        }
        
        findViewById<Button>(R.id.btn_pause).setOnClickListener { 
            sendMusicControlAction("PLAY_PAUSE") 
        }

        // -- Session Management --
        findViewById<Button>(R.id.btn_logout).setOnClickListener {
            LoginState.isLoggedIn = false
            val loginIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(loginIntent)
            finish()
        }
    }

    /**
     * Sends specific playback commands to the MusicService.
     */
    private fun sendMusicControlAction(actionString: String) {
        val serviceIntent = Intent(this, MusicService::class.java).apply { action = actionString }
        startService(serviceIntent)
    }

    /**
     * Fetches real-time battery stats and displays them in a formatted dialog.
     */
    private fun showDetailedBatteryDialog() {
        val batteryStatusIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        if (batteryStatusIntent == null) {
            Toast.makeText(this, "Error: Unable to retrieve battery data.", Toast.LENGTH_SHORT).show()
            return
        }

        // Extract raw data from the system intent
        val currentLevel = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val maxScale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val percentage = if (maxScale > 0) (currentLevel * 100) / maxScale else -1

        // Map charging status to human-readable strings
        val statusInt = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val statusText = when (statusInt) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            else -> "Checking..."
        }

        // Map power source to human-readable strings
        val pluggedInt = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val powerSourceText = when (pluggedInt) {
            BatteryManager.BATTERY_PLUGGED_AC -> "Wall Outlet (AC)"
            BatteryManager.BATTERY_PLUGGED_USB -> "Computer Port (USB)"
            else -> "Battery Power"
        }

        // Map battery health to human-readable strings
        val healthInt = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val healthText = when (healthInt) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Critical: Overheating"
            else -> "Healthy"
        }

        val voltageMv = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val tempCelsius = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0
        val techString = batteryStatusIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion"

        val batteryReport = """
            • Battery Level: $percentage%
            • Charging Status: $statusText
            • Power Source: $powerSourceText
            • Health Status: $healthText
            • Current Voltage: $voltageMv mV
            • Temperature: $tempCelsius °C
            • Technology: $techString
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(R.string.battery_dialog_title)
            .setMessage(batteryReport)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showMusicSelectionDialog() {
        val tracks = arrayOf("Music 1", "Music 2", "Music 3")
        val ids = intArrayOf(R.raw.music1, R.raw.music2, R.raw.music3)
        
        AlertDialog.Builder(this)
            .setTitle("Select a Track")
            .setItems(tracks) { _, selection -> 
                val startIntent = Intent(this, MusicService::class.java).apply {
                    action = "START"
                    putExtra("MUSIC_RES_ID", ids[selection])
                    putExtra("MUSIC_NAME", tracks[selection])
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(startIntent)
                } else {
                    startService(startIntent)
                }
            }.show()
    }

    override fun onStart() {
        super.onStart()
        val exportFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Context.RECEIVER_EXPORTED else 0
        registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED), exportFlag)
    }

    override fun onStop() {
        super.onStop()
        // Safety: Prevent leaks by unregistering on stop
        unregisterReceiver(airplaneModeReceiver)
    }
}