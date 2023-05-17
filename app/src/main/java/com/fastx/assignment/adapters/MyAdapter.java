package com.fastx.assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fastx.assignment.R;
import com.fastx.assignment.activities.DetailedActivity;
import com.fastx.assignment.data.Employee;
import com.fastx.assignment.data.EmployeeList;
import com.fastx.assignment.databinding.SampleItemBinding;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.UserViewHolder> {
    EmployeeList employees;
    Context context;

    public MyAdapter(EmployeeList employees, Context context) {
        this.employees = employees;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.binding.email.setText(employee.getEmail());
        holder.binding.name.setText(employee.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("id", employee.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        SampleItemBinding binding;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleItemBinding.bind(itemView);
        }
    }
}
