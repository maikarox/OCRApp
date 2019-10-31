package com.mkr.ocr.imagetotext.converter.Utilities;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class UtilityLib
{
    public static void rateTheApp(Context context, String marketUrl, String webUrl)
    {
        try
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)));
        }
        catch (ActivityNotFoundException anfe)
        {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)));
        }
    }
    public static void openWeb(final Context context, final String url)
    {
        try
        {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            return;
        }
        catch (ActivityNotFoundException ex)
        {
            ex.printStackTrace();
            Toast.makeText(context, "Error opening website", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public static void copy(final Context context, String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT);
    }

}