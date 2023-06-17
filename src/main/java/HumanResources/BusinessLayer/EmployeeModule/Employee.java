package HumanResources.BusinessLayer.EmployeeModule;

import java.util.List;

public class Employee {

    private String name;
    private String id;
    private String personalInfo;
    private BankInformation bankInformation;
    private EmploymentContract employmentContract;
    private double baseSalary;

    private String password;

    private List<Role> roles;
    private List<Salary> salaries;

    public Employee(String name, String id, String personalInfo, BankInformation bankInformation, EmploymentContract employmentContract, List<Role> role, List<Salary> salaries, double baseSalary) {
        this.name = name;
        this.id = id;
        this.personalInfo = personalInfo;
        this.bankInformation = bankInformation;
        this.employmentContract = employmentContract;
        this.roles = role;
        this.salaries = salaries;
        this.baseSalary = baseSalary;
        //TODO: REMOVE DEFAULT PASSWORD
        this.password = "123";
    }

    public Employee(String name, String id, String personalInfo, double baseSalary, String password){
        this.name = name;
        this.id = id;
        this.personalInfo = personalInfo;
        this.baseSalary = baseSalary;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void addRole(Role role) {
        if(!roles.contains(role))
            this.roles.add(role);
    }

    public void removeRole(Role role) {
        if(roles.contains(role))
            this.roles.remove(role);
    }

    public void addSalary(Salary salary) {
        if(!salaries.contains(salary))
            this.salaries.add(salary);
    }

    public void setBonusLastSalary(double bonus) {
        Salary salary = getLastSalary();
        if(salary != null)
            salary.setBonus(bonus);
    }

    public Salary getLastSalary() {
        return salaries.stream().reduce((acc, curr) -> acc.getDate().compareTo(curr.getDate()) > 0 ? acc : curr).orElse(null);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public void setRole(List<Role> roles) {
        this.roles = roles;
    }

    public BankInformation getBankInformation() {
        return bankInformation;
    }

    public void setBankInformation(BankInformation bankInformation) {
        this.bankInformation = bankInformation;
    }

    public EmploymentContract getEmploymentContract() {
        return employmentContract;
    }

    public void setEmploymentContract(EmploymentContract employmentContract) {
        this.employmentContract = employmentContract;
    }

    public List<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
    }

    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Personal Info: " + personalInfo + ", Bank Information: " + bankInformation + ", Employment Contract: " + employmentContract + ", Role: " + roles + ", Salaries: " + salaries;
    }


    public boolean equals(Employee employee) {
        if (this.name.equals(employee.name) && this.id.equals(employee.id) && this.personalInfo.equals(employee.personalInfo) && this.bankInformation.equals(employee.bankInformation) && this.employmentContract.equals(employee.employmentContract) && this.roles.equals(employee.roles) && this.salaries.equals(employee.salaries)) {
            return true;
        }
        return false;
    }

}
