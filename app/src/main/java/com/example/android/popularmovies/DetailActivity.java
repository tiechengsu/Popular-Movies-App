package com.example.android.popularmovies;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.popularmovies.data.FavoritesService;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by harry on 3/15/17.
 */

public class DetailActivity extends AppCompatActivity{
    private final static String TAG = DetailActivity.class.getSimpleName();

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private CoordinatorLayout mCoordinatorLayout;

    private ImageView mBackdrop;

    private FloatingActionButton mFloatingActionButton;

    Movie mMovie;

    private FragmentManager mFragmentManager = getFragmentManager();

    private FavoritesService mFavoritesService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState==null){
            DetailActivityFragment mDetailActivityFragment = new DetailActivityFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.detail_container,mDetailActivityFragment)
                    .commit();
        }

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        mBackdrop = (ImageView) findViewById(R.id.backdrop);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating);

        mFavoritesService = new FavoritesService(this);

        Intent intent = getIntent();
        if(intent!=null&&intent.hasExtra(getString(R.string.movie_detail))){
            mMovie = intent.getParcelableExtra(getString(R.string.movie_detail));
        }

        Picasso.with(this).load(mMovie.getBackDrop()).into(mBackdrop);

        updateFloating();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mFavoritesService.isFavorite(mMovie)){
                    mFavoritesService.removeFromFavorites(mMovie);
                    Snackbar.make(mCoordinatorLayout,R.string.message_removed_from_favorites,Snackbar.LENGTH_LONG).show();
                }else{
                    mFavoritesService.addToFavorites(mMovie);
                    Snackbar.make(mCoordinatorLayout,R.string.message_added_to_favorites,Snackbar.LENGTH_LONG).show();
                }
                updateFloating();
            }
        });

        initToolbar();

    }

    private void updateFloating(){
        if(mFavoritesService.isFavorite(mMovie)){
            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_white);
        }else{
            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_white_border);
        }
    }

    private void initToolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this,android.R.color.transparent));
        setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
