package com.mkr.ocr.imagetotext.converter.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkr.ocr.imagetotext.converter.R;
import com.mkr.ocr.imagetotext.converter.Utilities.UtilityLib;

import java.util.HashMap;

public class ViewTextActivity extends AppCompatActivity {

    EditText editText;
    HashMap<String,String> set;
    String textName;
    SharedPreferences sharedPreferences;
    String name;
    int from;
    FloatingActionsMenu menu;
    com.getbase.floatingactionbutton.FloatingActionButton saveBtn, deleteBtn, shareBtn, copyBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        name = "";
        shareBtn = findViewById(R.id.button_share);
        deleteBtn = findViewById(R.id.action_delete);
        saveBtn = findViewById(R.id.button_save);
        copyBtn = findViewById(R.id.button_copy);
        from = (Integer)getIntent().getSerializableExtra("activity");
        name = (String) getIntent().getSerializableExtra("name");

        if(from != 1){
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        editText = (EditText) findViewById(R.id.editText);
        String texto = (String)getIntent().getSerializableExtra("texto");
        editText.setTextIsSelectable(true);
        editText.setText(texto);
        editText.setScroller(new Scroller(ViewTextActivity.this));
        editText.setMaxLines(22);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(new ScrollingMovementMethod());

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilityLib.copy(getApplicationContext(), editText.getText().toString());
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewTextActivity.this);
                alertDialog.setTitle(getString(R.string.confirm));
                alertDialog.setMessage(getString(R.string.confirmDelete)+ " ("+name+") ?");
                alertDialog.setIcon(R.drawable.round_delete_forever_white_48dp);

                alertDialog.setPositiveButton(getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Gson gson = new Gson();
                                String hashmapString = sharedPreferences.getString("savedDocs", null);
                                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                                }.getType();
                                set = gson.fromJson(hashmapString, type);
                                if (set == null) {
                                    set = new HashMap<>();
                                }
                                set.remove(name);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                hashmapString = gson.toJson(set);
                                editor.putString("savedDocs", hashmapString);
                                editor.apply();
                                Toast.makeText(ViewTextActivity.this, getApplicationContext().getResources().getString(R.string.textDeleted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewTextActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();


            }
        });
    }

    private void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void save(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewTextActivity.this);
        alertDialog.setTitle(getString(R.string.fileName));
        alertDialog.setMessage(getString(R.string.Name));

        final EditText input = new EditText(ViewTextActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(name);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.baseline_save_24);

        alertDialog.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        textName = input.getText().toString();
                        if (!textName.equals("")) {
                            Gson gson = new Gson();
                            String hashmapString = sharedPreferences.getString("savedDocs", null);
                            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                            set = gson.fromJson(hashmapString, type);
                            if(set == null){
                                set = new HashMap<>();
                            }
                            set.put(textName,editText.getText().toString());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            hashmapString = gson.toJson(set);
                            editor.putString("savedDocs", hashmapString);
                            editor.apply();
                            Toast.makeText(ViewTextActivity.this,getString(R.string.saved), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewTextActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ViewTextActivity.this,
                                    getString(R.string.nameOfFile), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String googlePlayUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String marketUrl = "market://details?id=" + getPackageName();
        if (id == R.id.menu_rate) {
            UtilityLib.rateTheApp(this, marketUrl, googlePlayUrl);
        } else if (id == R.id.menu_pp) {

            UtilityLib.openWeb(this, "https://lolawic.blogspot.com/p/privacy-policy.html");
        }else if(id == R.id.menu_share){
            shareApp(googlePlayUrl);
        }
        return super.onOptionsItemSelected(item);
    }
    private void shareApp(String googlePlayUrl){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, googlePlayUrl);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
