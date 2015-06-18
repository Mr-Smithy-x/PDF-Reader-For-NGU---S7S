package mbreader.mrsmyx.com.mbreader.interfaces;

import android.view.View;

import java.io.File;

/**
 * Created by Charlton on 6/11/2015.
 */
public interface OnFileListener {
    public void OnFileDownloaded(File file, String filename, View view);
    public void OnFileFailed(String error);
}
