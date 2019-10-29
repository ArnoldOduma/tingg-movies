package com.techspaceke.cellulant.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techspaceke.cellulant.R;
import com.techspaceke.cellulant.models.Movies;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    public Movies movie;
    @BindView(R.id.desc_title) TextView mdescTittle;
    @BindView(R.id.desc_rating) TextView mdescRating;
    @BindView(R.id.desc_release) TextView mdescRelease;
    @BindView(R.id.desc_plot) TextView mdescPlot;
    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movie = MainActivity.movieDetails;
                ;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,v);
        mdescTittle.setText(movie.getOriginalTitle());
        mdescRating.setText(movie.getVoteAverage().toString());
        mdescRelease.setText(movie.getReleaseDate());
        mdescPlot.setText(movie.getOverview());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
