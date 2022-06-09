package com.a2.essteling.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.a2.essteling.R;
import com.a2.essteling.ScoreBoard.PlayerList;

public class ShopActivity extends AppCompatActivity implements ShopItemListener {
    private static final String LOG_TAG = ShopActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ShopListAdapter mAdapter;
    private TextView totalPoints;

    private ShopItem[] items = ShopitemList.getShopItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Red));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //get the recyclerview
        mRecyclerView = findViewById(R.id.ShopRecyclerView);

        //create the adapter
        mAdapter = new ShopListAdapter(this, items, this);

        //give the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //make the recyclerview a grid layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        totalPoints = findViewById(R.id.totalPointsShop);

        totalPoints.setText("Total points: " + PlayerList.totalPoints());



    }

    //react to clicked items
    @Override
    public void onItemClicked(ShopItem item) {
        System.out.println(item.getName());
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("item", item);

        startActivity(intent);
    }
}