package com.techspaceke.cellulant.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techspaceke.cellulant.R;
import com.techspaceke.cellulant.models.Movies;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    public Movies movie;
    @BindView(R.id.desc_title) TextView mdescTittle;
    @BindView(R.id.desc_rating) TextView mdescRating;
    @BindView(R.id.desc_release) TextView mdescRelease;
    @BindView(R.id.desc_plot) TextView mdescPlot;
    @BindView(R.id.trailer) TextView mTrailer;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,v);
        mTrailer.setOnClickListener(this);
        movie = MainActivity.movieDetails;
        String rating = getResources().getString(R.string.rating) + " " + movie.getVoteAverage().toString();
        String release = getResources().getString(R.string.release) + " " + movie.getReleaseDate();
        String plot = getResources().getString(R.string.plot) + " " + movie.getOverview();
        mdescTittle.setText(movie.getOriginalTitle());
        mdescRating.setText(rating);
        mdescRelease.setText(release);
        mdescPlot.setText(plot);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view == mTrailer){
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+ movie.getVideo()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+ movie.getVideo()));
            try {
                getContext().startActivity(appIntent);
            }catch (ActivityNotFoundException ex){
                getContext().startActivity(webIntent);
            }
        }
    }
}
