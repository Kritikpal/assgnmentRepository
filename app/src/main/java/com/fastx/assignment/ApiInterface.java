package com.fastx.assignment;

import com.fastx.assignment.data.Employee;
import com.fastx.assignment.data.EmployeeList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("users")
    Call<EmployeeList> getEmployeeList();

    @GET("users/{id}")
    Call<Employee> getEmployee(@Path("id") int id);
}
