package com.test.employeedet;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class Employee_Data_Adapter extends RecyclerView.Adapter<Employee_Data_Adapter.ViewHolder>  {

    private List<EmployeeResponse.Employee> dataset = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView first_name,designation,city;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            first_name= (TextView) v.findViewById(R.id.first_name);
            designation= (TextView) v.findViewById(R.id.designation);
            city= (TextView) v.findViewById(R.id.city);
            image= (ImageView) v.findViewById(R.id.image);
        }
    }

    public Employee_Data_Adapter() {  }

    @Override
    public Employee_Data_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_basic_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EmployeeResponse.Employee emp=dataset.get(position);

       /*Loading Image using Glide Image Library*/
        Glide.with(holder.itemView.getContext())
                .load(emp.imageURL)
                .placeholder(R.drawable.ic_avatar_156dp)
                .into(holder.image);

        holder.first_name.setText(emp.firstName+" "+emp.lastName);
        holder.designation.setText(emp.designation);
        holder.city.setText(emp.city);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),Employee_View_Activity.class);
                i.putExtra("data",new Gson().toJson(emp));
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void clear(){
        this.dataset.clear();
    }

    public void add(List<EmployeeResponse.Employee> data){
        if(data!=null){
            this.dataset.addAll(data);
            notifyDataSetChanged();
        }
    }
}
