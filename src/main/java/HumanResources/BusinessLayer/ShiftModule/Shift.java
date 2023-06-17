package HumanResources.BusinessLayer.ShiftModule;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.DataAcessLayer.EmployeeDAL.DriverDAO;
import HumanResources.DataAcessLayer.ShiftDAL.ShiftBlockedDAO;
import HumanResources.DataAcessLayer.ShiftDAL.ShiftRequestedDAO;
import HumanResources.DataAcessLayer.ShiftDAL.ShiftRequiredDAO;
import HumanResources.ServiceLayer.ServiceFactory;

import java.util.*;

public class Shift {
    private int shiftId;
    private Date date;
    private ShiftType shiftType;
    private int branchId;
    private Map<Employee, Role> approvedEmployees;
    private List<Employee> requestedEmployees;
    private List<Employee> blockedEmployees;
    private Map<Role, Integer> requiredEmployees;

    public Shift(int shiftId, Date date, ShiftType shiftType, int branchId) {
        this.shiftId = shiftId;
        this.date = date;
        this.shiftType = shiftType;
        this.branchId = branchId;
        this.approvedEmployees = new HashMap<>();
        this.requestedEmployees = new ArrayList<>();
        this.blockedEmployees = new ArrayList<>();
        this.requiredEmployees = new HashMap<>();

    }


    public void insertRequestedEmployees(List<Integer> requested){
        for(int request: requested){
            Employee employee = (Employee)ServiceFactory.getInstance().getEmployeeService().getEmployee(Integer.toString(request)).getData();
            requestedEmployees.add(employee);
        }
    }
    public void insertBlockedEmployees(List<Integer> blocked){
        for(int request: blocked){
            Employee employee = (Employee)ServiceFactory.getInstance().getEmployeeService().getEmployee(Integer.toString(request)).getData();
            blockedEmployees.add(employee);
        }
    }

    public void insertApprovedEmployees(Map<Integer,Role> approved){
        for(Map.Entry<Integer,Role> entry: approved.entrySet()){
            Employee employee = (Employee)ServiceFactory.getInstance().getEmployeeService().getEmployee(Integer.toString(entry.getKey())).getData();
            Role role = entry.getValue();
            approvedEmployees.put(employee,role);
        }
    }
    public void insertRequiredRoles(Map<Role,Integer> requiredRoles){
        for(Map.Entry<Role,Integer> entry: requiredRoles.entrySet()){
            Role role = entry.getKey();
            requiredEmployees.put(role,entry.getValue());
        }
    }
    public void addRequiredManager(int amount) {
        if(new ShiftRequiredDAO().insert(shiftId,"MANAGER",amount))
            requiredEmployees.put(Role.MANAGER, amount);
        else
            throw new IllegalArgumentException("role already added");
    }

    public void addRequiredRole(Role role, int amount) throws RuntimeException{
        if(!requiredEmployees.containsKey(role)) {
            boolean inserted = new ShiftRequiredDAO().insert(shiftId,role.toString(),amount);
            if(inserted)
                requiredEmployees.put(role, amount);
            else
                throw new RuntimeException("cannot insert to db");
        }
        else{
            boolean updated = new ShiftRequiredDAO().update(shiftId, role.toString(), amount);
            if(updated)
                requiredEmployees.put(role, amount);
            else
                throw new RuntimeException("cannot update db");
        }
    }

    public Map<Role, Integer> getRequiredEmployees() {
        return requiredEmployees;
    }

    public void removeRequiredRole(Role role) {
        if(new ShiftRequiredDAO().delete(shiftId,role.toString()))
            requiredEmployees.remove(role);
        else
            throw new IllegalArgumentException("role isn't required for shift");
    }

    public boolean isRoleApproved(Role role){
        for (Map.Entry<Employee,Role> entry : approvedEmployees.entrySet()){
            if(entry.getValue().equals(role))
                return true;
        }
        return false;
    }

    public void addApprovedEmployee(Employee employee, Role role) {
        approvedEmployees.put(employee, role);
    }

    public void addRequestedEmployee(Employee employee) {
        requestedEmployees.add(employee);
    }

    public void addBlockedEmployee(Employee employee) {
        blockedEmployees.add(employee);
    }

    public void removeApprovedEmployee(Employee employee) {
        approvedEmployees.remove(employee);
    }

    public void removeRequestedEmployee(Employee employee) {
        requestedEmployees.remove(employee);
    }

    public void removeBlockedEmployee(Employee employee) {
        blockedEmployees.remove(employee);
    }

    public Map<Employee, Role> getApprovedEmployees() {
        return approvedEmployees;
    }

    public List<Employee> getRequestedEmployees() {
        return requestedEmployees;
    }

    public List<Employee> getBlockedEmployees() {
        return blockedEmployees;
    }

    public boolean isApproved(Employee employee) {
        return approvedEmployees.containsKey(employee);
    }

    public boolean isRequested(Employee employee) {
        return requestedEmployees.contains(employee);
    }

    public boolean isBlocked(Employee employee) {
        return blockedEmployees.contains(employee);
    }

    public int getShiftId() {
        return shiftId;
    }

    public int getBranchId() {
        return branchId;
    }

    public Date getDate() {
        return date;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public String toString() {
        return "Date: " + date + ", Shift type: " + shiftType;
    }

    public boolean equals(Shift shift) {
        if (this.date == shift.date && this.shiftType == shift.shiftType) {
            return true;
        }
        return false;
    }

    public boolean isDayShift() {
        if (shiftType == ShiftType.MORNING) {
            return true;
        }
        return false;
    }

    public boolean isNightShift() {
        if (shiftType == ShiftType.NIGHT) {
            return true;
        }
        return false;
    }

//    function that gives the day of the shift
    public String getDayOfTheWeek() {
        int day = date.getDay();
        switch (day) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return "Error";
    }

    // function that checks if the shift on the same date
    public boolean isSameDate(Shift shift) {
        if (this.date.equals(shift.date)) {
            return true;
        }
        return false;
    }





}
