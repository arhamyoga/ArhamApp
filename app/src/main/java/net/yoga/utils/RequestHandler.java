package net.yoga.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {

    public String sendGetRequest(String id)
    {
        URL url;
        StringBuilder sb=new StringBuilder();
        try
        {
            url=new URL("https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v="+id+"&format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("url connection",""+conn);
            int responseCode = conn.getResponseCode();
            Log.d("response code",""+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null)
                {
                    sb.append(response);
                }

                Log.d("result",""+sb);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
