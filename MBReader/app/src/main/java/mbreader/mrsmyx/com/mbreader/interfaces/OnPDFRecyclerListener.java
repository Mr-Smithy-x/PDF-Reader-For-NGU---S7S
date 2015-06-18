package mbreader.mrsmyx.com.mbreader.interfaces;

import android.view.View;

import mbreader.mrsmyx.com.mbreader.structs.PDF;

/**
 * Created by Charlton on 6/12/2015.
 */
public interface OnPDFRecyclerListener  {
    public void OnPDFClicked(View view, int position, PDF pdf);
    public void OnPDFLongClicked(View view, int position, PDF pdf);
}
