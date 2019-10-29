package com.techspaceke.cellulant.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

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
    private GridView mMoviesGridView ;
    public static Movies movieDetails;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String[] moviesArray = new String[]{"jonnas", "Star wars", "Airbender","mozzat","jonnas", "Star wars", "Airbender","mozzat"};

    private static List<Movies> moviesArrayList = new ArrayList<>();
    private Integer[] arrayInt = new Integer[]{
            0,1,2,3,4,5,6,7,8,9
    };

    private ArrayAdapter<String> adapterString;
    private ArrayAdapter<Integer> adapterInteger;
    private MoviesAdapter mAdapter;
    private GridView gridView;

    Fragment movieDetailsFragment = new MovieDetailFragment();
    FragmentManager fm = getSupportFragmentManager();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBack.setVisibility(View.INVISIBLE);
        adapterString = new ArrayAdapter<>(this, R.layout.movie_view,moviesArray);
        final MoviesAdapter moviesAdapter = new MoviesAdapter(this,moviesArrayList);
        getPopularMovies();
        mMoviesGridView = findViewById(R.id.grid);
        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                Movies movies = moviesArrayList.get(position);
                movieDetails = moviesArrayList.get(position);
                mBack.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                transaction.replace(R.id.fragment_container,movieDetailsFragment);
                transaction.commit();
                String overView = moviesArrayList.get(position).getOverview();
            }
        });

    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (view == mBack){
            if (movieDetailsFragment.isAdded()){
                transaction.remove(movieDetailsFragment);
                mBack.setVisibility(View.INVISIBLE);
            }else {
                transaction.add(R.id.fragment_container, movieDetailsFragment);
                mBack.setVisibility(View.VISIBLE);
            }
            transaction.commit();
        }
    }



    public void getPopularMovies(){
        MoviesService.getPopularMovies(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Error getting Movies",Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
                final String jsonData = responseBody.string();
                Log.e(TAG, jsonData);
                if (response.isSuccessful()){
                    moviesArrayList = MoviesService.processResults(response);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Movies fetched successfully",Toast.LENGTH_LONG).show();
                            mAdapter = new MoviesAdapter(getApplicationContext(), moviesArrayList);
                            mMoviesGridView = findViewById(R.id.grid);
                            mMoviesGridView.setAdapter(mAdapter);

                            if(jsonData.length() < 1){
                                Toast.makeText(MainActivity.this,"Error fetching Movies",Toast.LENGTH_LONG).show();
                            }
                        }


                    });

                }
            }
        });

    }
}
