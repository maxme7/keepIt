package com.example.keepit.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceFragmentCompat
import com.example.keepit.R
import androidx.preference.Preference
import java.util.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setPreferenceListeners()
    }

    private fun setPreferenceListeners() {
        val config = requireContext().resources.configuration

        findPreference<Preference>("setLanguage")?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
                //code for what you want it to do
                requireContext().resources.configuration.setLocale(Locale.GERMAN)
//                Toast.makeText(requireContext(), requireContext().resources.configuration.locales.toString(), Toast.LENGTH_LONG).show()
                true
            }

        findPreference<Preference>("chooseTheme")?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
                requireActivity().recreate() //recreate activity to apply changed theme
                true
            }

//        not necessary since startDestination only relevant on start, where the setting is fetched anyways
//        findPreference<Preference>("startDestination")?.onPreferenceChangeListener =
//            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
////                requireActivity().recreate() //recreate activity to apply changed startDestination
//                true
//            }

        findPreference<Preference>("enableNotifications")?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
                //code for what you want it to do
                Toast.makeText(requireContext(), requireContext().resources.configuration.locales.toString(), Toast.LENGTH_LONG).show()
                requireContext().resources.configuration.setLocale(Locale.GERMAN)
                Toast.makeText(requireContext(), requireContext().resources.configuration.locales.toString(), Toast.LENGTH_LONG).show()
                true
            }

        findPreference<Preference>("lockScreenVisibility")?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference: Preference, newValue: Any ->
                //code for what you want it to do
                Toast.makeText(requireContext(), requireContext().resources.configuration.locales.toString(), Toast.LENGTH_LONG).show()
                requireContext().resources.configuration.setLocale(Locale.GERMAN)
                Toast.makeText(requireContext(), requireContext().resources.configuration.locales.toString(), Toast.LENGTH_LONG).show()
                true
            }

        //https://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen#7251575
        // attaching event listener to "button" preference
        findPreference<Preference>("clearDatabase")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                //code for what you want it to do
                createClearDatabaseDialog()?.show()
                true
            }
    }

    //https://stackoverflow.com/questions/32825483/setting-confirmation-on-button-click
    // confirm dialog on clearing database
    private fun createClearDatabaseDialog(): AlertDialog.Builder? {
        return AlertDialog.Builder(requireContext())
            .setTitle("Clear Database")
            .setMessage("Do you really want to DELETE all the words you saved so far?")
            .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(requireContext(), "Database has been cleared", Toast.LENGTH_LONG).show();
            }
            .setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel();
            }
    }


}