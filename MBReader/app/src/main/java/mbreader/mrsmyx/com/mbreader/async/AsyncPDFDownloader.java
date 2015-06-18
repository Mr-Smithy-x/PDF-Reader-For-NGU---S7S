package mbreader.mrsmyx.com.mbreader.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.io.IOException;

import mbreader.mrsmyx.com.mbreader.interfaces.OnFileListener;
import mbreader.mrsmyx.com.mbreader.utils.WebClient;

/**
 * Created by Charlton on 6/11/2015.
 */
public class AsyncPDFDownloader extends AsyncTask<String, Integer, File> {

    private Context context;
    private OnFileListener onFileListener;
    private String filename;
    private View view;
    ProgressDialog pd;
    public AsyncPDFDownloader(Context context,OnFileListener onFileListener, String filename ,View view){
        this.context = context;
        this.onFileListener = onFileListener;
        this.filename = filename;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Downloading PDF Please Wait...");
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected File doInBackground(String... params) {
        try {
            return WebClient.downloadFile(params[0], params[1], params[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(pd.isShowing()){
            pd.dismiss();
        }
        if(file != null){
            onFileListener.OnFileDownloaded(file, filename, view);
        }else{
            onFileListener.OnFileFailed("Could Not Download The File =(");
        }
    }
}
