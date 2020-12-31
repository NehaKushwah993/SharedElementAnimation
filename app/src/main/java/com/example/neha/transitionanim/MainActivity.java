package com.example.neha.transitionanim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new RecyclerViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_, mainFragment).commit();



    }


    @Override
    public void onBackPressed() {

        mainFragment.onBackPressed();

    }

}
