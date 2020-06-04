package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class handles a single review.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class Review implements Serializable {

    /** Used to retrieve review ID. */
    private static final String REVIEW_ID = "review_id";
    /** Used to retrieve reviewer. */
    private static final String REVIEWER = "reviewer";
    /** Used to retrieve reviewed. */
    private static final String REVIEWED = "reviewed";
    /** Used to retrieve review. */
    private static final String REVIEW = "review";
    /** Used to retrieve the rating. */
    private static final String RATING = "rating";
    /** Used to retrieve the role. */
    private static final String ROLE = "role";
    /** Used to retrieve the date posted. */
    private static final String DATE_POSTED ="date_posted";

    /** The review of the user. */
    private String myReview;
    /** The username of the reviewer */
    private String myReviewer;
    /** The username of the person who is being reviewed. */
    private String myReviewed;
    /** The date the review was posted. */
    private String myDatePosted;
    /** The role of the reviewer. */
    private String myRole;
    /** The ID of the review. */
    private int myReviewID;
    /** The double rating of the review. */
    private double myRating;

    /**
     * Initializes all fields for the review.
     *
     * @param myReview The review
     * @param myReviewer The reviewer
     * @param myReviewed The reviewed user
     * @param myDatePosted The date review was posted
     * @param myRole The role of reviewer
     * @param myReviewID The review ID
     * @param myRating The rating of the review
     */
    public Review(String myReview, String myReviewer, String myReviewed, String myDatePosted, String myRole,
                  int myReviewID, double myRating) {
        this.myReview = myReview;
        this.myReviewer = myReviewer;
        this.myReviewed = myReviewed;
        this.myDatePosted = myDatePosted;
        this.myRole = myRole;
        this.myReviewID = myReviewID;
        this.myRating = myRating;
    }

    /**
     * Gets my review.
     *
     * @return My review
     */
    public String getMyReview() {
        return myReview;
    }

    /**
     * Sets my review.
     *
     * @param myReview The review
     */
    public void setMyReview(String myReview) {
        this.myReview = myReview;
    }

    /**
     * Gets my reviewer.
     *
     * @return My reviewer
     */
    public String getMyReviewer() {
        return myReviewer;
    }

    /**
     * Sets my reviewer
     *
     * @param myReviewer The reviewer
     */
    public void setMyReviewer(String myReviewer) {
        this.myReviewer = myReviewer;
    }

    /**
     * Gets my reviewed user.
     *
     * @return My reviewed user
     */
    public String getMyReviewed() {
        return myReviewed;
    }

    /**
     * Sets my reviewed user.
     *
     * @param myReviewed The reviewed user
     */
    public void setMyReviewed(String myReviewed) {
        this.myReviewed = myReviewed;
    }

    /**
     * Gets my date posted.
     *
     * @return My date review was posted
     */
    public String getMyDatePosted() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try {
            Date date = format.parse(myDatePosted);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, -7);
            Date temp = cal.getTime();
            format.applyPattern("MMM dd, yyyy hh:mm a");
            return format.format(temp);
        } catch (ParseException e) {
            return myDatePosted;
        }
    }

    /**
     * Sets my review date.
     *
     * @param myDatePosted The date review was posted
     */
    public void setMyDatePosted(String myDatePosted) {
        this.myDatePosted = myDatePosted;
    }

    /**
     * Gets my role.
     *
     * @return My role
     */
    public String getMyRole() {
        return myRole;
    }

    /**
     * Sets my role.
     *
     * @param myRole The role
     */
    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    /**
     * Gets my review ID.
     *
     * @return My review ID
     */
    public int getMyReviewID() {
        return myReviewID;
    }

    /**
     * Sets my review ID.
     *
     * @param myReviewID The review ID
     */
    public void setMyReviewID(int myReviewID) {
        this.myReviewID = myReviewID;
    }

    /**
     * Gets my rating.
     *
     * @return My rating
     */
    public double getMyRating() {
        return myRating;
    }

    /**
     * Sets my review rating.
     *
     * @param myRating The review rating
     */
    public void setMyRating(double myRating) {
        this.myRating = myRating;
    }

    /**
     * Parse the JSONObject and converts it into an Review object.
     *
     * @param itemJSON The JSONObject to be parse
     * @return The Review object created from the JSONObject
     * @throws JSONException If there is an issue parsing the JSONObject
     */
    public static Review parseItemJson(JSONObject itemJSON) throws JSONException {
        JSONObject obj = itemJSON;
        Review item = new Review(
                obj.getString(Review.REVIEW),
               obj.getString(Review.REVIEWER),
                obj.getString(Review.REVIEWED),
                obj.getString(Review.DATE_POSTED),
                obj.getString(Review.ROLE),
                obj.getInt(Review.REVIEW_ID),
                obj.getDouble(Review.RATING));
        return item;
    }
}
