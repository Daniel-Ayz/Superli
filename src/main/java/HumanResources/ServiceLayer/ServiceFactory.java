package HumanResources.ServiceLayer;

import HR_Delivery.HrDeliveryService;
import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.BranchModule.BranchFacade;
import HumanResources.BusinessLayer.EmployeeModule.EmployeeFacade;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.ShiftFacade;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.BranchDAL.BranchDAO;
import HumanResources.DataAcessLayer.EmployeeDAL.EmployeeDAO;
import HumanResources.DataAcessLayer.ShiftDAL.ShiftIdDAO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ServiceFactory {
    private static ServiceFactory instance = null;
    private ShiftServiceManagement shiftServiceManagement;
    private ShiftServiceEmployee shiftServiceEmployee;
    private EmployeeService employeeService;
    private BranchService branchService;

    private ShiftFacade shiftFacade;
    private EmployeeFacade employeeFacade;
    private BranchFacade branchFacade;

    private ServiceFactory() {
        shiftFacade = new ShiftFacade();
        employeeFacade = new EmployeeFacade();
        branchFacade = new BranchFacade();
        branchService = new BranchService(branchFacade);
        employeeService = new EmployeeService(employeeFacade);
        shiftServiceEmployee = new ShiftServiceEmployee(shiftFacade, employeeFacade);
        shiftServiceManagement = new ShiftServiceManagement(shiftFacade, employeeFacade, branchFacade);
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
            instance.loadData();
        }
        return instance;
    }

    private void loadData(){
        employeeFacade.loadData();
        branchFacade.loadData();
        shiftFacade.loadData();
    }

    public void reset() {
        instance = new ServiceFactory();
        instance.loadData();
    }

    public ShiftServiceManagement getShiftServiceManagement() {
        return shiftServiceManagement;
    }

    public ShiftServiceEmployee getShiftServiceEmployee() {
        return shiftServiceEmployee;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public BranchService getBranchService() {
        return branchService;
    }

    public static void initHR() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        initBranches(serviceFactory.getBranchService());
        initEmployees(serviceFactory.getEmployeeService());
        initMorningShifts(serviceFactory.getShiftServiceManagement(), 1);
        initMorningShifts(serviceFactory.getShiftServiceManagement(), 2);
        initDriverShifts(serviceFactory.getShiftServiceManagement());
        initNightShifts(serviceFactory.getShiftServiceManagement(), 1);
        initNightShifts(serviceFactory.getShiftServiceManagement(), 2);
        requestShiftsDrivers(serviceFactory.getShiftServiceEmployee());
    }

    private static void initBranches(BranchService branchService){
        HrDeliveryService hrd = new HrDeliveryService();
        hrd.addBranch(1, "08:00", "16:00", "16:00", "21:00", "Rager 9", "SOUTH", "052-1111111", "Miri", -10, 3);
        hrd.addBranch(2, "08:00", "16:00", "16:00", "21:30", "Rager 19", "SOUTH", "052-2222222", "Danel", -10, 13);
        hrd.addBranch(3, "09:00", "15:00", "16:00", "21:30", "Rager 29", "SOUTH", "052-3333333", "Yael", -10, 23);
        hrd.addBranch(4, "10:00", "14:00", "15:00", "21:30", "The Village Afula", "NORTH", "052-4444444", "Ron The Farmer(not mi)", -10, 33);
    }

    private static void initEmployees(EmployeeService employeeService){
        employeeService.addEmployee("John", "123", "Bla Bla", "12", "101", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Scam conditions+management", new ArrayList<>(Arrays.asList(Role.MANAGER)),2000, null);
        employeeService.addEmployee("Jane", "456", "Bla Bla", "12", "202", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Scam conditions+", new ArrayList<>(Arrays.asList(Role.CASHIER, Role.STOREKEEPER)),1500, null);
        employeeService.addEmployee("Jack", "789", "Bla Bla", "12", "303", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Scam conditions+", new ArrayList<>(Arrays.asList(Role.CASHIER, Role.STEWARD)),1500, null);
        employeeService.addEmployee("Jill", "101", "Bla Bla", "12", "404", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Scam conditions", new ArrayList<>(Arrays.asList(Role.CASHIER)),1000, null);
        employeeService.addEmployee("Ronmi", "8181", "Bla Bla", "12", "505", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Drive Fast", new ArrayList<>(Arrays.asList(Role.DRIVER)),1000, LicenseType.COLD_HEAVY);
        employeeService.addEmployee("Itay", "8200", "Bla Bla", "12", "606", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Drive Fast", new ArrayList<>(Arrays.asList(Role.DRIVER)),1000, LicenseType.COLD_LIGHT);
        employeeService.addEmployee("Miron", "1818", "Bla Bla", "12", "707", new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), "Drive Fast", new ArrayList<>(Arrays.asList(Role.DRIVER)),1000, LicenseType.COLD_HEAVY);
    }

    private static void initMorningShifts(ShiftServiceManagement shiftServiceManagement, int branchId) {
        shiftServiceManagement.addShift(new GregorianCalendar(2024, Calendar.JANUARY, 1).getTime(), ShiftType.MORNING, branchId);
        shiftServiceManagement.addShift(new GregorianCalendar(2024, Calendar.JANUARY, 2).getTime(), ShiftType.MORNING, branchId);
    }

    private static void initDriverShifts(ShiftServiceManagement shiftServiceManagement) {
        shiftServiceManagement.addShift(convertToDateViaInstant(getClosestSunday()), ShiftType.MORNING, 0);
    }

    private static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private static LocalDate getClosestSunday() {
        LocalDate now = LocalDate.now();
        LocalDate nextSunday = now.with(DayOfWeek.SUNDAY);
        if (now.isAfter(nextSunday)) {
            nextSunday = nextSunday.plusWeeks(1);
        }
        return nextSunday;
    }

    private static void initNightShifts(ShiftServiceManagement shiftServiceManagement, int branchId) {
        shiftServiceManagement.addShift(new GregorianCalendar(2024, Calendar.JANUARY, 1).getTime(), ShiftType.NIGHT, branchId);
        shiftServiceManagement.addShift(new GregorianCalendar(2024, Calendar.JANUARY, 2).getTime(), ShiftType.NIGHT, branchId);
    }

    private static void requestShiftsDrivers(ShiftServiceEmployee shiftServiceEmployee) {
        shiftServiceEmployee.requestShiftByEmployee("8181", 4);
        shiftServiceEmployee.requestShiftByEmployee("8200", 4);
        shiftServiceEmployee.requestShiftByEmployee("1818", 4);
    }

    public static void clearHRDB() {
        new BranchDAO().deleteAll();
        new EmployeeDAO().deleteAll();
        new ShiftIdDAO().deleteAll();
        ServiceFactory.getInstance().reset();
    }
}
