package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Frag4 extends Fragment{

    private View view;
   // private String[] LIST_MENU = {"이름","생일","고객센터","버전"};
  //  private String[] SubListMenu = {"051-890-xxxx","우리 아이 탄생일","우리 아이 이름","v1.0"};
    private String[] LIST_MENU = {"이름","생일","고객센터","버전"};
    private String[] SubListMenu = {"051-890-xxxx","우리 아이 탄생일","우리 아이 이름","v1.0"};
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


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //클릭된 리스트뷰 이벤트 역할
                switch (position){
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(),ProfileName.class)); //이름 변경
                        break;
                    case 1:
                        getActivity().startActivity(new Intent(getActivity(),CalendarActivity.class)); //생일 변경
                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                }
            }
        });


    return view;

    }
}
