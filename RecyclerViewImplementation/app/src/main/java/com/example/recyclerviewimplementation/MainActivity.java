package com.example.recyclerviewimplementation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class MainActivity extends AppCompatActivity {

    private static final  int PERMISSION_STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = null;
        Variables.pos = -1;

        RecyclerView programmingList = (RecyclerView) findViewById(R.id.programmingList);
        ArrayList<ArrayList<String>> getTitles=new ArrayList<>();


        programmingList.setLayoutManager(new LinearLayoutManager(this));
        getTitles = getJson();
        Variables.url = getTitles.get(3);

        programmingList.setAdapter(new programmingAdpater(getTitles));

        //THIS WAS A TRY CASE BELOW
        //DOWNLOADING START ON DEFAULT  , BUT I AM CALLING START DOWNLOAD FUNCTION FROM ADPTER CLASS BUT IT
        //IS THROEING AN EXCEPTION HERE IN MAIN ACTIVITY WHILE DOWNLOADING
        //......................................................................
        Variables.pos = 3;
        startDownloadingProcess();
        //.....................................................................


    }
    public void startDownloadingProcess()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);
            }
            else
            {
                //START DOWNLOADING
                startDownloading();
            }
        }
        else
        {
            //start downloading
            startDownloading();
        }
    }
    public void startDownloading()
    {

       try
       {
           String url = Variables.url.get(Variables.pos).trim();
           DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
           request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
           request.setTitle("Download");
           request.setDescription("Downloading File...");
           request.allowScanningByMediaScanner();
           request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
           request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());
           DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
           manager.enqueue(request);
       }
       catch (Exception exc)
       {
           return;
       }

    }
    public void onRequestPermissionResult(int requestCode , String[] permissions , int[] grantResults )
    {
        switch (requestCode)
        {
            case PERMISSION_STORAGE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    startDownloading();
                }
                else
                {
                    Toast.makeText(this , "Permission Denied",Toast.LENGTH_SHORT).show();
                }
        }
    }
    public ArrayList<ArrayList<String>> getJson()
    {
        String json=null;
        try
        {
            ArrayList<String> title = new ArrayList<>();
            ArrayList<String> level = new ArrayList<>();
            ArrayList<String> info = new ArrayList<>();
            ArrayList<String> url = new ArrayList<>();
            ArrayList<ArrayList<String>> together = new ArrayList<>();

            InputStream is =getAssets().open("books.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json=new String(buffer,"UTF-8");
            JSONArray jsonArr = new JSONArray(json);

            for(int i = 0; i<jsonArr.length();i++)
            {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                title.add(jsonObj.getString("title"));
                level.add(jsonObj.getString("level"));
                info.add(jsonObj.getString("info"));
                url.add(jsonObj.getString("url"));
            }

            together.add(title);
            together.add(level);
            together.add(info);
            together.add(url);

            return together;

        }
        catch (IOException|JSONException exc)
        {
            exc.printStackTrace();
            return null;
        }
    }


    public static class Variables
    {
        public static int pos;
        public static ArrayList<String> url = new ArrayList<>();

    }
}
