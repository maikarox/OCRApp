package com.mkr.ocr.imagetotext.converter.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkr.ocr.imagetotext.converter.Clases.OCRAsyncTask;
import com.mkr.ocr.imagetotext.converter.Clases.Utility;
import com.mkr.ocr.imagetotext.converter.Interfaces.IOCRCallBack;
import com.mkr.ocr.imagetotext.converter.Activities.MainActivity;
import com.mkr.ocr.imagetotext.converter.R;
import com.mkr.ocr.imagetotext.converter.Utilities.UtilityLib;
import com.mkr.ocr.imagetotext.converter.Activities.ViewTextActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

public class FragmentHome extends Fragment implements IOCRCallBack {
    ImageButton buttonCamera, buttonGallery,  buttonLink;
    TextView txtResult;
    View fragment_one;
    String mImageUrl;
    String language, textName;
    int option;
    HashMap<String, String> set ;
    SharedPreferences sharedPreferences;
    private IOCRCallBack mIOCRCallBack;
    boolean editing, hasresult;
    EditText resultEdit;
    boolean  toGallery;
    FloatingActionsMenu menu;
    com.getbase.floatingactionbutton.FloatingActionButton bsave, bedit, bshare, bcopy;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_one = inflater.inflate(R.layout.fragment_home, container, false);
        editing = false;
        hasresult = false;
        toGallery = false;
        resultEdit = fragment_one.findViewById(R.id.resultEdit);
        buttonCamera = fragment_one.findViewById(R.id.imageButtonCamera);
        buttonGallery = fragment_one.findViewById(R.id.imageButtonGallery);
        buttonLink = fragment_one.findViewById(R.id.imageButtonLink);
        bsave = fragment_one.findViewById(R.id.action_save);
        bedit = fragment_one.findViewById(R.id.button_edit);
        bshare = fragment_one.findViewById(R.id.button_share);
        bcopy = fragment_one.findViewById(R.id.button_copy);
        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        toGallery = sharedPreferences.getBoolean("toGallery", false);

        String lan = sharedPreferences.getString("lang", null);
        if(lan == null){
            language = Locale.getDefault().getISO3Language();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lang", language);
            editor.apply();
        }else{
            language = lan;
        }

        mIOCRCallBack = this;
        init();
        resultEdit.setScroller(new Scroller(getActivity()));
        resultEdit.setMaxLines(10);
        resultEdit.setVerticalScrollBarEnabled(true);
        resultEdit.setMovementMethod(new ScrollingMovementMethod());
        menu = (FloatingActionsMenu) fragment_one.findViewById(R.id.multiple_actions_down);
        menu.setVisibility(View.INVISIBLE);


