package com.mkr.ocr.imagetotext.converter.ListAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mkr.ocr.imagetotext.converter.Clases.SettingItem;
import com.mkr.ocr.imagetotext.converter.R;

import java.util.ArrayList;

public class SettingIttemAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<SettingItem> datos;
    public SettingIttemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SettingItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.datos = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.setting_item, null);

        ImageView imagen = item.findViewById(R.id.imgAnimal);
        imagen.setImageDrawable(datos.get(position).getImg());
        final int pos = position;
        final Switch sswitch = item.findViewById(R.id.switch1);
        if(datos.get(position).isHasSwitcher()){
            sswitch.setChecked(datos.get(position).isSwitcher());
            sswitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isChecked = !datos.get(pos).isSwitcher();
                    sswitch.setChecked(isChecked);
                    datos.get(pos).setIsSwitcher(isChecked);
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("toGallery",isChecked );
                    editor.apply();
                }
            });
        }else{
            sswitch.setVisibility(View.INVISIBLE);
        }


        TextView numCelda = (TextView) item.findViewById(R.id.tvField);
        numCelda.setText(datos.get(position).getName());

        return item;
    }
}
