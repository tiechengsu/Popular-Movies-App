package com.example.android.popularmovies;



import android.app.FragmentManager;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManger = getFragmentManager();

    private final static String FRAGMENT_KEY = "fragment_key";



    MovieListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(savedInstanceState==null){
            mFragment = new MovieListFragment();
            mFragmentManger.beginTransaction()
                    .add(R.id.container,mFragment)
                    .commit();
        }else{
            mFragment = (MovieListFragment) mFragmentManger.getFragment(savedInstanceState,FRAGMENT_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManger.putFragment(outState,FRAGMENT_KEY,mFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent startSettingActivity = new Intent(this,SettingsActivity.class);
            startActivity(startSettingActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
