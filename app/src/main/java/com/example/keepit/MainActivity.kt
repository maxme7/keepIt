package com.example.keepit

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import com.example.keepit.enums.Language
import com.example.keepit.room.AppDatabase
import com.example.keepit.room.Converters
import com.example.keepit.room.DictEntry
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking
import java.util.*


class MainActivity : AppCompatActivity() {
//    private lateinit var button: ImageButton
//    private lateinit var editText: EditText

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener

    private val bookmarks: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.navigationView), navController)
//TODO outsource in own method(s)
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)

        destinationChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->


            //test db

            runBlocking {
                databaseAccess()
            }

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

    private suspend fun databaseAccess() {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dictentries").build()
        val dictEntryDao = db.dictEntryDao()

        dictEntryDao.deleteAll()
        dictEntryDao.insertAll(
            DictEntry("some.url", Date(), Language.AR, Language.DE, "Adam", "1st Street", null, null, null, arrayOf()),
        )

        val dictEntries: List<DictEntry> = dictEntryDao.getAll()
        for (e in dictEntries) {
            Log.i("Room",
                "${e.url} ${e.date} ${e.sourceLang} ${e.targetLang} ${e.sourceWord} ${e.targetWord} ${e.gram} ${e.phon} ${e.ind} ${
                    arrayToString(e.categories)
                }")
        }
    }

    fun arrayToString(array: Array<String>): String {
        var string = "["
        array.forEach { string += "$it," }
        return string.substringBeforeLast(',') + "]"
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu);
        return true;
    }

    //https://developer.android.com/training/appbar/actions
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.bookmarkButton -> {
////            val s = editText.text.toString()
//            val s = webView.url.toString()
//            if (!bookmarks.contains(s)) {
//                bookmarks.add(s)
//            }
//            true
//        }
//        R.id.backButton -> {
//            webView.goBack()
//            true
//        }
//        R.id.forwardButton -> {
//            webView.goForward()
//            true
//        }
//        R.id.bookarmsButton -> {
//            showDialog()
//            true
//        }
//        else -> {
//            // If we got here, the user's action was not recognized.
//            // Invoke the superclass to handle it.
//            super.onOptionsItemSelected(item)
//        }
//    }

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

// save in bundle: https://medium.com/@evanbishop/saving-state-through-the-bundle-when-you-rotate-the-screen-d913422fe4f6