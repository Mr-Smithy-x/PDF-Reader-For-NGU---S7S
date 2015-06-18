package mbreader.mrsmyx.com.mbreader.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import mbreader.mrsmyx.com.mbreader.R;
import mbreader.mrsmyx.com.mbreader.interfaces.OnPDFRecyclerListener;
import mbreader.mrsmyx.com.mbreader.structs.PDF;

/**
 * Created by Charlton on 6/12/2015.
 */
public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.PDFHolder>{

    List<PDF> pdfs = new ArrayList<PDF>();
    private OnPDFRecyclerListener onPDFRecyclerListener;
    Context context;
    LayoutInflater inflater;
    public PDFAdapter(Context context, List<PDF> pdfs){
        this.context = context;
        this.pdfs = pdfs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PDFHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PDFHolder(inflater.inflate(R.layout.pdf_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(PDFHolder holder, int position) {
        PDF pdf = pdfs.get(position);
        holder.title.setText(String.format("%s",pdf.getFile().toUpperCase().replace(".PDF","")));
        holder.date.setText(String.format("Job Date: %s", pdf.getUploaded()));
        holder.create.setText(String.format("Material: %s", pdf.getType()));
        holder.job.setText(String.format("JOB #%s",pdf.getJob()));
        TextDrawable td = TextDrawable.builder().buildRound(String.valueOf(pdf.getFile().charAt(0)).toUpperCase(), context.getResources().getColor(R.color.md_blue_500));
        holder.imageView.setImageDrawable(td);
    }

    @Override
    public int getItemCount() {
        return pdfs.size();
    }

    public PDF getItemAtPosition(int childPosition) {
        return pdfs.get(childPosition);
    }

    public void appendItem(PDF override) {
        if(!pdfs.contains(override)) {
            pdfs.add(override);
            notifyItemInserted(pdfs.size());
        }
    }

    public void clear() {
        int size = pdfs.size();
        pdfs.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class PDFHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        AppCompatTextView title, date, create, job;
        public PDFHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.pdf_icon);
            title = (AppCompatTextView) itemView.findViewById(R.id.pdf_title);
            create = (AppCompatTextView) itemView.findViewById(R.id.pdf_by);
            date = (AppCompatTextView) itemView.findViewById(R.id.pdf_date);
            job = (AppCompatTextView)itemView.findViewById(R.id.pdf_job);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
