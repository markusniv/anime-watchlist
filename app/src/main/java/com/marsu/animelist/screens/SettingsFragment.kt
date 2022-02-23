package com.marsu.animelist.screens

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.marsu.animelist.App
import com.marsu.animelist.R
import com.marsu.animelist.databinding.FragmentSettingsBinding

private lateinit var binding: FragmentSettingsBinding

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        val sfwSwitch = binding.settingsViewSafeSwitch
        sfwSwitch.isChecked = App.sfw
        sfwSwitch.setOnClickListener {
            val message = if (App.sfw) "Are you sure you wish to turn SFW off?"
            else "Are you sure you wish to turn SFW on?"
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(message)
                .setTitle("Confirm")
                .setCancelable(true)
                .setPositiveButton("Yes"
                ) { _, _ ->
                    App.sfw = sfwSwitch.isChecked
                    val sharedPref = App.appContext.getSharedPreferences(
                        "SETTINGS_PREF", Context.MODE_PRIVATE
                    )
                    with (sharedPref.edit()) {
                        putBoolean("SFW", App.sfw)
                        apply()
                    }
                }
                .setNegativeButton("No"
                ) { dialog, _ ->
                    sfwSwitch.isChecked = App.sfw
                    dialog.cancel()
                }
            builder.show()
        }

        return binding.root;
    }

}