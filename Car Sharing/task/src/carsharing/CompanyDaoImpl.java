package carsharing;

import carsharing.entity.Company;

import java.util.List;

public class CompanyDaoImpl implements CompanyDao{

    DatabaseManagment db = DatabaseManagment.launchDatabase();

    @Override
    public List<Company> getCompanyList() {
        String query = "SELECT * FROM company";
        return db.getRecords(query);
    }

    @Override
    public Company createCompany(String companyName) {
        Company company = new Company(companyName);
        String query = "INSERT INTO company(name) VALUES ('"+companyName+"')";
        db.executeQuery(query);
        return company;
    }

    @Override
    public void  updateTable(){
        String query = "ALTER TABLE company " +
                "ADD PRIMARY KEY (id) ";
        System.out.println("Upadting Table");
        //db.executeQuery(query, false);
        query = "ALTER TABLE company " +
                "ALTER COLUMN id INT NOT NULL AUTO_INCREMENT";
        //db.executeQuery(query);

        query = "ALTER TABLE company " +
                "ADD UNIQUE(name)";
        //db.executeQuery(query);

        query = "ALTER TABLE company " +
                "ALTER COLUMN name VARCHAR(255) NOT NULL UNIQUE";
        //db.executeQuery(query);

        query = "DELETE FROM customer";
        db.executeQuery(query);

        query = "DELETE FROM car";
        db.executeQuery(query);

        query = "DELETE FROM company";
        db.executeQuery(query);

        //query = "ALTER TABLE company SET id AUTO_INCREMENT = 1";
        //db.executeQuery(query);

        query = "DROP TABLE customer";
        db.executeQuery(query);
        System.out.println("Customer Table dropped");

        query = "DROP TABLE car";
        db.executeQuery(query);
        System.out.println("Car Table dropped");

        query = "DROP TABLE company";
        db.executeQuery(query);
        System.out.println("Company Table dropped");




        //System.out.println("Updated Table");
    }
}
