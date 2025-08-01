package com.example.basicremotecall;

import android.app.Activity;
import android.widget.Toast;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RemoteUtilities {

    public static RemoteUtilities remoteUtilities = null;
    private Activity uiActivity;
    public RemoteUtilities(Activity uiActivity){

        this.uiActivity = uiActivity;
    }

    // Method to set the UI activity
    public void setUiActivity(Activity uiActivity){
        this.uiActivity = uiActivity;
    }

    // Method to get a singleton instance of RemoteUtilities
    public static RemoteUtilities getInstance(Activity uiActivity){
        if(remoteUtilities == null){
            remoteUtilities = new RemoteUtilities(uiActivity);
        }
        remoteUtilities.setUiActivity(uiActivity);
        return remoteUtilities;
    }

    // Method to open an HTTP connection to a given URL
    public HttpURLConnection openConnection(String urlString)  {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(conn == null){
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity,"Check Internet",Toast.LENGTH_LONG).show();
                }
            });

        }
        return conn;
    }

    // Method to check if the HTTP connection is okay
    public boolean isConnectionOkay(HttpURLConnection conn){
        try {
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            uiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(uiActivity,"Problem with API Endpoint",Toast.LENGTH_LONG).show();
                }
            });
        }
        return false;
    }

    // Method to get the response data as a string from the HTTP connection
    public String getResponseString(HttpURLConnection conn){
        String data = null;
        try {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = IOUtils.toByteArray(inputStream);  // Assumes IOUtils is available
            data = new String(byteData, StandardCharsets.UTF_8);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

}
