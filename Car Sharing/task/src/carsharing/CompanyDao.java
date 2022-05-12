package carsharing;

import carsharing.entity.Company;

import java.util.List;

public interface CompanyDao {

    public List<Company> getCompanyList();

    public Company createCompany(String companyName);

    public void updateTable();
}
