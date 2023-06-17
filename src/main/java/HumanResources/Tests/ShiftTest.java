package HumanResources.Tests;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.BranchDAL.BranchDAO;
import HumanResources.DataAcessLayer.EmployeeDAL.EmployeeDAO;
import HumanResources.DataAcessLayer.ShiftDAL.ShiftIdDAO;
import HumanResources.ServiceLayer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

class ShiftTest {

    private ShiftServiceManagement shiftServiceManagement;
    private ShiftServiceEmployee shiftServiceEmployee;
    private ServiceFactory serviceFactory;
    private EmployeeService employeeService;
    private BranchService branchService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        clearDB();
        serviceFactory = ServiceFactory.getInstance();
        serviceFactory.reset();
        shiftServiceEmployee = serviceFactory.getShiftServiceEmployee();
        shiftServiceManagement = serviceFactory.getShiftServiceManagement();
        employeeService = serviceFactory.getEmployeeService();
        branchService = serviceFactory.getBranchService();
        branchService.addBranch(1,"7:00","15:00","15:00","23:00");
        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(Role.CASHIER);
        employeeService.addEmployee("name", "1", "personalInfo", "bankAccountNumber","12342134", new Date(23,3,2020),"bla bla", roles, 0, null);
        //employee = new Employee("name", "id", "personalInfo", new BankInformation("bankName", "bankAccountNumber"), new EmploymentContract(new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "endDate"), new ArrayList<>(), new ArrayList<>(), 0);
    }


    @AfterEach
    void clearDB(){
        new BranchDAO().deleteAll();
        new EmployeeDAO().deleteAll();
        new ShiftIdDAO().deleteAll();
    }

    @Test
    void addShift(){
        Response r = shiftServiceManagement.addShift(new Date (2023,3,3), ShiftType.MORNING,1);
        assert r.isSuccess();
    }
    @Test
    void addShiftToNoneExistingBranch(){
        Response r = shiftServiceManagement.addShift(new Date (2023,3,3), ShiftType.MORNING,3);
        assert !r.isSuccess();
    }
   @Test
    void addRequest(){
        Date date = new Date (2023,3,3);
        shiftServiceManagement.addShift(date, ShiftType.MORNING,1);
       shiftServiceManagement.addRequiredRoleToShift(((Integer)shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1).getData()),Role.CASHIER, 1);
       Response r0 = shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1);
       int id=-1;
       if(r0.getData() instanceof Integer){
           id = (int)r0.getData();
       }
        Response r = shiftServiceEmployee.requestShiftByEmployee("1",id);
        assert r.isSuccess();
    }

    @Test
    void approveRequestToEmpWithoutRole(){
        Date date = new Date (2023,3,3);
        shiftServiceManagement.addShift(date, ShiftType.MORNING,1);
        shiftServiceManagement.addRequiredRoleToShift(((Integer)shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1).getData()),Role.CASHIER, 1);
        Response r0 = shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1);
        int id=-1;
        if(r0.getData() instanceof Integer){
            id = (int)r0.getData();
        }
        Response r = shiftServiceEmployee.requestShiftByEmployee("1",id);
        assert r.isSuccess();
        shiftServiceManagement.removeRequiredRoleFromShift(id,Role.CASHIER);
        Response r1 = shiftServiceManagement.approveShift(id,"1",Role.MANAGER);
        assert !r1.isSuccess();
    }

    @Test
    void approveRequestToEmpWithRole(){
        Date date = new Date (2023,3,3);
        shiftServiceManagement.addShift(date, ShiftType.MORNING,1);
        shiftServiceManagement.addRequiredRoleToShift(((Integer)shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1).getData()),Role.CASHIER, 1);
        Response r0 = shiftServiceEmployee.getShiftId(date,ShiftType.MORNING,1);
        int id=-1;
        if(r0.getData() instanceof Integer){
            id = (int)r0.getData();
        }
        Response r = shiftServiceEmployee.requestShiftByEmployee("1",id);
        assert r.isSuccess();
        Response r1 = shiftServiceManagement.approveShift(id,"1",Role.CASHIER);
        assert r1.isSuccess();
    }
}