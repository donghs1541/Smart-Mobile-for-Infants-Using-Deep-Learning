package com.example.capstoneproject;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;




public class Frag5 extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);




    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if(key.equals("name_change")){
            // do your work
            startActivity(new Intent(getApplicationContext(),ProfileName.class));
            return true;
        }
        else if(key.equals("birth_change")){
            startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
            return true;
        }
        return false;

    }
    }

