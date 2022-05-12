package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);

    static List<Company> companies = new ArrayList<>();
    static List<Car> cars = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static CompanyDao companyDao = new CompanyDaoImpl();
    static CarDao carDao = new CarDaoImpl();
    static CustomerDao customerDao = new CustomerDaoImpl();
    static Customer cust = null;
    static private boolean isManager = false;


    public static void main(String[] args) {
        //companyDao.updateTable();
        //if(true) return;

        boolean exit = false;
        while (!exit){
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            System.out.print("> ");
            int value = scan.nextInt();
            System.out.println();
            switch(value){
                case 1: {
                    loginAsManager();
                    break;
                }
                case 2: {
                    loginAsCustomer();
                    break;
                }
                case 3: {
                    createCustomer();
                    break;
                }
                default: {
                    exit = true;
                    break;
                }
                
            }
            
        }
    }

    private static void createCustomer() {

        Scanner myScan = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        System.out.print("> ");
        String name = myScan.nextLine();
        customerDao.createCustomer(name);
        System.out.println("The customer was added!");
        System.out.println();
    }

    private static void loginAsCustomer() {
        customerList();
    }


    private static void loginAsManager(){
        boolean back = false;
        isManager = true;
        while(!back){
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            System.out.print("> ");
            int value = scan.nextInt();
            System.out.println();
            if(value == 1) companyList();
            else if(value == 2) createCompany();
            else back = true;
        }
    }


    private static void companyList(){
        boolean back = false;
        companies = companyDao.getCompanyList();
        if(companies.isEmpty()){
            System.out.println("The company list is empty");
            System.out.println();
            return;
        }
        if(isManager) System.out.println("Choose the company:");
        else System.out.println("Choose a company");

        for(int i = 0; i<companies.size(); i++){
            Company name = companies.get(i);
            System.out.printf("%s. %s \n",(i+1),name.getName());

        }
        System.out.println("0. Back");
        System.out.print("> ");
        int choice = scan.nextInt();
        System.out.println();
        if(choice == 0) return;
        else{
            Company com = companies.get(choice-1);
            companyMenu(com);
        }

    }

    private static void createCompany(){
        Scanner myScan = new Scanner(System.in);

        System.out.println("Enter the company name:");
        System.out.print("> ");
        String company = myScan.nextLine();
        companyDao.createCompany(company);
        System.out.println("The company was created");
        System.out.println();

    }

    private static void companyMenu(Company company){
        boolean back = false;
        Scanner myScan = new Scanner(System.in);
        if(isManager){
            System.out.println("'"+company.getName()+"' company:");

            while(!back) {
                System.out.println("1. Car list\n2. Create a car\n0. Back");
                System.out.print("> ");
                int companyChoice = myScan.nextInt();
                System.out.println();
                if(companyChoice == 1){
                    carList(company);
                }
                else if(companyChoice == 2){
                    createCar(company);
                }
                else{
                    back = true;
                }
            }
        }
        else{
            carList(company);
        }

    }

    private static void createCar(Company company) {
        Scanner myScan = new Scanner(System.in);
        System.out.println("Enter the car name:");
        System.out.print("> ");
        String name = myScan.nextLine();
        carDao.createCar(name, company.getId());
        System.out.println("The car was added!");
        System.out.println();

    }

    private static void carList(Company company){
        cars = carDao.getCarListByCompany(company);

        if(isManager){
            if(cars.isEmpty()){
                System.out.println("The car list is empty!");
                System.out.println();
                return;
            }
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                Car name = cars.get(i);
                System.out.printf("%s. %s \n", (i + 1), name.getName());
            }
        }


        else{
            List<Car> unRentedCars = getUnrentedCars(cars);
            if(unRentedCars.isEmpty()) {
                System.out.println("The car list is empty!");
                System.out.println();
                return;
            }
            System.out.println("Choose a car:");
            for (int i = 0; i < unRentedCars.size(); i++) {
                Car name = unRentedCars.get(i);
                System.out.printf("%s. %s \n", (i + 1), name.getName());
            }
            System.out.println("0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();
            System.out.println();
            if(choice == 0) return;
            else{
                rentCar(cust, cars.get(choice-1));
            }
        }

    }

    private static void customerList(){
        boolean back = false;
        isManager = false;
        customers = customerDao.getCustomers();
        if(customers.isEmpty()){
            System.out.println("The customer list is empty!");
            System.out.println();
            return;
        }

        System.out.println("The customer list:");

        for(int i = 0; i<customers.size(); i++){
            Customer name = customers.get(i);
            System.out.printf("%s. %s \n",(i+1),name.getName());

        }
        System.out.println("0. Back");
        System.out.print("> ");
        int choice = scan.nextInt();
        System.out.println();
        if(choice == 0){
            return;
        }
        else{
            cust = customers.get(choice-1);
            customerMenu(cust);
        }

    }
    
    public static void customerMenu(Customer customer){
        boolean back = false;
        while(!back) {
            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            System.out.print("> ");
            int choice = scan.nextInt();
            System.out.println();
            if(choice == 1){
                if(!customerDao.checkIfCarRented(customer))
                    companyList();
                else{
                    System.out.println("You've already rented a car!");
                }
            }
            else if(choice == 2){
                Car car = customerDao.rentedCar(cust);
                returnRentedCar(customer,car);
            }
            else if(choice == 3 ){
                customerRentedCar(cust);
            }
            else {
                back = true;
            }
            System.out.println();

        }

    }

    private static void customerRentedCar(Customer customer) {
        Car car = customerDao.rentedCar(customer);
        if(car!=null){
            Company company = customerDao.rentedCarCompany(car);
            System.out.println("Your rented :\n"+
                    car.getName()+"\n" +
                    "Company:\n" +
                    ""+company.getName()+"");
        }
        else System.out.println("You didn't rent a car!");

        System.out.println();
    }

    private static void returnRentedCar(Customer customer, Car car) {
        String out = customerDao.returnRentedCar(customer, car);
        System.out.println(out);
        System.out.println();
    }

    private static void rentCar(Customer customer,Car car) {
        String out = customerDao.rentCar(customer, car);
        System.out.println(out);
        System.out.println();
    }

    private static List<Car> getUnrentedCars(List<Car> cars){
        List<Integer> rentedCarsId = customerDao.getRentedCarId();
        List<Car> unRentedCars = new ArrayList<>();
        Car car = customerDao.getRentedCar(cust);

        for(Car myCar: cars){
            if((!rentedCarsId.contains(myCar.getId())) || (car!= null && myCar == car)){
                unRentedCars.add(myCar);
            }
        }

        return unRentedCars;
    }





}