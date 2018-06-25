package com.rakaadinugroho.sealmood.core.activities.dashboard;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.rakaadinugroho.sealmood.BaseApps;
import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.core.activities.DayActivity;
import com.rakaadinugroho.sealmood.core.adapters.DashboardAdapter;
import com.rakaadinugroho.sealmood.models.Category;
import com.rakaadinugroho.sealmood.models.Daily;
import com.rakaadinugroho.sealmood.utils.RecyclerViewItemClickListener;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class Dashboard extends Fragment implements RealmChangeListener {
    private Context mContext;
    private Realm realm;

    private RecyclerView recyclerDashboard;
    private TextView no_data;
    private FloatingActionButton fabAdd;

    private DashboardAdapter adapter;
    private RealmResults<Category> categories;

    private long total_data;

    private LinearLayoutManager layoutManager;
    private AppEventsLogger mLogger;

    public Dashboard() {
        mContext    = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);
        if (FacebookSdk.isInitialized()){
            //Toast.makeText(mContext, "Terinisialisasi", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(mContext, "Tidak Terinisialisasi", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /* Initialize Component */
    private void initViews(View view) {
        recyclerDashboard   = (RecyclerView) view.findViewById(R.id.recycler_dashboard);
        no_data = (TextView) view.findViewById(R.id.dashboard_no_data);
        fabAdd  = (FloatingActionButton) view.findViewById(R.id.fab_dashboard_add);
        realm   = Realm.getDefaultInstance();
        mContext    = getContext();


        total_data  = realm.where(Category.class).count();
        mLogger = AppEventsLogger.newLogger(getContext());

        /*
        registerForContextMenu(recyclerDashboard);
         */
        adapter = new DashboardAdapter(mContext, categories, R.layout.item_dashboard);
        layoutManager   = new LinearLayoutManager(mContext);
        recyclerDashboard.setLayoutManager(layoutManager);
        recyclerDashboard.setAdapter(adapter);

        /*
        Click View Data
        */
        adapter.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                Intent intent   = new Intent(mContext, DayActivity.class);
                intent.putExtra("id", categories.get(position).getId());
                startActivity(intent);

            }
        });
        if (total_data <= 0){
            /*
            No Data
             */
            no_data.setVisibility(TextView.VISIBLE);
        }
        setUpContent();
    }

    private void setUpContent() {
        categories  = realm.where(Category.class).findAll();
        categories.addChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
        Click Add Data ( Category )
         */
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategory("Create new", "masukan nama kategori secara spesifik");
            }
        });
    }

    private void createCategory(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);

        View v  = LayoutInflater.from(mContext).inflate(R.layout.dialog_create, null);
        builder.setView(v);

        final EditText input    = (EditText) v.findViewById(R.id.edit_title);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String categoryName = input.getText().toString().trim();
                if (categoryName.length() > 0)
                    createNewCategory(categoryName);
                else
                    Toast.makeText(mContext, "Masukan Data Secara Spesifik", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void createNewCategory(String name) {
        realm.beginTransaction();
        Category category   = new Category(name);
        realm.copyToRealm(category);
        realm.commitTransaction();

        mLogger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);
        mLogger.logEvent("menambahkan data");
    }

    @Override
    public void onChange(Object element) {
        try {
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Toast.makeText(mContext, "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(categories.get(info.position).getTitle());
        getActivity().getMenuInflater().inflate(R.menu.context_menu_dashboard, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.data_delete:
                return true;
            case R.id.data_update:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
