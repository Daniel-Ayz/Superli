package HumanResources.BusinessLayer.EmployeeModule;

import java.util.Date;

public class Salary {
    private double salary;
    private double bonus;
    private Date date;

    public Salary(double salary, double bonus, Date date) {
        this.salary = salary;
        this.bonus = bonus;
        this.date = date;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return "Salary: " + salary + ", Bonus: " + bonus;
    }

    public boolean equals(Salary salary) {
        if (this.salary == salary.salary && this.bonus == salary.bonus) {
            return true;
        }
        return false;
    }

}
