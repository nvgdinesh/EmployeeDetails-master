package com.test.employeedet;

import java.util.ArrayList;
public class EmployeeResponse {
    public ArrayList<Employee> employee = null;
    public ArrayList<Language> languages = null;
    public ArrayList<Skill> skills = null;


    public static class Employee {
        public String id;
        public String firstName;
        public String lastName;
        public String address;
        public String city;
        public String zipcode;
        public String gender;
        public String dob;
        public String designation;
        public String mobile;
        public String email;
        public String nationality;
        public String language;
        public String imageURL;
        public String extracurricular;
        public String technical;

        public ArrayList<Skill> skills = null;
        public static class Skill {
            public ArrayList<String> technical = null;
            public ArrayList<String> extra_curricular = null;
        }
    }

    public static class Language {

        public String id;
        public String language;
    }

    public class Skill {
        public String id;
        public String name;
    }


}
