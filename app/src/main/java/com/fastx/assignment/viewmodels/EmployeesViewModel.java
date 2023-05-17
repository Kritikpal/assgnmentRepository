package com.fastx.assignment.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fastx.assignment.data.Employee;
import com.fastx.assignment.data.EmployeeList;
import com.fastx.assignment.repository.EmployeeRepository;

public class EmployeesViewModel extends ViewModel {
    private final EmployeeRepository employeeRepository;

    public EmployeesViewModel(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public LiveData<EmployeeList> getEmployees() {
        return employeeRepository.getEmployeeList();
    }
    public LiveData<Employee> getEmployeeDetails(int id) {
        return employeeRepository.getEmployeeDetails(id);
    }


    public LiveData<String > getError(){
        return employeeRepository.getErrorMessage();
    }

}
