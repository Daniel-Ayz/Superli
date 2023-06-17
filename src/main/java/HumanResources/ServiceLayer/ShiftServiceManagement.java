package HumanResources.ServiceLayer;

import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.BranchModule.BranchFacade;
import HumanResources.BusinessLayer.EmployeeModule.Driver;
import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.EmployeeFacade;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftFacade;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;

import java.util.Date;

public class ShiftServiceManagement {
    private ShiftFacade shiftFacade;
    private EmployeeFacade employeeFacade;
    private BranchFacade branchFacade;

    public ShiftServiceManagement(ShiftFacade shiftFacade, EmployeeFacade employeeFacade, BranchFacade branchFacade) {
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
        this.branchFacade = branchFacade;
    }



    public Response addShift(Date date, ShiftType shiftType, int branchId) {
        try{
            if(!branchFacade.isBranchExist(branchId))
                throw new Exception("Branch does not exist");
            shiftFacade.addShift(date, shiftType, branchId);
            if(date.getDay() == 0 && branchId == 0)
                addRequiredRoleToShift(shiftFacade.getShiftId(date, shiftType, 0),Role.DRIVER,1);
            if(date.getDay() == 0 && branchId != 0)
                addRequiredRoleToShift(shiftFacade.getShiftId(date, shiftType, branchId),Role.STOREKEEPER,1);
            return new Response("Shift created successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response approveShift(int shiftId, String employeeId, Role role) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            Employee employee = employeeFacade.getEmployee(employeeId);
            shiftFacade.approveShift(shift.getDate(), shift.getShiftType(), shift.getBranchId(), employee, role);
            return new Response("Shift approved successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }


    // adds required count of role to shift
    public Response addRequiredRoleToShift(int shiftId, Role role, int count) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            shift.addRequiredRole(role, count);
            return new Response("Role added successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    //removes required count of role from shift
    public Response removeRequiredRoleFromShift(int shiftId, Role role) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            shift.removeRequiredRole(role);
            return new Response("Role removed successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    // disapproves employee from shift
    public Response disapproveShift(int shiftId, String employeeId) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            Employee employee = employeeFacade.getEmployee(employeeId);
            shiftFacade.disapproveShift(shift.getDate(), shift.getShiftType(), shift.getBranchId(), employee);
            return new Response("Shift disapproved successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    // get all employees that are approved for shift
    public Response getApprovedEmployeesForShift(int shiftId) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            return new Response(shift.getApprovedEmployees(), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    // get all employees that requested shift
    public Response getRequestedEmployeesForShift(int shiftId) {
        try{
            Shift shift = shiftFacade.getShift(shiftId);
            return new Response(shift.getRequestedEmployees(), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    //get all upcoming shifts
    public Response getUpcomingShifts() {
        try{
            return new Response(shiftFacade.getUpcomingShifts(), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }


    public Response isStorekeeperAvailable(int branchId, Date date, ShiftType shiftType) {
        try{
            return new Response(shiftFacade.isStorekeeperAvailable(branchId,date,shiftType), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }


    public Response getDrivers(LicenseType licenseType, Date date, ShiftType shiftType) {
        try{
            return new Response(shiftFacade.getDrivers(licenseType,date,shiftType), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }


    public Response approveDriver(Driver driver, Date date) {
        try{
            shiftFacade.approveDriver(driver, date);
            return new Response("Driver shift approved successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }
}
