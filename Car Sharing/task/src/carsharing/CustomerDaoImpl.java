package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao{
    DatabaseManagment db = DatabaseManagment.launchDatabase();

    @Override
    public Customer createCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        String query = "INSERT INTO customer(name) VALUES ('"+customerName+"')";
        db.executeQuery(query);
        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        String query = "SELECT * FROM customer";
        return db.getCustomers(query);
    }

    @Override
    public String rentCar(Customer customer, Car car) {
        boolean carRented = checkIfCarRented(customer);
        //System.out.println(car.getCompanyId());
        if(!carRented) {
            db.executeQuery("UPDATE customer SET rented_car_id = " + car.getId() + "" +
                    " WHERE id = " + customer.getId() + "");
            return "You rented '" + car.getName() + "'";
        }
        return "You have already rented a car!";
    }

    @Override
    public String returnRentedCar(Customer customer, Car car) {
        boolean carRented = checkIfCarRented(customer);
        if(carRented){
            db.executeQuery("UPDATE customer SET rented_car_id = NULL WHERE id = "+customer.getId()+"");
            return "You've returned a rented car!";
        }
        return "You didn't rent a car!";
    }


    @Override
    public Car rentedCar(Customer customer) {
        boolean carRented = checkIfCarRented(customer);
        if(carRented) {
            //db.getRentedCarID(customer.getId());
            return db.getRentedCarID(customer.getId());
        }
        return null;
    }

    @Override
    public Company rentedCarCompany(Car car) {
        return db.getCarCompanyName(car.getCompanyId());
    }

    @Override
    public boolean checkIfCarRented(Customer customer){
        return db.checkIfCarRented(customer);
    }

    @Override
    public List<Integer> getRentedCarId() {
        List<Integer> rentedCarIds = db.getRentedCarId();
        String query = "SELECT * FROM car";
        List<Car> cars = db.getCarRecords(query);
        List<Car> unRentedCars = new ArrayList<>();
        /*for(Car car: cars){
            if(rentedCarIds.contains(car.getId())) continue;
            unRentedCars.add(car);
        }*/
        return db.getRentedCarId();
    }

    @Override
    public Car getRentedCar(Customer customer) {
        return db.getRentedCarID(customer.getId());
    }
}
