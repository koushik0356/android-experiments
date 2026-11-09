package com.example.intentdemo

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FragmentDemoActivity : AppCompatActivity() {

    // Tracks the total number of fragments ever created
    private var totalFragmentsCreated = 0
    
    // Holds the names of fragments for the dropdown list
    private val fragmentDisplayNameList = mutableListOf<String>()
    
    private lateinit var dropdownAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fragment_demo)
        
        // Handle window padding for system bars to ensure UI isn't clipped
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_lab_root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        initializeInterface()
    }

    private fun initializeInterface() {
        val btnAddFragment = findViewById<Button>(R.id.btn_add_fragment)
        val btnBackToDashboard = findViewById<Button>(R.id.btn_back_dashboard)
        val tvTotalCountLabel = findViewById<TextView>(R.id.tv_total_fragments)
        val autoCompleteSelector = findViewById<AutoCompleteTextView>(R.id.auto_complete_fragments)
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        // Connect the shared bottom navigation bar
        NavHelper.setupBottomNav(this, bottomNavView, -1)

        // Setup the adapter for the "Exposed Dropdown Menu" style selector
        dropdownAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, fragmentDisplayNameList)
        autoCompleteSelector.setAdapter(dropdownAdapter)

        updateFragmentCounterDisplay(tvTotalCountLabel)

        // Logic to add a new fragment to the manager and update the UI
        btnAddFragment.setOnClickListener {
            totalFragmentsCreated++
            val newFragmentName = "Fragment $totalFragmentsCreated"
            
            // Update data source and refresh dropdown
            fragmentDisplayNameList.add(newFragmentName)
            dropdownAdapter.notifyDataSetChanged()
            updateFragmentCounterDisplay(tvTotalCountLabel)
            
            // Automatically switch view to the newly created fragment
            autoCompleteSelector.setText(newFragmentName, false)
            displayFragmentByNumber(totalFragmentsCreated)
        }

        btnBackToDashboard.setOnClickListener {
            // Close the lab and return to the Dashboard
            finish()
        }

        // Logic to switch displayed fragment when a dropdown item is clicked
        autoCompleteSelector.setOnItemClickListener { _, _, position, _ ->
            // Adding 1 because position is 0-indexed and our fragments start at 1
            val selectedFragmentNumber = position + 1
            displayFragmentByNumber(selectedFragmentNumber)
        }
    }

    private fun updateFragmentCounterDisplay(textView: TextView) {
        textView.text = getString(R.string.total_fragments_label, totalFragmentsCreated)
    }

    /**
     * Replaces the content in the central container with a specific fragment instance.
     */
    private fun displayFragmentByNumber(number: Int) {
        val fragmentInstance = DynamicFragment.newInstance(number)
        
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragmentInstance)
            .commit()
    }
}