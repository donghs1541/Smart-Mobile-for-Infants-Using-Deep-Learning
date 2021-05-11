package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Frag4 extends Fragment {

    private View view;
    private String[] LIST_MENU = {"버전","asd","LIST3","asdasdasdasd"};
    private String[] SubListMenu = {"v1.0","asdasd","sadasd","asdasdsadasd"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag4,container,false);
        ListView listview = (ListView) view.findViewById(R.id.Settings) ;

        HashMap<String,String> SettingsText = new HashMap<>();
        for(int i=0;i<LIST_MENU.length;i++){
            SettingsText.put(LIST_MENU[i],SubListMenu[i]);
        }

        List<HashMap<String,String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),listItems,R.layout.settings_textitem,new String[]{"First Line","Second Line"},new int[]{R.id.SettingsText1,R.id.SettingsText2});
        Iterator it = SettingsText.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String,String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultMap.put("First Line",pair.getKey().toString());
            resultMap.put("Second Line",pair.getValue().toString());
            listItems.add(resultMap);
        }

        listview.setAdapter(adapter) ;



    return view;

    }
}
