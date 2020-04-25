package edu.tacoma.uw.nguyen97.courseswebservicesapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private String mCourseId;
    private String mCourseShortDesc;
    private String mCourseLongDesc;
    private String mCoursePrereqs;

    public static final String ID = "id";
    public static final String SHORT_DESC = "shortdesc";
    public static final String LONG_DESC = "longdesc";
    public static final String PRE_REQS = "prereqs";

    public Course(String mCourseId, String mCourseShortDesc, String mCourseLongDesc, String mCoursePrereqs){
        this.mCourseId = mCourseId;
        this.mCourseShortDesc = mCourseShortDesc;
        this.mCourseLongDesc = mCourseLongDesc;
        this.mCoursePrereqs = mCoursePrereqs;
    }

    public String getCourseId(){
        return mCourseId;
    }
    public String getCourseLongDesc(){
        return mCourseLongDesc;
    }
    public String getCourseShortDesc(){
        return mCourseShortDesc;
    }
    public String getCoursePrereqs(){
        return mCoursePrereqs;
    }
    public static List<Course> parseCourseJson(String courseJson) throws JSONException{
        List<Course> courseList = new ArrayList<>();
        if(courseJson != null){
            JSONArray arr = new JSONArray(courseJson);
            for (int i = 0; i< arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Course course = new Course(obj.getString(Course.ID), obj.getString(Course.SHORT_DESC),
                        obj.getString(Course.LONG_DESC), obj.getString(Course.PRE_REQS));
                courseList.add(course);

            }
        }
        return courseList;
    }
    public void setCourseId(String courseId){
        mCourseId = courseId;
    }
    public void setCourseLongDesc(String longDesc){
        mCourseLongDesc = longDesc;
    }
    public void setCourseShortDesc(String shortDesc){
        mCourseShortDesc = shortDesc;
    }
    public void setCoursePrereqs(String prereqs){
        mCoursePrereqs = prereqs;
    }
}
