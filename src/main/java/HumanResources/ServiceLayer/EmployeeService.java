package HumanResources.ServiceLayer;

import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.EmployeeModule.EmployeeFacade;
import HumanResources.BusinessLayer.EmployeeModule.Role;

import java.util.Date;
import java.util.List;

public class EmployeeService {
    private EmployeeFacade employeeFacade;

    public EmployeeService(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    public Response getEmployee(String id) {
        try{
            return new Response(employeeFacade.getEmployee(id), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }


    public Response getAllEmployees() {
        try{
            return new Response(employeeFacade.getAllEmployees(), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response addEmployee(String name, String id, String personalInfo, String bankName, String bankAccountNumber, Date startDate, String termsAndConditions, List<Role> roles, double salary, LicenseType licenseType) {
        try{
            employeeFacade.addEmployee(name, id, personalInfo, bankName, bankAccountNumber, startDate, termsAndConditions, roles, salary, licenseType);
            return new Response("Employee added successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response addRole(String id, Role role) {
        try{
            employeeFacade.addRole(id, role);
            return new Response("Role added successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response removeRole(String id, Role role) {
        try{
            employeeFacade.removeRole(id, role);
            return new Response("Role removed successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response checkPassword(String id, String password) {
        try{
            if(employeeFacade.checkPassword(id, password))
                return new Response("Password is correct", true);
            else
                return new Response("Password is incorrect", false);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response isDriver(String employeeId){
        try{
            if(employeeFacade.isDriver(employeeId))
                return new Response("Employee is a driver", true);
            else
                return new Response("Employee is not a driver", false);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }
//    //adds an employee to the system
//    public void addEmployee(String name, String id, String personalInfo, String bankName, String bankAccountNumber, Date startDate, String termsAndConditions, List<Role> roles, double salary) {
//        employeeFacade.addEmployee(name, id, personalInfo, bankName, bankAccountNumber, startDate, termsAndConditions, roles, salary);
//    }

}
