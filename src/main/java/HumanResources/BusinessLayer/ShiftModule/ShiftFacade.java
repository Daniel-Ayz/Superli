package HumanResources.BusinessLayer.ShiftModule;

import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.EmployeeModule.Driver;
import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.DataAcessLayer.ShiftDAL.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ShiftFacade {
    private Map<Date, List<Shift>> shifts;
    int shiftCounter;

    public ShiftFacade() {
        shifts = new HashMap<Date, List<Shift>>();
        shiftCounter = 0;
    }

    public void loadData() {
        shiftCounter = new ShiftIdDAO().getShiftID();
        if(shiftCounter == -1) {
            new ShiftIdDAO().insert(0);
            shiftCounter=0;
        }
        ArrayList<Shift> shiftsDAL = new ShiftDAO().selectAll();
        for(Shift shift: shiftsDAL){
            if(!shifts.containsKey(shift.getDate()))
                shifts.put(shift.getDate(), new ArrayList<Shift>());
            shifts.get(shift.getDate()).add(shift);
        }
        loadDataShifts();
    }

    public void loadDataShifts(){
        //loading shift requests
        Map<Integer,List<Integer>> requestedShifts = new ShiftRequestedDAO().selectAll();
        for(Map.Entry<Integer,List<Integer>> entry: requestedShifts.entrySet()){
            getShift(entry.getKey()).insertRequestedEmployees(entry.getValue());
        }
        //loading blocked employees on shifts
        Map<Integer,List<Integer>> blockedShifts = new ShiftBlockedDAO().selectAll();
        for(Map.Entry<Integer,List<Integer>> entry: blockedShifts.entrySet()){
            getShift(entry.getKey()).insertBlockedEmployees(entry.getValue());
        }
        //loading approved employees on shifts
        Map<Integer,Map<Integer,Role>> approvedShifts = new ShiftApprovedDAO().selectAll();
        for(Map.Entry<Integer,Map<Integer,Role>> entry: approvedShifts.entrySet()){
            getShift(entry.getKey()).insertApprovedEmployees(entry.getValue());
        }
        //loading required roles for shifts
        Map<Integer,Map<Role,Integer>> requiredRoles = new ShiftRequiredDAO().selectAll();
        for(Map.Entry<Integer,Map<Role,Integer>> entry: requiredRoles.entrySet()){
            getShift(entry.getKey()).insertRequiredRoles(entry.getValue());
        }
    }

    // add a shift to a specific date (private)
    private void addShift(Date date, Shift shift) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        boolean isInserted = new ShiftDAO().insert(shift.getShiftId(),strDate,shift.getShiftType().toString(),shift.getBranchId());
        if(isInserted) {
            if(!shifts.containsKey(date))
                shifts.put(date, new ArrayList<Shift>());
            shifts.get(date).add(shift);
        }
        else
            throw new IllegalArgumentException("shift already exists");
    }

    // add new shift to a specific date and branch
    public void addShift(Date date, ShiftType shiftType, int branchId) {
        if(isShiftExist(date, shiftType, branchId))
            throw new IllegalArgumentException("Shift already exist");
        Shift shift = new Shift(shiftCounter++, date, shiftType, branchId);
        new ShiftIdDAO().update(shiftCounter);
        //all shifts require at least one manager
        addShift(date, shift);
        shift.addRequiredManager(1);
    }

    public Shift getShift(int id){
        for (List<Shift> shiftList : shifts.values()) {
            for (Shift shift : shiftList) {
                if(shift.getShiftId() == id)
                    return shift;
            }
        }
        return null;
    }

    public int getShiftId(Date date,ShiftType shiftType,int branchID){
        List<Shift> shift = shifts.get(date);
        if (shift == null)
            throw new IllegalArgumentException("There are no shift's at the time entered");
        for(Shift s:shift){
            if(s.getShiftType()==(shiftType)){
                if(s.getBranchId() == branchID){
                    return s.getShiftId();
                }
            }
        }
        throw new RuntimeException("No shift found");
    }

    // check if a shift exist
    public boolean isShiftExist(Date date, ShiftType shiftType, int branchId) {
        return shifts.containsKey(date) && shifts.get(date).stream().anyMatch(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId);
    }

    // check if an employee already requested a shift
    public boolean isEmployeeRequestedShift(Date date, ShiftType shiftType, int branchId, Employee employee) {
        return shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().getRequestedEmployees().contains(employee);
    }

    public boolean isEmployeeBlockedShift(Date date, ShiftType shiftType, int branchId, Employee employee) {
        return shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().getBlockedEmployees().contains(employee);
    }

    public boolean isEmployeeApprovedShift(Date date, ShiftType shiftType, int branchId, Employee employee) {
        return shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().getApprovedEmployees().containsKey(employee);
    }

    // request a shift for an employee (add to requested employees)
    public void requestShift(Date date, ShiftType shiftType, int branchId, Employee employee) throws Exception {
        if (!isShiftExist(date, shiftType, branchId))
            throw new Exception("Shift does not exist");
        if (isEmployeeRequestedShift(date, shiftType, branchId, employee))
            throw new Exception("Employee already requested this shift");
        if (isEmployeeBlockedShift(date, shiftType, branchId, employee))
            throw new Exception("Employee is blocked on this shift");
        if(!isOneOfRolesRequired(date,shiftType,employee.getRoles(),branchId))
            throw new Exception("Employee roles are not required for this shift");
        if(requestShiftFromDAL(date,shiftType,branchId,Integer.parseInt(employee.getId())))
            shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().addRequestedEmployee(employee);
        else
            throw new IllegalArgumentException("shift request already exists");
    }

    private boolean requestShiftFromDAL(Date date, ShiftType shiftType, int branchId,int employeeId){
        int shiftId = getShiftId(date,shiftType,branchId);
        return new ShiftRequestedDAO().insert(shiftId,employeeId);
    }

    public boolean isRoleRequired(Date date, ShiftType shiftType, Role role,int branchId) {
        return shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().getRequiredEmployees().containsKey(role);
    }

    public boolean isOneOfRolesRequired(Date date, ShiftType shiftType, List<Role> roles,int branchId) {
        return shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().getRequiredEmployees().keySet().stream().anyMatch(roles::contains);
    }

    // approve a shift for an employee (add to approved employees and remove from requested employees, and block all the shifts of an employee on a specific date)
    public void approveShift(Date date, ShiftType shiftType, int branchId, Employee employee, Role role) throws Exception {
        if(!isShiftExist(date, shiftType, branchId))
            throw new Exception("Shift does not exist");
        if(!isEmployeeRequestedShift(date, shiftType, branchId, employee))
            throw new Exception("Employee did not request this shift");
        if(isEmployeeBlockedShift(date, shiftType, branchId, employee))
            throw new Exception("Employee is blocked from this shift");
        if(!employee.getRoles().contains(role))
            throw new Exception("Employee doesnt have this role");
        if(!isRoleRequired(date, shiftType, role,branchId))
            throw new Exception("Role is not required for this shift");

        if(approveEmployeeDAL(date,shiftType,branchId,Integer.parseInt(employee.getId()),role) && removeEmployeeFromRequestedDAL(date,shiftType,branchId,Integer.parseInt(employee.getId())) && blockEmployeeDAL(date,Integer.parseInt(employee.getId()))) {
            //add employee to approved employees
            shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().addApprovedEmployee(employee, role);
            //remove employee from requested employees
            shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().removeRequestedEmployee(employee);
            // block all the shifts of an employee on a specific date
            blockShifts(date, employee);
        }else{
            throw new Exception("DAL procedure failed");
        }
    }
    private boolean approveEmployeeDAL(Date date, ShiftType shiftType, int branchId,int employeeId,Role role){
        int shiftId = getShiftId(date,shiftType,branchId);
        return new ShiftApprovedDAO().insert(shiftId,employeeId, role.toString());
    }
    private boolean removeEmployeeFromRequestedDAL(Date date, ShiftType shiftType, int branchId,int employeeId){
        int shiftId = getShiftId(date,shiftType,branchId);
        return new ShiftRequestedDAO().delete(shiftId,employeeId);
    }
    private boolean blockEmployeeDAL(Date date,int employeeId){
        ArrayList<Integer> shiftsId = getAllShiftsToBlock(date);
        for(int shiftId : shiftsId){
            if(!new ShiftBlockedDAO().insert(shiftId,employeeId))
                return false;
        }
        return true;
    }
    private ArrayList<Integer> getAllShiftsToBlock(Date date) {
        List<Shift> shiftsOnDate =  shifts.get(date);
        ArrayList<Integer> shiftsId = new ArrayList<>();
        for (Shift s:
             shiftsOnDate) {
            shiftsId.add(s.getShiftId());
        }
        return shiftsId;
    }
    // block all the shifts of an employee on a specific date
    public void blockShifts(Date date, Employee employee) throws Exception {
        if(!shifts.containsKey(date))
            throw new Exception("Shift does not exist");
        shifts.get(date).stream().forEach(shift -> shift.addBlockedEmployee(employee));
    }


    public List<Shift> getAvailableShiftsFromDate(Date date, Employee employee){
        List<Shift> availableShifts = new ArrayList<>();
        for (List<Shift> shiftList : shifts.values()) {
            for (Shift shift : shiftList) {
                if(!shift.getRequestedEmployees().contains(employee) && !shift.getApprovedEmployees().containsKey(employee) && !shift.getBlockedEmployees().contains(employee) && isShiftAfterDate(date, shift))
                    if(shift.getRequiredEmployees().keySet().stream().anyMatch(role -> employee.getRoles().contains(role)))
                        availableShifts.add(shift);
            }
        }
        return availableShifts;
    }

    // get all the shifts that the employee is approved on
    public List<Shift> getApprovedShiftsFromDate(Date date, Employee employee){
        List<Shift> approvedShifts = new ArrayList<>();
        for (List<Shift> shiftList : shifts.values()) {
            for (Shift shift : shiftList) {
                if(shift.getApprovedEmployees().containsKey(employee) && isShiftAfterDate(date, shift))
                    approvedShifts.add(shift);
            }
        }
        return approvedShifts;
    }

    // disapprove an approved shift for an employee (remove from approved employees and add to requested employees and unblock all the shifts of an employee on a specific date)
    public void disapproveShift(Date date, ShiftType shiftType, int branchId, Employee employee) throws Exception {
        if(!isShiftExist(date, shiftType, branchId))
            throw new Exception("Shift does not exist");
        if(!isEmployeeApprovedShift(date, shiftType, branchId, employee))
            throw new Exception("Employee isn't approved on this shift");
        if(disapproveEmployeeDAL(date,shiftType,branchId,Integer.parseInt(employee.getId())) && addEmployeeToRequestedDAL(date,shiftType,branchId,Integer.parseInt(employee.getId())) && unblockEmployeeDAL(date,Integer.parseInt(employee.getId()))) {
            //remove employee from approved employees
            shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().removeApprovedEmployee(employee);
            //add employee to requested employees
            shifts.get(date).stream().filter(shift -> shift.getShiftType() == shiftType && shift.getBranchId() == branchId).findFirst().get().addRequestedEmployee(employee);
            // unblock all the shifts of an employee on a specific date
            unblockShifts(date, employee);
        }else{
            throw new Exception("DAL procedure failed");
        }
    }
    private boolean disapproveEmployeeDAL(Date date, ShiftType shiftType, int branchId,int employeeId){
        int shiftId = getShiftId(date,shiftType,branchId);
        return new ShiftApprovedDAO().delete(shiftId,employeeId);
    }
    private boolean addEmployeeToRequestedDAL(Date date, ShiftType shiftType, int branchId,int employeeId){
        int shiftId = getShiftId(date,shiftType,branchId);
        return new ShiftRequestedDAO().insert(shiftId,employeeId);
    }
    private boolean unblockEmployeeDAL(Date date,int employeeId){
        ArrayList<Integer> shiftsId = getAllShiftsToBlock(date);
        for(int shiftId : shiftsId){
            if(!new ShiftBlockedDAO().delete(shiftId,employeeId))
                return false;
        }
        return true;
    }
    private void unblockShifts(Date date, Employee employee) {
        shifts.get(date).stream().filter(shift -> shift.getBlockedEmployees().contains(employee)).forEach(shift -> shift.removeBlockedEmployee(employee));
    }

    //get all upcoming shifts that are in the future (for all employees)
    public List<Shift> getUpcomingShifts(){
        List<Shift> upcomingShifts = new ArrayList<>();
        for (List<Shift> shiftList : shifts.values()) {
            for (Shift shift : shiftList) {
                if(isShiftAfterDate(new Date(), shift))
                    upcomingShifts.add(shift);
            }
        }
        return upcomingShifts;
    }

    private boolean isShiftAfterDate(Date date, Shift shift){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return !shift.getDate().before(date) || fmt.format(shift.getDate()).equals(fmt.format(date));
    }

    public boolean isStorekeeperAvailable(int branchId, Date date, ShiftType shiftType) {
        int shiftId = getShiftId(date,shiftType,branchId);
        return getShift(shiftId).isRoleApproved(Role.STOREKEEPER);
    }

    public List<Driver> getDrivers(LicenseType licenseType, Date date, ShiftType shiftType) {
        int shiftId = getShiftId(date,shiftType,0);
        List<Employee> employees = getShift(shiftId).getRequestedEmployees();
        List<Driver> drivers = new ArrayList<>();
        for (Employee e : employees){
            if(e.getRoles().contains(Role.DRIVER) && canDrive(licenseType,((Driver)e).getDriverLicense())){
                Driver d = (Driver)e;
                drivers.add(d);
            }
        }
        return drivers;
    }

    private boolean canDrive(LicenseType requiredLicenseType,LicenseType driverLicenseType) {
        if (driverLicenseType == LicenseType.COLD_HEAVY)
            return true;
        else if (driverLicenseType == LicenseType.REGULAR_HEAVY && (requiredLicenseType == LicenseType.REGULAR_HEAVY || requiredLicenseType == LicenseType.REGULAR_LIGHT))
            return true;
        else if (driverLicenseType == LicenseType.COLD_LIGHT && (requiredLicenseType == LicenseType.COLD_LIGHT || requiredLicenseType == LicenseType.REGULAR_LIGHT))
            return true;
        else if (driverLicenseType == LicenseType.REGULAR_LIGHT && requiredLicenseType == LicenseType.REGULAR_LIGHT)
            return true;
        else
            return false;
    }

    public void approveDriver(Driver driver, Date date) throws Exception {
        approveShift(date, ShiftType.MORNING, 0, driver, Role.DRIVER);
    }
}
