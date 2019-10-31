package com.mkr.ocr.imagetotext.converter.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkr.ocr.imagetotext.converter.R;
import com.mkr.ocr.imagetotext.converter.Activities.ViewTextActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSaved extends Fragment {
    private ArrayList<String> textList;
    SharedPreferences sharedPreferences;
    View fragment_one;
    HashMap<String,String> set;
    ListView listView;
    boolean euconsent;
    int count;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_one = inflater.inflate(R.layout.fragment_saved_texts, container, false);
        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String hashmapString = sharedPreferences.getString("savedDocs", null);
        euconsent = sharedPreferences.getBoolean("euconsent", false);
        count = sharedPreferences.getInt("countInt", 0);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        set = gson.fromJson(hashmapString, type);
        if(set != null){
            createList();
        }

        return fragment_one;
    }

    private void createList(){
        textList = new ArrayList<>();

        for (String it: set.keySet()){
            Log.d("key", it);
            textList.add(it);

        }
        listView = (ListView) fragment_one.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,textList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                count++;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("countInt", count);
                editor.apply();
                String key = textList.get(i);
                final int it = i;
                Log.d("guardado",set.get(key));

                start(i);

            }
        });
    }
    private void start(int i){
        Intent intent = new Intent(getActivity(), ViewTextActivity.class);
        intent.putExtra("texto", set.get(textList.get(i)));
        intent.putExtra("activity", 1);
        intent.putExtra("name", textList.get(i));
        startActivity(intent);
    }

}