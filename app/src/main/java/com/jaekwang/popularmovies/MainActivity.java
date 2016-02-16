package com.jaekwang.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Movie[] movieObjects;
    private MovieThumbnailAdapter thumbnailAdapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




    }

    @Override
    protected void onStart() {
        super.onStart();

        //thumbnailAdapter = new MovieThumbnailAdapter(this);

        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {

        FetchMoviesTask task = new FetchMoviesTask();
        task.execute();

    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            movieObjects = movies;

            gridView = (GridView) findViewById(R.id.gridview);

            //Set poster

            final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

            String[] paths = new String[movies.length];
            for(int i = 0; i < paths.length; i++) {
                paths[i] = BASE_POSTER_URL + movies[i].getPosterPath();
            }
            if (paths.length > 0) {
                gridView.setAdapter(new MovieThumbnailAdapter(getApplicationContext(), paths));
            }


        }

        @Override
        protected Movie[] doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonResponse = null;

            String format = "json";

            try {

                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String APIKEY_PARAM = "api_key";
                final String SORTBY_PARAM = "sort_by";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .appendQueryParameter(SORTBY_PARAM, "popularity.desc")
                        .appendQueryParameter("vote_count.gte", "50")
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to Movie DB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                jsonResponse= buffer.toString();

                Log.v(LOG_TAG, "JSON String: " + jsonResponse);

            } catch(IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(jsonResponse);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        private Movie[] getMovieDataFromJson(String jsonStr)
            throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MDB_LIST = "results";
            final String MDB_WEATHER = "";
            final String OWM_TEMPERATURE = "";
            final String OWM_MAX = "";
            final String OWM_MIN = "";
            final String OWM_DESCRIPTION = "";

            JSONObject jsonTopObj = new JSONObject(jsonStr);
            JSONArray MovieResults =  jsonTopObj.getJSONArray(MDB_LIST);

            ArrayList<Movie> movies = new ArrayList();
            for (int i = 0; i < MovieResults.length(); i++) {

                JSONObject jsonMovie = MovieResults.getJSONObject(i);
                String poster_path =  jsonMovie.getString("poster_path");
                boolean adult = jsonMovie.getBoolean("adult");
                String overview = jsonMovie.getString("overview");
                String release_date = jsonMovie.getString("release_date");
                int id = jsonMovie.getInt("id");
                String original_title = jsonMovie.getString("original_title");
                String title = jsonMovie.getString("title");
                String backdrop_path = jsonMovie.getString("backdrop_path");
                int vote_count = jsonMovie.getInt("vote_count");
                double vote_average = jsonMovie.getDouble("vote_average");

                Movie movie = new Movie(poster_path, adult, overview, release_date, id, original_title, title,
                        backdrop_path, vote_count, vote_average);
                movies.add(movie);
            }

            for (Movie movie : movies) {
                Log.v(LOG_TAG, "Moview: " + movie.getOriginalTitle());
            }
            return movies.toArray(new Movie[movies.size()]);
        }
    }

}
