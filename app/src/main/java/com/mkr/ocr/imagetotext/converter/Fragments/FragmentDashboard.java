package com.mkr.ocr.imagetotext.converter.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mkr.ocr.imagetotext.converter.Clases.SettingItem;
import com.mkr.ocr.imagetotext.converter.ListAdapters.SettingIttemAdapter;
import com.mkr.ocr.imagetotext.converter.R;
import com.mkr.ocr.imagetotext.converter.Utilities.UtilityLib;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentDashboard extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<SettingItem> items;
    SharedPreferences sharedPreferences;
    SettingIttemAdapter adapter;
    boolean toGallery;
    View fragment_one;
    String lang;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_one = inflater.inflate(R.layout.fragment_settings, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        toGallery = sharedPreferences.getBoolean("toGallery", false);
        setListViewAdapter();
        return fragment_one;
    }

    private void createList(){
        String lan = sharedPreferences.getString("lang", null);
        if(lan == null){
            lang = Locale.getDefault().getISO3Language();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lang", lang);
            editor.apply();
        }else{
            lang = lan;
        }
        Log.d("language", lang);
        items = new ArrayList<>();
        items.add(new SettingItem(getResources().getDrawable(R.drawable.baseline_language_white_48dp), getString(R.string.ChangeInputLang)+ "("+ lang +")",false, false));
        items.add(new SettingItem(getResources().getDrawable(R.drawable.round_save_white_48dp), getString(R.string.saveImagesToGAllery), toGallery, true));
        items.add(new SettingItem(getResources().getDrawable(R.drawable.round_star_rate_white_48dp), getString(R.string.rateInstore), false, false));
        items.add(new SettingItem(getResources().getDrawable(R.drawable.round_share_white_48dp), getString(R.string.shareText), false, false));
        items.add(new SettingItem(getResources().getDrawable(R.drawable.round_business_white_48dp), getString(R.string.privacyPolicy), false, false));
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                selectInputLang();
                break;
            case 1:
                break;
            case 2:
                rateTheApp();
                break;
            case 3:
                share();
                break;
            case 4:
                UtilityLib.openWeb(getActivity(), "https://mino.blogspot.com/2019/06/privacy-policy.html");
                break;

        }
    }

    private void selectInputLang(){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(getString(R.string.language));
        String[] types = {"Arabic","Bulgarian", "Chinese(Simplified)", "Chinese(Traditional)", "Croatian", "Czech", "Danish", "Dutch", "English", "Finnish", "French",
                "German", "Greek", "Hungarian",  "Korean", "Italian", "Japanese", "Polish", "Portuguese", "Russian", "Slovenian", "Spanish", "Swedish", "Turkish"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        changeLan("ara");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.ara)+")");
                        break;
                    case 1:
                        changeLan("bul");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.bul)+")");
                        break;
                    case 2:
                        changeLan("chs");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.chs)+")");
                        break;
                    case 3:
                        changeLan("cht");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.cht)+")");
                        break;
                    case 4:
                        changeLan("hrv");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.hrv)+")");
                        break;
                    case 5:
                        changeLan("cze");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.cze)+")");
                        break;
                    case 6:
                        changeLan("dan");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.dan)+")");
                        break;
                    case 7:
                        changeLan("dut");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.dut)+")");
                        break;
                    case 8:
                        changeLan("eng");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.eng)+")");
                        break;
                    case 9:
                        changeLan("fin");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.fin)+")");
                        break;
                    case 10:
                        changeLan("fre");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.fre)+")");
                        break;
                    case 11:
                        changeLan("ger");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.ger)+")");
                        break;
                    case 12:
                        changeLan("gre");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.gre)+")");
                        break;
                    case 13:
                        changeLan("hun");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.hun)+")");
                        break;
                    case 14:
                        changeLan("kor");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.kor)+")");
                        break;
                    case 15:
                        changeLan("ita");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.ita)+")");
                        break;
                    case 16:
                        changeLan("jpn");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.jpn)+")");
                        break;
                    case 17:
                        changeLan("pol");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.pol)+")");
                        break;
                    case 18:
                        changeLan("por");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.por)+")");
                        break;
                    case 19:
                        changeLan("rus");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.rus)+")");
                        break;
                    case 20:
                        changeLan("slv");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.slv)+")");
                        break;
                    case 21:
                        changeLan("spa");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.spa)+")");
                        break;
                    case 22:
                        changeLan("swe");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.swe)+")");
                        break;
                    case 23:
                        changeLan("tur");
                        items.get(0).setName(getString(R.string.ChangeInputLang)+ "("+getString(R.string.tur)+")");
                        break;
                }

            }

        });

        b.show();
    }
    private void rateTheApp(){
        String googlePlayUrl = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
        String marketUrl = "market://details?id="+getActivity().getPackageName();
        UtilityLib.rateTheApp(getActivity(),marketUrl ,googlePlayUrl);
    }
    private void share(){
        String googlePlayUrl = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
        share(googlePlayUrl);
    }
    private void setListViewAdapter(){
        createList();
        adapter = new SettingIttemAdapter(getActivity(), R.layout.fragment_settings, items);
        adapter.notifyDataSetChanged();
        ListView listView = fragment_one.findViewById(R.id.listViewSetting);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    private void share(String shareText){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void changeLan(String language){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", language);
        editor.apply();
        setListViewAdapter();
    }

}