package model;

import java.io.Serializable;

/**
 * This class handles the item used when posting
 * the item to the database.
 *
 * @author TCSS 450 Team 8
 * @version 1.0
 */
public class EmailItem implements  Serializable{

        /** Used to retrieve User ID. */
        public static final String USER_ID = "user_id";
        /** Used to retrieve fname ID. */
        public static final String FNAME_ID = "fname_id";
        /** Used to retrieve the lname ID. */
        public static final String LNAME_ID = "lname_id";
        /** Used to retrieve the email ID. */
        public static final String EMAIL_ID = "email_id";


        /** The user ID. */
        private int mUserID;
        /** The fname ID. */
        private String mFNameID;
        /** The lname ID. */
        private String mLNameID;
        /** The email ID. */
        private String mEmailID;


        /**
         * Initializes the fields of the item.
         *
         * @param theUserID The user ID
         * @param theFName The Fname
         * @param theLName The lName
         * @param theEmail The Email
\
         */
        public EmailItem(int theUserID, String theFName, String theLName, String theEmail) {
            mUserID = theUserID;
            mFNameID = theFName;
            mLNameID = theLName;
            mEmailID = theEmail;
        }

        /**
         * Gets the member ID.
         *
         * @return The member ID
         */
        public String getmUserID() {
        return Integer.toString(mUserID);
    }

        /**
         * Gets the title.
         *
         * @return The title
         */
        public String getmFName() {
            return mFNameID;
        }

        /**
         * Gets the location.
         *
         * @return The item location
         */
        public String getmLName() {
            return mLNameID;
        }

        /**
         * Gets the item ID.
         *
         * @return The item ID
         */
        public String getmEmail() {
            return mEmailID;
        }

}
