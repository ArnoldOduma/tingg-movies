package com.techspaceke.cellulant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techspaceke.cellulant.Constants;
import com.techspaceke.cellulant.R;
import com.techspaceke.cellulant.adapters.MoviesAdapter;
import com.techspaceke.cellulant.models.Movies;
import com.techspaceke.cellulant.services.MoviesService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btnBack)
    Button mBack;
    @BindView(R.id.titleTextView)
    TextView mTitle;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.grid) GridView mGrid;
    private GridView mMoviesGridView ;
    public static Movies movieDetails;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static List<Movies> moviesArrayList = new ArrayList<>();

    private MoviesAdapter mAdapter;
    private GridView gridView;

    Fragment movieDetailsFragment = new MovieDetailFragment();
    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGrid.animate().alpha(0.0f).translationY(mGrid.getHeight()/2).setDuration(0);
        mBack.animate().alpha(0.0f).setDuration(0).translationX(-mBack.getHeight());
        showProgressDialog();
        mBack.setOnClickListener(this);
        final MoviesAdapter moviesAdapter = new MoviesAdapter(this,moviesArrayList);
        getMovies(Constants.MOVIES_POPULAR);
        mMoviesGridView = findViewById(R.id.grid);
        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Movies movies = moviesArrayList.get(position);
                movieDetails = moviesArrayList.get(position);
                mBack.animate().alpha(1.0f).setDuration(500).setStartDelay(500).translationX(mBack.getHeight()).setDuration(800);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                transaction.replace(R.id.fragment_container,movieDetailsFragment);
                transaction.commit();
            }
        });

    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (view == mBack){
                transaction.remove(movieDetailsFragment);
                mBack.animate().alpha(0.0f).setDuration(100).setStartDelay(0).translationX(-mBack.getHeight()).setDuration(800);
                transaction.commit();
        }
    }

    Handler handler = new Handler();
    Runnable updateData = new Runnable() {
        @Override
        public void run() {
            getMovies(Constants.MOVIES_POPULAR);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showProgressDialog();
        switch (item.getItemId()){
            case R.id.action_filter:
                mGrid.animate().translationY(mGrid.getHeight()/4).setDuration(500).alpha(0.0f);
                getMovies(Constants.MOVIES_TOP_RATED);
                mTitle.setText(getResources().getString(R.string.top_rated));
                return true;
            case R.id.most_popular:
                mGrid.animate().translationY(mGrid.getHeight()/4).setDuration(500).alpha(0.0f);
                getMovies(Constants.MOVIES_POPULAR);
                mTitle.setText(getResources().getString(R.string.popular));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void getMovies(String category) {
        MoviesService.getMovies(category, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        handler.postDelayed(updateData, 3000);
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
                final String jsonData = responseBody.string();
                Log.e(TAG, jsonData);
                if (response.isSuccessful()) {
                    moviesArrayList = MoviesService.processResults(response);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                            mAdapter = new MoviesAdapter(getApplicationContext(), moviesArrayList);
                            mMoviesGridView = findViewById(R.id.grid);
                            mMoviesGridView.setAdapter(mAdapter);
                            hideProgressDialog();
                            mGrid.animate().translationY(0).setDuration(1000).setStartDelay(500).alpha(1.0f).setDuration(500);
                            if (jsonData.length() < 1) {
                                handler.postDelayed(updateData, 3000);
                            }
                        }
                    });
                }
            }
        });

    }


    public void showProgressDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressDialog(){
        mProgressBar.setVisibility(View.GONE);
    }


}
