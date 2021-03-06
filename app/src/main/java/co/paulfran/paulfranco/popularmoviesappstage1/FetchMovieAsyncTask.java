package co.paulfran.paulfranco.popularmoviesappstage1;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class FetchMovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

    private final String LOG_TAG = FetchMovieAsyncTask.class.getSimpleName();
    private final String mApiKey;
    private final OnTaskFinish mListener;

    public FetchMovieAsyncTask(OnTaskFinish listener, String apiKey) {
        super();

        mListener = listener;
        mApiKey = apiKey;
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Set the moviesJsonString to null
        String moviesJsonString = null;

        try {
            URL url = getApiUrl(params);

            // Start the URL connection
            urlConnection = (HttpURLConnection) url.openConnection();
            // Set the request to GET
            urlConnection.setRequestMethod("GET");
            // Complete the connection
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            // If the inputStream is empty return null
            if (inputStream == null) {
                return null;
            }
            // Else read the data in the inputStream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Adds '\n' after each line (new line)
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                // No data found.
                return null;
            }

            moviesJsonString = builder.toString();

        } catch (IOException e) {
            // Log the Error
            Log.e(LOG_TAG, "Error: ", e);
            return null;

        } finally {
            // Release url connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    // Log the Error in closing the stream
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            // Get the JSON data needed
            return getMoviesDataFromJson(moviesJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Extracts data from the JSON object and returns an Array of movie objects.
     *
     * @param moviesJsonString JSON string to be traversed
     * @return Array of Movie objects
     * @throws JSONException
     */
    private Movie[] getMoviesDataFromJson(String moviesJsonString) throws JSONException {
        // Initialize Json Tags
        final String TAG_RESULTS = "results";
        final String TAG_ORIGINAL_TITLE = "original_title";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_OVERVIEW = "overview";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";

        // Get the array containing the movies found
        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

        // Create array of Movie objects that stores data from the JSON string
        Movie[] movies = new Movie[resultsArray.length()];

        // Iterate through movies one by one and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            movies[i] = new Movie();

            // Object contains all tags we're looking for
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            // Store data in movie object
            movies[i].setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(TAG_OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));

        }

        return movies;
    }

    /**
     * Creates and returns an URL.
     *
     * @param parameters Parameters to be used in the API call
     * @return URL formatted with parameters for the API
     * @throws MalformedURLException
     */
    private URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String TMDB_BASE_URL = "https://api.themoviedb.org";
        final String API_KEY_PARAM = "api_key";

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath("3")
                .appendPath("movie")
                .appendPath(parameters[0]) //add /top_rated or /popular to the url
                .appendQueryParameter(API_KEY_PARAM, mApiKey) //add the api key
                .build();
        // Log the final Url
        Log.d(TAG, "outcome = " + builtUri);
        return new URL(builtUri.toString());

    }

    //For debuging the final url
    private static final String TAG = "Debug FetchMovieAsync";

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        // Notify UI
        mListener.onFetchMoviesTaskCompleted(movies);
    }


}
