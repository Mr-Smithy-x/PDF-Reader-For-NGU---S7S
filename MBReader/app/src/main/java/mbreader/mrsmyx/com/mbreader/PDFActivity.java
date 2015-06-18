package mbreader.mrsmyx.com.mbreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;

import java.io.File;

public class PDFActivity extends AppCompatActivity {

    PDFView pdfView;
    String file, fshort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfview);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        file = getIntent().getStringExtra("file");
        fshort = getIntent().getStringExtra("short");
        getSupportActionBar().setTitle(fshort);
        getSupportActionBar().setSubtitle(file);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
    pdfView.fromFile(new File(file))
            .defaultPage(1)
            .showMinimap(false)
            .enableSwipe(true)
            .load();
}catch (Exception ex){
    Toast.makeText(this,file, Toast.LENGTH_SHORT).show();
    ex.printStackTrace();
}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pd, menu);
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
        }else if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
