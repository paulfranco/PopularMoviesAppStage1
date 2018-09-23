package co.paulfran.paulfranco.popularmoviesappstage1;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private GridView gridView;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);

        if (savedInstanceState == null) {
            // Get data from the Internet

        } else {
            // Get data from local resources
            // Get Movie objects
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.parcel_movie));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                // Load movie objects into view
                gridView.setAdapter(new MoviePosterAdapter(this, movies));
            }
        }

    }
}
