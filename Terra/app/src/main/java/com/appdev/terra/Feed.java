package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;


public class Feed extends AppCompatActivity {
    private List<Post> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        for (int i = 1; i <= 50; i++) {
            Post post = new Post("Post " + i, "Username " + i, "Location " + i, "Level " + i);
            items.add(post);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(items));

        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Feed.this, NewPostActivity.class);
                startActivity(intent2);
            }
        });
    }
}
