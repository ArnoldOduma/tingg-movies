package com.techspaceke.cellulant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techspaceke.cellulant.R;
import com.techspaceke.cellulant.models.Movies;

import java.util.List;

public class MoviesAdapter extends BaseAdapter {
    String TAG = MoviesAdapter.class.getSimpleName();
    private List<Movies> mMovies ;
    private Context mContext;

    public MoviesAdapter(Context context, List<Movies> movies){
        super();
        mContext = context;
        mMovies = movies;

    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Movies movies = mMovies.get(position);
        if (view == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.movie_layout, null);
            final ImageView imageView = (ImageView)view.findViewById(R.id.movie_thumb);
            final TextView title = (TextView)view.findViewById(R.id.movie_title);
            final ViewHolder viewHolder = new ViewHolder(title,title,imageView);
            view.setTag(viewHolder);
        }
        final ViewHolder viewHolder = (ViewHolder)view.getTag();
        ImageView mMovieThumb = view.findViewById(R.id.movie_thumb);
        Picasso.get().load(movies.getPosterPath()).into(viewHolder.imageViewCoverArt);
        viewHolder.nameTextView.setText(movies.getOriginalTitle());

        return view;
    }

    private class ViewHolder implements View.OnClickListener {
        private final TextView nameTextView;
        private final TextView authorTextView;
        private final ImageView imageViewCoverArt;

        public ViewHolder(TextView nameTextView, TextView authorTextView, ImageView imageViewCoverArt) {
            this.nameTextView = nameTextView;
            this.authorTextView = authorTextView;
            this.imageViewCoverArt = imageViewCoverArt;
        }

        @Override
        public void onClick(View view) {
//            int itemPosition = getItem();
            Toast.makeText(mContext,"Error fetching Movies",Toast.LENGTH_LONG).show();
        }
    }


}
