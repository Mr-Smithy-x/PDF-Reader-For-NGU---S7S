package mbreader.mrsmyx.com.mbreader;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mbreader.mrsmyx.com.mbreader.adapters.PDFAdapter;
import mbreader.mrsmyx.com.mbreader.async.AsyncPDFDownloader;
import mbreader.mrsmyx.com.mbreader.interfaces.OnFileListener;
import mbreader.mrsmyx.com.mbreader.interfaces.OnPDFRecyclerListener;
import mbreader.mrsmyx.com.mbreader.structs.PDF;
import mbreader.mrsmyx.com.mbreader.utils.WebClient;

public class MainActivity extends AppCompatActivity implements OnFileListener, OnPDFRecyclerListener, View.OnClickListener {
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.floating_action){
            pdfAdapter.clear();
            downloadStringDetails(getString(R.string.url));

            return;
        }
    }

    public class PDFTouchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        private Context context;
        private OnPDFRecyclerListener onPDFRecyclerListener;
        public PDFTouchListener(Context context, final RecyclerView recyclerView, final OnPDFRecyclerListener onPDFRecyclerListener){
            this.context = context;
            this.onPDFRecyclerListener = onPDFRecyclerListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (view != null && onPDFRecyclerListener != null) {
                        onPDFRecyclerListener.OnPDFLongClicked(view, recyclerView.getChildPosition(view), pdfAdapter.getItemAtPosition(recyclerView.getChildPosition(view)));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null && onPDFRecyclerListener != null && e.getAction() == MotionEvent.ACTION_UP) {
                onPDFRecyclerListener.OnPDFClicked(view, recyclerView.getChildPosition(view), pdfAdapter.getItemAtPosition(recyclerView.getChildPosition(view)));
                return false;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    PDFAdapter pdfAdapter;
    RecyclerView recyclerView;
    PDFTouchListener pdfTouchListener;

    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("MBReader");
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(5);
        recyclerView.addOnItemTouchListener(pdfTouchListener = new PDFTouchListener(this, recyclerView, this));
        pdfAdapter = new PDFAdapter(this, new ArrayList<PDF>());
        recyclerView.setAdapter(pdfAdapter);
        downloadStringDetails(getString(R.string.url));

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action);
        floatingActionButton.setRippleColor(getResources().getColor(R.color.md_blue_500));
        floatingActionButton.setImageResource(R.mipmap.ic_cached_white_48dp);
        floatingActionButton.setOnClickListener(this);
        Toast.makeText(this,"This Application Was Developed By Mr Smithy x, Please take the time to learn off of this PDFReader. Library used = com.joanzapata.pdfview:android-pdfview:1.0.3@aar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnFileDownloaded(File file, String filename, View view) {
        Intent intent = new Intent(this, PDFActivity.class);
        intent.putExtra("file",file.getPath() + "/"+filename);
        intent.putExtra("short",filename);

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
        startActivity(intent, compat.toBundle());
    }

    @Override
    public void OnFileFailed(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }
    public MainActivity getActivity(){
        return this;
    }

    Handler handler = new Handler();
    public void downloadStringDetails(final String link){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String json = WebClient.downloadString(link);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OnJsonLoaded(json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Could not load files :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void OnJsonLoaded(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("pdfs");
        for(int i = 0; i < jsonArray.length();i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            String link = obj.getString("link");
            String filename = obj.getString("filename");
            String date = obj.getString("date");
            String created=obj.getString("createdby");
            String type = obj.getString("type");
            String job = obj.getString("job");
            pdfAdapter.appendItem(PDF.Builder().Override(filename,link,created, date, type, job));
        }
    }


    @Override
    public void OnPDFClicked(View view, int position, PDF pdf) {
        new AsyncPDFDownloader(this,this, pdf.getFile(), view).execute(pdf.getLink(),android.os.Environment.getExternalStorageDirectory().getPath() + "/JOB/", pdf.getFile());
    }

    @Override
    public void OnPDFLongClicked(View view, int position, PDF pdf) {

    }
}
