package com.example.keepit

import android.Manifest.permission.*
import android.app.Application
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import com.example.keepit.broadcastReceivers.NotificationReceiver
import com.example.keepit.enums.Language
import com.example.keepit.notifications.OngoingMediaNotification
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.getDb
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking
import java.util.*


class MainActivity : AppCompatActivity() {
//    private lateinit var button: ImageButton
//    private lateinit var editText: EditText

    private lateinit var navController: NavController
    private lateinit var drawerLayoutParent: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    private lateinit var drawer: DrawerLayout

    private val bookmarks: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        setAppTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if permission granted; if permission is requested everytime  => resume; pause; resume sequence occurs
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 1) //TODO update to current permissions
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //TODO hide toolbar on scroll down webview??
//        supportActionBar?.hide()
//        supportActionBar?.subtitle = "subtitle"

//        This makes back arrow open drawer instead of going back to home:
//        var toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()


        setupDrawer()

        //TODO tab switcher main view

        //test db:
        runBlocking {
            databaseAccess()
        }



        runBlocking {
            //fallbackToDestructiveMigration will clear all data from db on room db version change
            val dictEntryDao = getDb(applicationContext).dictEntryDao()

            NotificationReceiver.list = dictEntryDao.getEntriesByLang(Language.DE, Language.AR)

        }

        NotificationReceiver.cancel = {
            OngoingMediaNotification.cancelAll(this)
        }

        //https://stackoverflow.com/questions/33698122/android-change-actionbar-title-text-color
//        val s = SpannableString(title)
//        s.setSpan(
//            ForegroundColorSpan(resources.getColor(R.color.dark_red)),
//            0,
//            title.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        supportActionBar?.title = s
    }

    fun setAppTheme() {
        val themePreference = PreferenceManager.getDefaultSharedPreferences(this).getString("chooseTheme", "light_theme")
        if ("dark_theme" == themePreference) {
            setTheme(R.style.Theme_KeepIt_Dark)
        } else {
            setTheme(R.style.Theme_KeepIt)
        }
    }


    //TODO hamburger menu animation too fast
    //TODO how to set fragment transition animations? (since menu is taking care of transitions)

    override fun onBackPressed() {
//        can't get fragment reference //returns null
//        val webViewFragment = supportFragmentManager.findFragmentById(R.id.webViewFragment) as WebViewFragment

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer((GravityCompat.START))
//        } else if (webViewFragment.getWebView().canGoBack()) {
            //goBack() in webview
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDrawer() {
        //TODO https://www.youtube.com/watch?v=zYVEMCiDcmY  make drawer nicer

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //set nav graph startDestination dynamically
        //https://medium.com/@anoopg87/set-start-destination-for-navhostfragment-dynamically-b072a29bfe49
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        val startDestinationPreference = PreferenceManager.getDefaultSharedPreferences(this).getString("startDestination", "langenscheidtFragment")
        navGraph.setStartDestination(resources.getIdentifier(startDestinationPreference, "id", packageName))
        navController.graph = navGraph


        drawerLayoutParent = findViewById(R.id.drawer_layout)
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.navigationView), navController)

        appBarConfig = AppBarConfiguration(navController.graph, drawerLayoutParent)
        setupActionBarWithNavController(navController, appBarConfig)

        destinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            //To be implemented
        }
    }

    private suspend fun databaseAccess() {
        val dictEntryDao = getDb(this).dictEntryDao()

//        dictEntryDao.deleteAll() //TODO
//        dictEntryDao.insertAll(
//            DictEntry("some.url", Date(), Language.AR, Language.DE, "Adam", "1st Street", null, null, null, arrayOf()),
//        )
//
//        val dictEntries: List<DictEntry> = dictEntryDao.getAll()
//        for (e in dictEntries) {
//            Log.i("Room",
//                "${e.url} ${e.date} ${e.sourceLang} ${e.targetLang} ${e.sourceWord} ${e.targetWord} ${e.gram} ${e.phon} ${e.ind} ${
//                    arrayToString(e.categories)
//                }")
//        }
    }

    companion object {
        fun arrayToString(array: Array<String>): String { //TODO move
            var string = "["
            array.forEach { string += "$it," }
            return string.substringBeforeLast(',') + "]"
        }
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangeListener)

        // when app is not yet up completely, notifications (in onDestroy) don't get deleted; either pressing home button or task manager too early
        // put notificationSetup() somewhere where it is guaranteed that app will be up completely. (maybe where you select word set

        val notificationsEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enableNotifications", true)
        if (notificationsEnabled) { //TODO can als be disabled on mobile; should be communicated? can I get info if it is disabled?
            OngoingMediaNotification.setup(this)
            //TODO if statement; only setup, when setting flashcard notifications is true -> probably make a "start button" triggering notifications
        }

        //TODO !! if database cleared, problem with notifications
    }

    override fun onStart() {
        super.onStart()
    }

    //TODO resume called twice and pause once for some reason
    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangeListener)
//
////TODO problem currently only set once. why??
        val languagePreferenc = PreferenceManager.getDefaultSharedPreferences(this).getString("setLanguage", "en")
//
//        val locale = Locale(languagePreference)
//        Locale.setDefault(locale) //necessary for RTL configuration change
//        resources.configuration.setLocale(locale)


        val config = resources.configuration
        config.setLayoutDirection(Locale(languagePreferenc)) //LTR or not
        resources.updateConfiguration(config, resources.displayMetrics)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onDestroy() {
//        OngoingMediaNotification.deleteChannel(this) //rather cancel notifications then delete whole channel
        OngoingMediaNotification.cancelAll(this)

        super.onDestroy()
    }


    //https://protocoderspoint.com/android-alert-dialog-box-with-a-list-of-options/
//    private fun showDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Bookmarks")
//        builder.setItems(bookmarks.toTypedArray()) { dialog, which ->
//            editText.setText(bookmarks[which])
//            button.performClick()
//            dialog.dismiss()
//        }
//
//        builder.create().show()
//    }

}


// broadcast receiver and notifications: https://www.youtube.com/watch?v=CZ575BuLBo4

// save in bundle: https://medium.com/@evanbishop/saving-state-through-the-bundle-when-you-rotate-the-screen-d913422fe4f6