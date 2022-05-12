package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManagment {

    private static DatabaseManagment dbm= null;
    private static Connection conn = null;
    private static Statement stmt = null;
    private final static String DRIVER = "org.h2.Driver";
    private final static String URL = "jdbc:h2:./src/carsharing/db/carsharing";
    private final static String USERNAME = "";
    private final static String PASSWORD = "";
    private final static String CREATE_TABLE_QUERY ="CREATE TABLE IF NOT EXISTS COMPANY " +
            " (id INTEGER not NULL AUTO_INCREMENT, " +
            " name VARCHAR(255) UNIQUE NOT NULL, " +
            " PRIMARY KEY ( id ))";

    private final static String CREATE_CAR_TABLE_QUERY ="CREATE TABLE IF NOT EXISTS CAR " +
            " (id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY, " +
            " name VARCHAR(255) UNIQUE NOT NULL, " +
            " company_id INT NOT NULL, " +
            " FOREIGN KEY (company_id) REFERENCES company(id))";

    private final static String CREATE_CUSTOMER_TABLE_QUERY ="CREATE TABLE IF NOT EXISTS CUSTOMER " +
            " (id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY, " +
            " name VARCHAR(255) UNIQUE NOT NULL, " +
            " rented_car_id INT NULL, " +
            " FOREIGN KEY (rented_car_id) REFERENCES car(id))";

    private DatabaseManagment(){
        executeQuery(CREATE_TABLE_QUERY);
        executeQuery(CREATE_CAR_TABLE_QUERY);
        executeQuery(CREATE_CUSTOMER_TABLE_QUERY);
    }

    public static DatabaseManagment launchDatabase(){
        if(dbm == null){
            dbm = new DatabaseManagment();
        }

        return dbm;
    }

    public void executeQuery(String query){
        try {
            connect();
            //System.out.println("Executing Query....");
            stmt.executeUpdate(query);
            //System.out.println("Executed Query");

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
        } //end try
    }

    public void closeAll(){
        try{
            if(stmt!=null) stmt.close();
        } catch(SQLException ignored) {
        } // nothing we can do
        try {
            if(conn!=null) conn.close();
        } catch(SQLException se){
            se.printStackTrace();
        } //end finally try
    }

    private void connect() throws Exception{
        Class.forName(DRIVER);
        //System.out.println("Connecting to database");
        conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
    }

    public List<Company> getRecords(String query){
        List<Company> companies = new ArrayList<>();
        ResultSet rs = null;
        try {
            connect();
            //System.out.println("Executing Query....");
            rs = stmt.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                companies.add(new Company(id,name));
            }
            //System.out.println("Executed Query");

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //end try
        return companies;
    }


    public List<Car> getCarRecords(String query){
        List<Car> cars = new ArrayList<>();
        ResultSet rs = null;
        try {
            connect();
            //System.out.println("Executing Query....");
            rs = stmt.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int companyId = rs.getInt("company_id");
                cars.add(new Car(id,name,companyId));
            }
            //System.out.println("Executed Query");

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //end try
        return cars;
    }


    public List<Customer> getCustomers(String query){
        List<Customer> customers = new ArrayList<>();
        ResultSet rs = null;
        try {
            connect();
            //System.out.println("Executing Query....");
            rs = stmt.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rentedCarId = rs.getInt("rented_car_id");
                customers.add(new Customer(id,name,rentedCarId));
            }

            //System.out.println("Executed Query");

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //end try
        return customers;
    }

    public Car getRentedCarID(int customerId) {
        Car car = null;
        int id = -1;
        String query = "SELECT * FROM customer WHERE id = "+customerId+"";
        ResultSet rs = null;
        try {
            connect();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                id = rs.getInt("rented_car_id");
            }


            query = "SELECT * FROM car WHERE id = "+id+"";
            rs = stmt.executeQuery(query);
            String name = "";
            int companyId = 0;

            while(rs.next()){
                id = rs.getInt("id");
                name = rs.getString("name");
                companyId = rs.getInt("company_id");
                car = new Car(id,name,companyId);
            }

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //end try
        return car;

    }

    public Company getCarCompanyName(int id){
        Company company = null;
        String query = "SELECT * FROM company WHERE id = "+id+"";
        ResultSet rs = null;
        try {
            connect();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                int companyId = rs.getInt("id");
                String name = rs.getString("name");

                company = new Company(id,name);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
            try {
                if(rs!=null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return company;
    }

    public boolean checkIfCarRented(Customer customer){
        ResultSet rs1 = null;

        boolean carIsRented = false;
        String query = "SELECT * FROM customer WHERE id ="+customer.getId()+"";
        try {
            connect();
            rs1 = stmt.executeQuery(query);

            while(rs1.next()){
                int id  = rs1.getInt("id");
                String name = rs1.getString("name");
                int carId = rs1.getInt("rented_car_id");
                if(carId > 0) {
                    carIsRented = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
            try {
                if(rs1!=null) rs1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return carIsRented;
    }

    public List<Integer> getRentedCarId(){
        String query = "SELECT * FROM customer WHERE rented_car_id IS NOT NULL";
        List<Integer> rentedCarIds = new ArrayList<>();
        ResultSet rs = null;
        try {
            connect();
            //System.out.println("Executing Query....");
            rs = stmt.executeQuery(query);

            while(rs.next()){
                int rentedCarId = rs.getInt("rented_car_id");
                rentedCarIds.add(rentedCarId);
            }

            //System.out.println("Executed Query");

        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            closeAll();
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } //end try
        return rentedCarIds;
    }

}
