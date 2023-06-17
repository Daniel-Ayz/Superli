package HumanResources.BusinessLayer.EmployeeModule;

import HR_Delivery.LicenseType;
import HumanResources.DataAcessLayer.EmployeeDAL.*;

import java.util.*;

public class EmployeeFacade {
    private Map<String, Employee> employees;

    public EmployeeFacade() {
        employees = new HashMap<>();
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public Employee getEmployee(String id) {
        return employees.get(id);
    }

    public boolean checkIfEmployeeExist(String id) {
        return employees.containsKey(id);
    }

    public boolean checkPassword(String id, String password) {
        if (!checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        return employees.get(id).getPassword().equals(password);
    }

    public void addEmployee(String name, String id, String personalInfo, String bankName, String bankAccountNumber, Date startDate, String termsAndConditions, List<Role> roles, double salary, LicenseType licenseType) throws IllegalArgumentException {
        if (checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee already exist");
        if(Integer.parseInt(id) < 0)
            throw new IllegalArgumentException("ID must be positive");
        if(licenseType == null){
            if(roles.contains(Role.DRIVER))
                throw new IllegalArgumentException("Driver must have a license");
        }
        else{
            if(!roles.contains(Role.DRIVER))
                throw new IllegalArgumentException("Only driver can have a license");
        }
        //create the correct employee type
        Employee employee;
        if(roles.contains(Role.DRIVER) && licenseType != null)
            employee = new Driver(name, id, personalInfo, new BankInformation(bankName, bankAccountNumber), new EmploymentContract(startDate, termsAndConditions), roles, new ArrayList<>(), salary, licenseType);
        else
            employee = new Employee(name, id, personalInfo, new BankInformation(bankName, bankAccountNumber), new EmploymentContract(startDate, termsAndConditions), roles, new ArrayList<>(), salary);

        if (insertEmployee(employee)
                && insertBankInformation(id, employee.getBankInformation())
                && insertEmployeeRoles(id, roles)
                && insertEmploymentContract(id, employee.getEmploymentContract())) {
            employees.put(id, employee);
            if(roles.contains(Role.DRIVER)) {
                if(!insertDriver(id, licenseType))
                    throw new RuntimeException("Failed to insert Driver info to DB");
            }
        }
        else
            throw new RuntimeException("Failed to insert employee to DB");
    }


    private boolean insertEmployee(Employee employee) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.insert(Integer.parseInt(employee.getId()), employee.getName(), employee.getPersonalInfo(), employee.getBaseSalary(), employee.getPassword());
    }

    private boolean insertBankInformation(String id, BankInformation bankInformation) {
        BankInformationDAO bankInformationDAO = new BankInformationDAO();
        return bankInformationDAO.insert(Integer.parseInt(id), bankInformation.getBankName(), bankInformation.getBankAccountNumber());
    }

    private boolean insertEmployeeRoles(String id, List<Role> roles) {
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO();
        for (Role role: roles) {
            boolean bool = employeeRolesDAO.insert(Integer.parseInt(id), role);
            if(!bool)
                return false;
        }
        return true;
    }

    private boolean insertEmploymentContract(String id, EmploymentContract employmentContract) {
        EmploymentContractDAO employmentContractDAO = new EmploymentContractDAO();
        return employmentContractDAO.insert(Integer.parseInt(id), employmentContract.getStartDate(), employmentContract.getTermsOfEmployment());
    }

    private boolean insertDriver(String id, LicenseType licenseType) {
        DriverDAO driverDAO = new DriverDAO();
        return driverDAO.insert(Integer.parseInt(id), licenseType);
    }


    public void removeEmployee(String id) {
        if(checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        if(deleteEmployee(employees.get(id).getId()))
            employees.remove(id);
        else
            throw new RuntimeException("Failed to delete employee from DB");
    }

    private boolean deleteEmployee(String id) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.delete(Integer.parseInt(id));
    }

    public void addRole(String id, Role role) {
        if(role == null)
            throw new IllegalArgumentException("Role cannot be null");
        if(role == Role.DRIVER)
            throw new IllegalArgumentException("CANNOT ADD DRIVER ROLE");
        if(!checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        if (employees.get(id).getRoles().contains(role))
            throw new IllegalArgumentException("Employee already have this role");
        if(!insertEmployeeRole(Integer.parseInt(id), role))
            throw new RuntimeException("Failed to insert role to DB");
        employees.get(id).addRole(role);
    }

    private boolean insertEmployeeRole(Integer id, Role role) {
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO();
        return employeeRolesDAO.insert(id, role);
    }

    public void removeRole(String id, Role role) {
        if(role == null)
            throw new IllegalArgumentException("Role cannot be null");
        if(role == Role.DRIVER)
            throw new IllegalArgumentException("CANNOT REMOVE DRIVER ROLE");
        if(!checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        if (!employees.get(id).getRoles().contains(role))
            throw new IllegalArgumentException("Employee does not have this role");
        if(!deleteEmployeeRole(Integer.parseInt(id), role))
            throw new RuntimeException("Failed to delete role from DB");
        employees.get(id).removeRole(role);
    }

    private boolean deleteEmployeeRole(Integer id, Role role) {
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO();
        return employeeRolesDAO.delete(id, role);
    }

    public void addBaseSalary(String id, Date date) {
        if (checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        Employee employee = employees.get(id);
        Salary salary = new Salary(employee.getBaseSalary(),0, date);
        if (insertSalary(id, salary))
            employee.addSalary(salary);
        else
            throw new RuntimeException("Failed to insert salary to DB");
    }

    private boolean insertSalary(String id, Salary salary) {
        SalaryDAO salaryDAO = new SalaryDAO();
        return salaryDAO.insert(Integer.parseInt(id), salary.getDate(), salary.getBonus(), salary.getSalary());
    }

    public void setBonusLastSalary(String id, double bonus) {
        if(!checkIfEmployeeExist(id))
            throw new IllegalArgumentException("Employee does not exist");
        if(!updateSalary(id, employees.get(id).getLastSalary(), bonus))
            throw new RuntimeException("Failed to update salary in DB");
        employees.get(id).setBonusLastSalary(bonus);
    }

    private boolean updateSalary(String id, Salary salary, double bonus) {
        SalaryDAO salaryDAO = new SalaryDAO();
        return salaryDAO.update(Integer.parseInt(id), salary.getDate(), bonus, salary.getSalary());
    }


    public void loadData() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        DriverDAO driverDAO = new DriverDAO();
        BankInformationDAO bankInformationDAO = new BankInformationDAO();
        EmployeeRolesDAO employeeRolesDAO = new EmployeeRolesDAO();
        EmploymentContractDAO employmentContractDAO = new EmploymentContractDAO();
        SalaryDAO salaryDAO = new SalaryDAO();

        List<Employee> employees = employeeDAO.selectAll();
        for (Employee employee: employees) {
            List<Role> roles = employeeRolesDAO.getById(Integer.parseInt(employee.getId()));
            List<Salary> salaries = salaryDAO.getById(Integer.parseInt(employee.getId()));
            BankInformation bankInformation = bankInformationDAO.getById(Integer.parseInt(employee.getId()));
            EmploymentContract employmentContract = employmentContractDAO.getById(Integer.parseInt(employee.getId()));

            //failed to load data from DB
            if(bankInformation == null)
                bankInformation = new BankInformation("", "");
            if(employmentContract == null)
                employmentContract = new EmploymentContract(new Date(), "");

            if(roles.contains(Role.DRIVER)) {
                LicenseType licenseType = driverDAO.getById(Integer.parseInt(employee.getId()));
                this.employees.put(employee.getId(), new Driver(employee.getName(), employee.getId(), employee.getPersonalInfo(), bankInformation, employmentContract, roles, salaries, employee.getBaseSalary(), licenseType));
            }
            else
                this.employees.put(employee.getId(), new Employee(employee.getName(), employee.getId(), employee.getPersonalInfo(), bankInformation, employmentContract, roles, salaries, employee.getBaseSalary()));
        }
    }
}
