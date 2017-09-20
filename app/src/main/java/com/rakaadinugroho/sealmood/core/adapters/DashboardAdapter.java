package com.rakaadinugroho.sealmood.core.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.models.Category;
import com.rakaadinugroho.sealmood.utils.RecyclerViewItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Raka Adi Nugroho on 4/9/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private int layout;

    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DashboardAdapter(Context context, List<Category> categoryList, int layout) {
        this.context = context;
        this.categoryList = categoryList;
        this.layout = layout;
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        final DashboardViewHolder viewHolder    = new DashboardViewHolder(view);

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
    public void onBindViewHolder(final DashboardViewHolder holder, int position) {
        Category category   = categoryList.get(position);
        int total_data = category.getDailies().size();

        holder.category_name.setText(category.getTitle());
        holder.category_total.setText(total_data + " Data");
        holder.category_created.setText("Dibuat pada : " + dateFormater(category.getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder{
        TextView category_name;
        TextView category_total;
        TextView category_created;
        Button category_option;
        public DashboardViewHolder(View itemView) {
            super(itemView);
            category_name   = (TextView) itemView.findViewById(R.id.dashboard_category_name);
            category_total  = (TextView) itemView.findViewById(R.id.dashboard_category_total);
            category_created    = (TextView) itemView.findViewById(R.id.dashboard_category_created);
            category_option = (Button) itemView.findViewById(R.id.dashboard_option);
        }
    }

    private String dateFormater(Date date){
        return DateFormat.getDateInstance().format(date);
    }
}
