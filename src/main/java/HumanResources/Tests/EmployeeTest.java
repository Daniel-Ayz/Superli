package HumanResources.Tests;

import HumanResources.BusinessLayer.EmployeeModule.BankInformation;
import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.EmploymentContract;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.DataAcessLayer.EmployeeDAL.EmployeeDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

class EmployeeTest {

    private Employee employee;
    @BeforeEach
    void setUp() {
        clearDB();
        employee = new Employee("name", "123", "personalInfo", new BankInformation("bankName", "bankAccountNumber"), new EmploymentContract(new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "endDate"), new ArrayList<>(), new ArrayList<>(), 0);
    }

    @AfterEach
    void clearDB(){
        new EmployeeDAO().deleteAll();
    }

    @Test
    void addRole() {
        assert employee.getRoles().size() == 0;
        employee.addRole(Role.MANAGER);
        assert employee.getRoles().size() == 1;
        assert employee.getRoles().get(0) == Role.MANAGER;
    }

    @Test
    void addAndRemoveRole(){
        assert employee.getRoles().size() == 0;
        employee.addRole(Role.MANAGER);
        assert employee.getRoles().size() == 1;
        assert employee.getRoles().get(0) == Role.MANAGER;
        employee.removeRole(Role.MANAGER);
        assert employee.getRoles().size() == 0;
    }

    @Test
    void failRole(){
        assert employee.getRoles().size() == 0;
        employee.addRole(Role.MANAGER);
        assert employee.getRoles().size() == 1;
        assert employee.getRoles().get(0) != Role.CASHIER;
    }

}