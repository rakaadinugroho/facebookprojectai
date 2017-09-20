package com.rakaadinugroho.sealmood.core.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.models.Chapter;
import com.rakaadinugroho.sealmood.models.Daily;
import com.rakaadinugroho.sealmood.utils.RecyclerViewItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Raka Adi Nugroho on 4/16/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private Context mContext;
    private List<Chapter> chapterList;
    private int layout;

    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DayAdapter(Context mContext, List<Chapter> chapterList, int layout) {
        this.mContext = mContext;
        this.chapterList = chapterList;
        this.layout = layout;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        final DayViewHolder viewHolder  = new DayViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position    = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    if (recyclerViewItemClickListener != null)
                        recyclerViewItemClickListener.onItemClick(position, viewHolder.itemView);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.day_item_dominant.setText("Nilai : "+ theBigest(chapter));
        holder.day_item_label.setText(theBiggestLabel(chapter));
        holder.day_item_created.setText(unixToDate(chapter.getCreatedAt()));
        holder.day_item_icon.setImageResource(theBiggestIcon(chapter));
    }
    private String unixToDate(long tanggal){
        Date date   = new Date(tanggal);
        SimpleDateFormat simpleDateFormat   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    private float theBigest(Chapter chapter){
        List<Float> listChapter = new ArrayList<>();
        listChapter.add(chapter.getAnger());
        listChapter.add(chapter.getContempt());
        listChapter.add(chapter.getDisgust());
        listChapter.add(chapter.getFear());
        listChapter.add(chapter.getHappy());
        listChapter.add(chapter.getContempt());
        listChapter.add(chapter.getNeutral());
        listChapter.add(chapter.getSupprised());

        Collections.sort(listChapter);

        return listChapter.get(listChapter.size()-1);
    }
    private String theBiggestLabel(Chapter chapter){
        String dataIcon[]    = new String[]{
                "Happy","Neutral","Anger","Contempt","Disgust","Fear","Sadness","Suprissed"
        };
        float biggest   = theBigest(chapter);
        if (biggest == chapter.getHappy())
            return dataIcon[0];
        else if (biggest == chapter.getNeutral())
            return dataIcon[1];
        else if (biggest == chapter.getAnger())
            return dataIcon[2];
        else if (biggest == chapter.getContempt())
            return dataIcon[3];
        else if (biggest == chapter.getDisgust())
            return dataIcon[4];
        else if (biggest == chapter.getFear())
            return dataIcon[5];
        else if (biggest == chapter.getSadness())
            return dataIcon[6];
        else if (biggest == chapter.getSupprised())
            return dataIcon[7];
        return "";
    }
    private int theBiggestIcon(Chapter chapter){
        Integer dataIcon[]    = new Integer[]{
                R.drawable.ico_happy,
                R.drawable.ico_neutral,
                R.drawable.ico_angry,
                R.drawable.ico_contempt,
                R.drawable.ico_disgust,
                R.drawable.ico_fear,
                R.drawable.ico_sad,
                R.drawable.ico_suprised
        };
        float biggest   = theBigest(chapter);
        if (biggest == chapter.getHappy())
            return dataIcon[0];
        else if (biggest == chapter.getNeutral())
            return dataIcon[1];
        else if (biggest == chapter.getAnger())
            return dataIcon[2];
        else if (biggest == chapter.getContempt())
            return dataIcon[3];
        else if (biggest == chapter.getDisgust())
            return dataIcon[4];
        else if (biggest == chapter.getFear())
            return dataIcon[5];
        else if (biggest == chapter.getSadness())
            return dataIcon[6];
        else if (biggest == chapter.getSupprised())
            return dataIcon[7];
        return 0;
    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    class DayViewHolder extends RecyclerView.ViewHolder{
        TextView day_item_created;
        TextView day_item_dominant;
        TextView day_item_label;
        ImageView day_item_icon;
        public DayViewHolder(View itemView) {
            super(itemView);
            day_item_created    = (TextView) itemView.findViewById(R.id.day_item_created);
            day_item_dominant   = (TextView) itemView.findViewById(R.id.day_item_dominant);
            day_item_label  = (TextView) itemView.findViewById(R.id.day_item_label);
            day_item_icon   = (ImageView) itemView.findViewById(R.id.day_item_icon);
        }
    }
}
