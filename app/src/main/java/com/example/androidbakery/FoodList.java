package com.example.androidbakery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.androidbakery.Interface.ItemClickListener;
import com.example.androidbakery.Model.Food;
import com.example.androidbakery.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodlist;
    String categoryId="";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database=FirebaseDatabase.getInstance();
        foodlist=database.getReference("Foods");
        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            categoryId=getIntent().getStringExtra("CategortId");
        if(!categoryId.isEmpty()&&categoryId!=null)
        {
            loadListFood(categoryId);
        }
    }

    private void loadListFood(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,FoodViewHolder.class,
                foodlist.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
