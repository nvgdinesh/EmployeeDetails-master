package com.test.employeedet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import static com.test.employeedet.MainActivity.employeeArrayList;
import static com.test.employeedet.MainActivity.isNetworkAvailable;
import static com.test.employeedet.MainActivity.languageArrayList;
import static com.test.employeedet.MainActivity.skillArrayList;

public class Employee_View_Activity extends AppCompatActivity {
    private TextView name_value,address_value,gender_value,Dob_value,Designation_value,
            Mobile_value,Email_value,Nationality_value,Language_value,Skills_value;
    private ImageView user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_view_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        EmployeeResponse.Employee emp = new Gson().fromJson(getIntent().getStringExtra("data"), EmployeeResponse.Employee.class);

        name_value = (TextView) findViewById(R.id.name_value);
        address_value = (TextView) findViewById(R.id.address_value);
        gender_value = (TextView) findViewById(R.id.gender_value);
        Dob_value = (TextView) findViewById(R.id.Dob_value);
        Designation_value = (TextView) findViewById(R.id.Designation_value);
        Mobile_value = (TextView) findViewById(R.id.Mobile_value);
        Email_value = (TextView) findViewById(R.id.Email_value);
        Nationality_value = (TextView) findViewById(R.id.Nationality_value);
        Language_value = (TextView) findViewById(R.id.Language_value);
        Skills_value = (TextView) findViewById(R.id.Skills_value);
        user_image = (ImageView) findViewById(R.id.user_image);

        name_value.setText(emp.firstName + " " + emp.lastName);
        address_value.setText(emp.address + "\n" + emp.city + "\n" + emp.zipcode);
        gender_value.setText(emp.gender);
        Dob_value.setText(emp.dob);
        Designation_value.setText(emp.designation);
        Mobile_value.setText(emp.mobile);
        Email_value.setText(emp.email);
        Nationality_value.setText(emp.nationality);
        Language_value.setText(emp.language);

        /*Loading Image using Glide Image Library*/
        Glide.with(this)
                .load(emp.imageURL)
                .placeholder(R.drawable.ic_avatar_156dp)
                .into(user_image);

        Skills_value.setText("Technical - "+emp.technical+"\n"+"Extra Curricular - "+emp.extracurricular);

      if(isNetworkAvailable()){
          String languageValue = LanguageMapping(emp.language);
          Language_value.setText(languageValue);

          String technical_skills = Employee_View_Activity.getskillsdetails(emp,2);
          String extra_curricular_skills = Employee_View_Activity.getskillsdetails(emp,1);
          Skills_value.setText("Technical - "+technical_skills+"\n"+"Extra Curricular - "+extra_curricular_skills);
        }
    }

   public static String getskillsdetails(EmployeeResponse.Employee emp, int val) {
        String skills_result = "", extra_curricular, technical;
        if (val == 1) {
            for (int i = 0; i < emp.skills.get(0).extra_curricular.size(); i++) {
                String add_Seperator = "";
                extra_curricular = emp.skills.get(0).extra_curricular.get(i);
                if (emp.skills.get(0).extra_curricular.size() > 1 && i < emp.skills.get(0).extra_curricular.size() - 1)
                    add_Seperator = ",";
                skills_result += extra_curricular + add_Seperator;
            }
        } else if (val == 2) {
             for (int i = 0; i < emp.skills.get(0).technical.size(); i++) {
                 String add_Seperator = "";
                 technical = emp.skills.get(0).technical.get(i);
                 skills_result+=SkillsMapping(technical);
                 if (emp.skills.get(0).technical.size() > 1 && i < emp.skills.get(0).technical.size() - 1)
                     add_Seperator = ",";
                 skills_result += add_Seperator;
             }
         }
        return skills_result;
    }

    public static String LanguageMapping(String language) {
        String languageResult = "";
        for (int i = 0;i<languageArrayList.size(); i++)
        {
            if(languageArrayList.get(i).id.equals(language)){
                languageResult=languageArrayList.get(i).language;
            }
        }
        return languageResult;
    }

   public static String SkillsMapping(String skills) {
        String skillmap = "";
        for (int i = 0; i <skillArrayList.size();i++)
        {
            if(skillArrayList.get(i).id.equals(skills)){
                skillmap+=skillArrayList.get(i).name;
            }
        }
        return skillmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