        return fragment_one;
    }


    private void init() {
        txtResult = (TextView) fragment_one.findViewById(R.id.result);
        txtResult.setScroller(new Scroller(getActivity()));
        txtResult.setMaxLines(10);
        txtResult.setVerticalScrollBarEnabled(true);
        txtResult.setMovementMethod(new ScrollingMovementMethod());
        txtResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasresult){
                    Intent intent = new Intent(getActivity(), ViewTextActivity.class);
                    intent.putExtra("texto", txtResult.getText().toString());
                    intent.putExtra("activity", 0);
                    intent.putExtra("name", "");
                    startActivity(intent);

                }
            }
        });
        if (buttonCamera != null) {
           buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = 0;
                    txtResult.setVisibility(View.VISIBLE);
                    resultEdit.setVisibility(View.INVISIBLE);
                    editing = false;
                    boolean hasPermissions = Utility.checkPermission(getActivity());
                    if(hasPermissions){
                        dispatchTakePictureIntent();
                    }

                }
            });
        }
        if (buttonGallery != null) {
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = 1;
                    hasresult = false;
                    selectInGalery();

                }
            });
        }
        if(buttonLink != null){
            buttonLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(getString(R.string.link));
                    alertDialog.setMessage(getString(R.string.insertImagelink));

                    final EditText input = new EditText(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setIcon(R.drawable.baseline_link_24);

                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mImageUrl = input.getText().toString();
                                    Log.d("url", mImageUrl);
                                    if (!mImageUrl.equals("")) {
                                        hasresult = false;
                                        OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(getActivity(), getActivity().getApplicationContext().getResources().getString(R.string.keyOcr), false, mImageUrl, language,mIOCRCallBack, true);
                                        oCRAsyncTask.execute();
                                    }
                                }
                            });

                    alertDialog.setNegativeButton(getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();



                }
            });
        }
        if(bshare != null){
           bshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hasresult = false;
                    share();
                }
            });
        }
        bcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilityLib.copy(getActivity(), txtResult.getText().toString());
            }
        });
        if(bsave != null){
            bsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!editing){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle(getString(R.string.fileName));
                        alertDialog.setMessage(getString(R.string.Name));

                        final EditText input = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
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
                                            set.put(textName,txtResult.getText().toString());
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            hashmapString = gson.toJson(set);
                                            editor.putString("savedDocs", hashmapString);
                                            editor.apply();
                                            Toast.makeText(getActivity(),getString(R.string.saved), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getActivity(),
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

                    }else{
                        txtResult.setText(resultEdit.getText());
                        txtResult.setVisibility(View.VISIBLE);
                        bedit.setVisibility(View.VISIBLE);
                        resultEdit.setVisibility(View.INVISIBLE);
                        bshare.setVisibility(View.VISIBLE);
                        bcopy.setVisibility(View.VISIBLE);
                        editing = false;
                        Toast.makeText(getActivity(),getString(R.string.doneEditing), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        if(bedit != null){
            bedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editing = true;
                    resultEdit.setText(txtResult.getText());
                    txtResult.setVisibility(View.INVISIBLE);
                    resultEdit.setVisibility(View.VISIBLE);
                    bedit.setVisibility(View.INVISIBLE);
                    bshare.setVisibility(View.INVISIBLE);
                    bcopy.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void getOCRCallBackResult(String response) {
        String showResult = "No Result";

        try{
            JSONObject jsonobject = new JSONObject(response);
            Log.d("response",response);
            JSONArray jsonarray = jsonobject.getJSONArray("ParsedResults");
            if(jsonarray != null && jsonarray.length() > 0){
                hasresult = true;
                showResult = jsonarray.getJSONObject(0).getString("ParsedText");

            }

        }catch (Exception e){

        }
        txtResult.setText(showResult);
        resultEdit.setText(showResult);
        menu.setVisibility(View.VISIBLE);
    }

    private void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, txtResult.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private void selectInGalery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,RESULT_LOAD_IMAGE );
    }

    Uri image_uri;
    File photoFile;
    private void dispatchTakePictureIntent() {
        toGallery = sharedPreferences.getBoolean("toGallery", false);
        if(!toGallery  /*|| Build.VERSION.SDK_INT <Build.VERSION_CODES.LOLLIPOP*/){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Make sure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                photoFile = null;
                try {
                    // Create the image file
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // An error occurred while creating the File
                }
                //Only continue if the File was successfully created
                if (photoFile != null) {
                    image_uri = FileProvider.getUriForFile(getActivity(),
                            "com.mkr.ocr.imagetotext.converter.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }


    }
    String currentPhotoPath;
    String timeStamp;
    File image;
    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        switch (requestCode){
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    dispatchTakePictureIntent();
                }

                else {
                    //permission from popup was denied
                    Toast.makeText(getActivity(), getString(R.string.deniedPermissions), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (option == 0 && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if( toGallery){
                final InputStream imageStream;
                if(toGallery){
                    galleryAddPic();
                }

                try {
                    imageStream = getActivity().getContentResolver().openInputStream(image_uri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    covertImageToBase64(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                covertImageToBase64(imageBitmap);
            }




        }else if(option == 1 && requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                covertImageToBase64(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void covertImageToBase64(Bitmap imageBitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
        try{
            baos.flush();
            baos.close();
        }catch(Exception e){

        }

        byte[] b = baos.toByteArray();
        mImageUrl = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Base 64", mImageUrl);
        mImageUrl = "data:image/png;base64,"+mImageUrl;
        executeOcr();
    }
    private void executeOcr(){
        language = sharedPreferences.getString("lang", null);
        OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(getActivity(), getActivity().getApplicationContext().getResources().getString(R.string.keyOcr), false, mImageUrl, language,mIOCRCallBack, false);
        oCRAsyncTask.execute();
    }
}