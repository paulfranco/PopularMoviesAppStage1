package co.paulfran.paulfranco.popularmoviesappstage1;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;

public class MovieDetails extends AppCompatActivity {

    private final String LOG_TAG = MovieDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        setupUI();
    }

    private void setupUI() {
        // Initialize TextViews
        TextView tvOriginalTitle = findViewById(R.id.textview_original_title);
        TextView tvOverView = findViewById(R.id.textview_overview);
        TextView tvVoteAverage = findViewById(R.id.textview_vote_average);
        TextView tvReleaseDate = findViewById(R.id.textview_release_date);

        // Initialize ImageView
        ImageView ivPoster = findViewById(R.id.imageview_poster);

        // Get the Intent and data from MainActivity
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        tvOriginalTitle.setText(movie.getOriginalTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(185,278)
                .error(R.drawable.not_found)
                .placeholder(R.drawable.placeholder)
                .into(ivPoster);

        String overView = movie.getOverview();
        if (overView == null) {
            tvOverView.setTypeface(null, Typeface.NORMAL);
            overView = getResources().getString(R.string.no_summary_found);
        }
        tvOverView.setText(overView);
        tvVoteAverage.setText(movie.getDetailedVoteAverage());

        // Get the release date from the object - to be used if something goes wrong with getting localized release date.
        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {
            try {
                releaseDate = DateTimeShortForm.getLocalizedDate(this,
                        releaseDate, movie.getDateFormat());
            } catch (ParseException e) {
                Log.e(LOG_TAG, getString(R.string.cant_get_release_date), e);
            }
        } else {
            tvReleaseDate.setTypeface(null, Typeface.NORMAL);
            releaseDate = getResources().getString(R.string.no_release_date_found);
        }
        tvReleaseDate.setText(releaseDate);
    }


}
