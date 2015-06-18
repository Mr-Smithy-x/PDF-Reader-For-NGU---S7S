package mbreader.mrsmyx.com.mbreader.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Charlton on 6/11/2015.
 */
public class WebClient {
    public static File downloadFile(String link, String dest, String filename) throws IOException {
        File f = new File(dest);
        if(!f.exists()){
            f.mkdirs();
        }else if(new File(dest + "/" + filename).exists()){
            Log.e("File Exist","The File Exist");
            return new File(dest);
        }
        URL url = new URL(link);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.connect();

        FileOutputStream fis = new FileOutputStream(dest + "/" + filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = httpURLConnection.getInputStream();
        byte[] buffer = new byte[1024];
        int i;
        while((i = is.read(buffer)) != -1){
            baos.write(buffer,0,i);
        }
        is.close();
        baos.close();
        httpURLConnection.disconnect();
        fis.write(baos.toByteArray());
        fis.flush();
        fis.close();
        return f.getAbsoluteFile();
    }
    public static String downloadString(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String buffer = "";
        while((buffer = br.readLine())!= null){
            sb.append(buffer);
        }
        br.close();
        httpURLConnection.disconnect();
        return sb.toString();
    }
}
