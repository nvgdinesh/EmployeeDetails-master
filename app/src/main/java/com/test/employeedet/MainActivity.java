package com.test.employeedet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Employee_Data_Adapter mAdapter;
    Retrofit retrofit;
    private ProgressDialog progress;
    private static Context mcontext;


    public static ArrayList<EmployeeResponse.Language> languageArrayList = null;
    public static ArrayList<EmployeeResponse.Skill> skillArrayList = null;
    public static ArrayList<EmployeeResponse.Employee> employeeArrayList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = getApplicationContext();

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.app_loading));
        progress.setCancelable(false);

        mAdapter = new Employee_Data_Adapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        initialize_Retrofit();

        /*Load data based on the network availability
        * online - Load from API and store it in SQL Lite DB
        * Offline - Retrieve result from SQL Lite DB*/

        if(isNetworkAvailable()){
            progress.show();
            Toast.makeText(mcontext, getString(R.string.app_online_data),
                    Toast.LENGTH_SHORT).show();
            employee_API_Call();
        }else if(SqlLiteDbHelper.getInstance(getApplicationContext()).checkdatavailable()){
            progress.show();
            Toast.makeText(mcontext, getString(R.string.app_Offline_data),
                    Toast.LENGTH_SHORT).show();
            list_offline_details();
        }else{
            Toast.makeText(mcontext, getString(R.string.app_network_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*Retrofit Initialization*/
    private void initialize_Retrofit(){
        final String BASE_URL ="https://private-2a004-androidtest3.apiary-mock.com/";
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /*Retrofit API call*/
    private void employee_API_Call(){
        Call<List<EmployeeResponse>> call = retrofit.create(ApiInterface.class).getEmployeeDetails();
        call.enqueue(new Callback<List<EmployeeResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeeResponse>> call, Response<List<EmployeeResponse>> response) {
                progress.cancel();

                getLanguageDetails();
                getTechnicalDetails();

                for(EmployeeResponse empList:response.body()){

                    mAdapter.add(empList.employee);
                    employeeArrayList = empList.employee;

                    /*if(SqlLiteDbHelper.getInstance(getApplicationContext()).checkdatavailable()){
                        mAdapter.add(empList.employee); *//*Passing data to adapter*//*
                    }else
                    {
                        for(EmployeeResponse.Employee emp : empList.employee){
                            SqlLiteDbHelper.getInstance(getApplicationContext()).insertEmployeeData(emp); *//*Storing Data in SQL Lite Db for the first Occurences*//*
                        }
                        mAdapter.add(empList.employee); *//*Passing data to adapter*//*
                    }*/
                }
            }
            @Override
            public void onFailure(Call<List<EmployeeResponse>> call, Throwable t) {
            }
        });
    }
    /*Retrieving Data from SQL Lite DB if it is Offline*/
    private void list_offline_details(){
        mAdapter.clear();
        progress.cancel();
        mAdapter.add(SqlLiteDbHelper.getInstance(getApplicationContext()).getEmployeeList());
    }

    /*Retrofit APi Interface*/
    public interface ApiInterface {
        @GET("employeesListID")
        Call <List<EmployeeResponse>> getEmployeeDetails();

        @GET("language")
        Call <List<EmployeeResponse>> getLanguageDetails();


        @GET("technical")
        Call <List<EmployeeResponse>> getTechnicalDetails();
    }


    private void getLanguageDetails() {
        Call<List<EmployeeResponse>> call = retrofit.create(ApiInterface.class).getLanguageDetails();
        call.enqueue(new Callback<List<EmployeeResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeeResponse>> call, Response<List<EmployeeResponse>> response) {
                for (EmployeeResponse langlist : response.body()) {
                    languageArrayList = langlist.languages;
                }
            }
            @Override
            public void onFailure(Call<List<EmployeeResponse>> call, Throwable t) {
            }
        });
    }

    private void getTechnicalDetails() {
        Call<List<EmployeeResponse>> call = retrofit.create(ApiInterface.class).getTechnicalDetails();
        call.enqueue(new Callback<List<EmployeeResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeeResponse>> call, Response<List<EmployeeResponse>> response) {
                for (EmployeeResponse skilllist : response.body()) {
                    skillArrayList = skilllist.skills;
                }
                if(!SqlLiteDbHelper.getInstance(getApplicationContext()).checkdatavailable()) {
                        for (EmployeeResponse.Employee emp : employeeArrayList) {
                            SqlLiteDbHelper.getInstance(getApplicationContext()).insertEmployeeData(emp);
                        }
                    }
            }
            @Override
            public void onFailure(Call<List<EmployeeResponse>> call, Throwable t) {
            }
        });
    }



    /*Check if the Network is available or not*/
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connection = (ConnectivityManager) mcontext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo Wifi = connection
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (Wifi != null && Wifi.isConnected() && Wifi.isAvailable())
                return true;

            NetworkInfo Mobile = connection
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (Mobile != null && Mobile.isConnected() && Mobile.isAvailable())
                return true;

            NetworkInfo activeNet = connection.getActiveNetworkInfo();
            if (activeNet != null && activeNet.isConnected() && activeNet.isAvailable())
                return true;

        } catch (Exception ignored) {
              }
        return false;
    }


}
