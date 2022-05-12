package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.util.List;

public interface CarDao {

    public List<Car> getCarList();

    public Car createCar(String carName, int companyId);

    public List<Car> getCarListByCompany(Company company);

    public void deleteCarsByCompany(Company company);
}
