package HumanResources.ServiceLayer;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.EmployeeFacade;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftFacade;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;

import java.util.Date;

public class ShiftServiceEmployee {

    private ShiftFacade shiftFacade;
    private EmployeeFacade employeeFacade;

    public ShiftServiceEmployee(ShiftFacade shiftFacade, EmployeeFacade employeeFacade) {
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
    }

    public Response requestShiftByEmployee(String employeeId, int shiftId) {
        try{
            Employee employee = employeeFacade.getEmployee(employeeId);
            Shift shift = shiftFacade.getShift(shiftId);
            shiftFacade.requestShift(shift.getDate(), shift.getShiftType(), shift.getBranchId(), employee);
            return new Response("Shift requested successfully", true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response getAllShiftsAvailableFromDate(Date date, String employeeId) {
        try{
            Employee employee = employeeFacade.getEmployee(employeeId);
            return new Response(shiftFacade.getAvailableShiftsFromDate(date, employee), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response getAllShiftsApprovedFromDate(Date date, String employeeId) {
        try{
            Employee employee = employeeFacade.getEmployee(employeeId);
            return new Response(shiftFacade.getApprovedShiftsFromDate(date, employee), true);
        }
        catch (Exception e){
            return new Response(e.getMessage(), false);
        }
    }

    public Response getShiftId(Date date, ShiftType shiftType, int branchId){
        try{
            int id = shiftFacade.getShiftId(date,shiftType,branchId);
            return new Response(id,true);
        }
        catch(Exception e){
            return new Response(e.getMessage(),false);
        }
    }

}
