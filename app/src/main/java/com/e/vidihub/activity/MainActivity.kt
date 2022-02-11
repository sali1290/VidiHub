package com.e.vidihub.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.e.vidihub.R
import dagger.hilt.android.AndroidEntryPoint
import android.content.ComponentName
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize firebase analytics
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = Firebase.analytics

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.custom_button, menu)


        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        this@MainActivity,
                        SearchableActivity::class.java
                    )
                )
            )
            isIconifiedByDefault = true // Do not iconify the widget; expand it by default
        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_name -> {
                val drawer = findViewById<DrawerLayout>(R.id.nav_layout)
                if (!drawer.isOpen) {
                    drawer.open()
                } else {
                    drawer.close()
                }
                true
            }
            R.id.action_search -> {
//                findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment)
                onSearchRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}