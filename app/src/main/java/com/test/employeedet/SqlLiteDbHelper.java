package com.test.employeedet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.test.employeedet.Employee_View_Activity.LanguageMapping;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    static SqlLiteDbHelper mInstance;
    private static final int DBVERSION=1;
    private String TBL_EMPLOYEE ="tbl_employee";

    public static synchronized SqlLiteDbHelper getInstance(Context context){
        if(mInstance==null){
            mInstance=new SqlLiteDbHelper(context);
        }
        return  mInstance;
    }

    public SqlLiteDbHelper(Context context) {
        super(context, "employeeData.db", null, DBVERSION);
    }

    /*SQL Lite Table Creation*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                TBL_EMPLOYEE +
                " (id INTEGER PRIMARY KEY," +
                   "firstName TEXT," +
                   "lastName TEXT," +
                   "address TEXT," +
                   "city TEXT," +
                   "zipcode  TEXT," +
                   "gender  TEXT," +
                   "dob  TEXT," +
                   "designation  TEXT," +
                   "mobile  TEXT," +
                   "email  TEXT," +
                   "nationality  TEXT," +
                   "language  TEXT," +
                   "imageURL  TEXT," +
                   "extra_curricular  TEXT," +
                   "technical  TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        onCreate(sqLiteDatabase);
    }

    /*Insert Data in SQL LIte Db */
    public long insertEmployeeData(EmployeeResponse.Employee emp)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id",emp.id);
        cv.put("firstName",emp.firstName);
        cv.put("lastName",emp.lastName);
        cv.put("address",emp.address);
        cv.put("city",emp.city);
        cv.put("zipcode",emp.zipcode);
        cv.put("gender",emp.gender);
        cv.put("dob",emp.dob);
        cv.put("designation",emp.designation);
        cv.put("mobile",emp.mobile);
        cv.put("email",emp.email);
        cv.put("nationality",emp.nationality);
       // cv.put("language",emp.language);
        cv.put("imageURL",emp.imageURL);
        cv.put("language",LanguageMapping(emp.language));

        String extra_curricular_skills= Employee_View_Activity.getskillsdetails(emp,1);
        String technical_skills= Employee_View_Activity.getskillsdetails(emp,2);

        cv.put("extra_curricular",extra_curricular_skills);
        cv.put("technical",technical_skills);

        long ret=DB.insert(TBL_EMPLOYEE, null, cv);
        DB.close();
        return ret;
    }

    /*Retrieving Data from SQL Lite DB*/
    public List<EmployeeResponse.Employee> getEmployeeList(){
        List<EmployeeResponse.Employee> employeeList = new ArrayList<>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor mCursor =  DB.rawQuery( "select * from "+TBL_EMPLOYEE, null);
        if(mCursor != null && mCursor.getCount() > 0){
            while(mCursor.moveToNext()){
                EmployeeResponse.Employee employee = new EmployeeResponse.Employee();
                employee.id = mCursor.getString(0);
                employee.firstName = mCursor.getString(1);
                employee.lastName = mCursor.getString(2);
                employee.address = mCursor.getString(3);
                employee.city = mCursor.getString(4);
                employee.zipcode = mCursor.getString(5);
                employee.gender = mCursor.getString(6);
                employee.dob = mCursor.getString(7);
                employee.designation = mCursor.getString(8);
                employee.mobile = mCursor.getString(9);
                employee.email = mCursor.getString(10);
                employee.nationality = mCursor.getString(11);
                employee.language = mCursor.getString(12);
                employee.imageURL = mCursor.getString(13);
                employee.extracurricular = mCursor.getString(14);
                employee.technical = mCursor.getString(15);
                employeeList.add(employee);
            }
            mCursor.close();
        }
        return employeeList;
    }

    /*Check whether Data is available or not in DB*/
    public boolean checkdatavailable(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor mCursor =  DB.rawQuery( "select * from "+TBL_EMPLOYEE, null);
        if(mCursor != null && mCursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
