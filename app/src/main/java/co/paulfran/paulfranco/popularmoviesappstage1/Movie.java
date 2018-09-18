package co.paulfran.paulfranco.popularmoviesappstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private Double mVoteAverage;
    private String mReleaseDate;

    /**
     * Constructor
     */
    public Movie() {
    }

    /**
     * GETTERS AND SETTERS
     */

    /**
     * Set Movie Title
     * originalTitle
     */
    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    /**
     * Set Movie Poster Path
     * posterPath
     */
    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    /**
     * Set Movie Overview
     * overview
     */
    public void setOverview(String overview) {
        if(!overview.equals("null")) {
            mOverview = overview;
        }
    }

    /**
     * Set Average Vote
     * voteAverage
     */
    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }

    /**
     * Set Release Date
     * releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        if(!releaseDate.equals("null")) {
            mReleaseDate = releaseDate;
        }
    }

    /**
     * Get Movie Title
     * mOriginalTitle
     */
    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    /**
     * Get Poster Path
     * mPosterPath
     */
    public String getPosterPath() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + mPosterPath;
    }

    /**
     * Get Movie Overview
     * mOverview
     */
    public String getOverview() {
        return mOverview;
    }

    /**
     * Get Average Vote
     * mVoteAverage
     */
    private Double getVoteAverage() {
        return mVoteAverage;
    }

    /**
     * Get Release Date
     * mReleaseDate
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

    /**
     * Get Movie Score
     * voteAverage
     */
    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }

    /**
     * Get Date Format
     * DATE_FORMAT
     */
    public String getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeValue(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    private Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        mReleaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
