package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.util.List;

public class CarDaoImpl implements CarDao{

    DatabaseManagment db = DatabaseManagment.launchDatabase();

    @Override
    public List<Car> getCarList() {
        String query = "SELECT * FROM car";
        return db.getCarRecords(query);
    }

    @Override
    public List<Car> getCarListByCompany(Company company) {
        String query = "SELECT * FROM car WHERE company_id = "+company.getId()+"";
        return db.getCarRecords(query);
    }

    @Override
    public void deleteCarsByCompany(Company company) {
        String query = "DELETE FROM car WHERE company_id = "+company.getId()+"";
        db.executeQuery(query);
    }

    @Override
    public Car createCar(String carName,int companyId) {
        Car car = new Car(carName, companyId);
        String query = "INSERT INTO car(name,company_id) VALUES ('"+carName+"',"+companyId+")";
        db.executeQuery(query);
        return car;
    }
}
