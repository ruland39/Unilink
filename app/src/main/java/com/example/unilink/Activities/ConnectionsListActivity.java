package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;


public class ConnectionsListActivity extends AppCompatActivity {
    private ConnectionsRowAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int DATASET_COUNT = 20;
    private String[] dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections_list);
        RecyclerView recyclerView = findViewById(R.id.connections_recyclerview);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        adapter = new ConnectionsRowAdapter(dataset);
        recyclerView.setAdapter(adapter);

    }

//    private void initDataset(){
//        dataset = new String[DATASET_COUNT];
//    }

}