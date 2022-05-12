package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;

public interface CustomerDao {

    public Customer createCustomer(String customerName);

    public List<Customer> getCustomers();

    public String rentCar(Customer customer, Car car);

    public String returnRentedCar(Customer customer, Car car);

    public Car rentedCar(Customer customer);

    public Company rentedCarCompany(Car car);

    public boolean checkIfCarRented(Customer customer);

    public List<Integer> getRentedCarId();

    public Car getRentedCar(Customer customer);
}
