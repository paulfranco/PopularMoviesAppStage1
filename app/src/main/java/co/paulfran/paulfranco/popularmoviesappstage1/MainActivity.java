package co.paulfran.paulfranco.popularmoviesappstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    // Initialize gridView
    private GridView gridView;
    // Initialize Menu
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Complete the initialization of the gridView by binding the resources
        gridView = findViewById(R.id.gridview);
        // Set OnClickListener on the gridView
        gridView.setOnItemClickListener(listener);

        if (savedInstanceState == null) {
            // Get data from the Movie_db
            getMoviesFromTMDb(getSortMethod());
        } else {
            // Get data from local resources
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.parcel_movie));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }

                // Set the MoviePosterAdapter to the gridView
                gridView.setAdapter(new MoviePosterAdapter(this, movies));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, mMenu);

        // Make mMenu accessible
        mMenu = menu;

        // Add menu items
        mMenu.add(Menu.NONE, // No group
                R.string.pref_sort_popular, // ID SORT_POPULAR
                Menu.NONE, // Sort order: not relevant
                "Popular") // Text to display
                .setVisible(false) // set visibility
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Same settings as the one above
        mMenu.add(Menu.NONE, // No Group
                R.string.pref_sort_top_rated, //ID SORT_TOP_RATED
                Menu.NONE, // Sort order: not relevant
                "Top Rated") // Text to display
                .setVisible(false)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Update menu to show relevant items
        updateMenu();

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int numMovieObjects = gridView.getCount();
        // if there are movie objects
        if (numMovieObjects > 0) {
            // Get Movie objects from gridview
            Movie[] movies = new Movie[numMovieObjects];
            // iterate through the Movie objects
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) gridView.getItemAtPosition(i);
            }

            // Save Movie objects to bundle
            outState.putParcelableArray(getString(R.string.parcel_movie), movies);
        }

        super.onSaveInstanceState(outState);
    }
    // Options for menu when menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.string.pref_sort_popular:
                updateSharedPrefs(getString(R.string.sort_popular));
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            case R.string.pref_sort_top_rated:
                updateSharedPrefs(getString(R.string.sort_top_rated));
                updateMenu();
                getMoviesFromTMDb(getSortMethod());
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    //Listener when user clicks on a particular movie poster
    private final GridView.OnItemClickListener listener = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Movie movie = (Movie) parent.getItemAtPosition(position);

            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
            intent.putExtra(getResources().getString(R.string.parcel_movie), movie);

            startActivity(intent);
        }
    };

    private void getMoviesFromTMDb(String sortMethod) {
        // If there is internet connectivity
        if (isNetworkAvailable()) {
            // api key for theMovieDb
            String apiKey = getString(R.string.key_themoviedb);

            // Listener for when AsyncTask is ready to update UI
            OnTaskFinish taskCompleted = new OnTaskFinish() {
                @Override
                public void onFetchMoviesTaskCompleted(Movie[] movies) {
                    gridView.setAdapter(new MoviePosterAdapter(getApplicationContext(), movies));
                }
            };

            // Execute movieTask
            FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(taskCompleted, apiKey);
            movieTask.execute(sortMethod);
        } else {
            // If there is no internet connectivity
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    // Check for internet connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Update the menu
    private void updateMenu() {
        String sortMethod = getSortMethod();

        if (sortMethod.equals(getString(R.string.sort_popular))) {
            mMenu.findItem(R.string.pref_sort_popular).setVisible(false);
            mMenu.findItem(R.string.pref_sort_top_rated).setVisible(true);
        } else {
            mMenu.findItem(R.string.pref_sort_top_rated).setVisible(false);
            mMenu.findItem(R.string.pref_sort_popular).setVisible(true);
        }
    }

    // Get the sort method from the menu, by default, sort by popularity
    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        return prefs.getString(getString(R.string.pref_sort_method_key),
                getString(R.string.pref_sort_popular));
    }

    // Save the sort method
    private void updateSharedPrefs(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_sort_method_key), sortMethod);
        editor.apply();
    }
}
