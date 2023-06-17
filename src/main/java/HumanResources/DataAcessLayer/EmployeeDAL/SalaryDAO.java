package HumanResources.DataAcessLayer.EmployeeDAL;

import HumanResources.BusinessLayer.EmployeeModule.Salary;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalaryDAO extends DAO {
    private static final String TABLE_NAME = "Salary";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SALARY = "salary";
    private static final String COLUMN_BONUS = "bonus";

    private final SimpleDateFormat sdf;

    public SalaryDAO() {
        super(TABLE_NAME);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public List<Salary> getById(int employeeId) {
        ArrayList<Salary> result = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {

            //process ResultSet
            while(rs.next()){
                String dateString = rs.getString(COLUMN_DATE);
                double salary = rs.getDouble(COLUMN_SALARY);
                double bonus = rs.getDouble(COLUMN_BONUS);
                Date date = sdf.parse(dateString); // convert text to date
                result.add(new Salary(salary, bonus, date));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (ParseException e){
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementGetById(Connection con, int employeeId) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        return ps;
    }

    public boolean insert(int employeeId, Date date, double salary, double bonus) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, sdf.format(date), salary, bonus);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String date, double salary, double bonus) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s,%s,%s) VALUES(?,?,?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_DATE, COLUMN_SALARY, COLUMN_BONUS);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, date);
        ps.setDouble(3, salary);
        ps.setDouble(3, bonus);
        return ps;
    }

    public Map<Integer, List<Salary>> selectAll(){
        Map<Integer, List<Salary>> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String dateString = rs.getString(COLUMN_DATE);
                double salary = rs.getDouble(COLUMN_SALARY);
                double bonus = rs.getDouble(COLUMN_BONUS);
                Date date = sdf.parse(dateString); // convert text to date
                Salary s = new Salary(salary, bonus, date);
                if (!result.containsKey(employeeId)){
                    List<Salary> list = new ArrayList<>();
                    result.put(employeeId, list);
                }
                result.get(employeeId).add(s);
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        catch (ParseException e){
            //e.printStackTrace();
        }
        //finally, return result
        return result;
    }

    private PreparedStatement createPreparedStatementSelectAll(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", tableName);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }

    public boolean update(int employeeId, Date date, double salary, double bonus) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, sdf.format(date), salary, bonus);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String date, double salary, double bonus) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?", tableName, COLUMN_DATE, COLUMN_SALARY,COLUMN_BONUS, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, date);
        ps.setDouble(2, salary);
        ps.setDouble(3, salary);
        ps.setInt(4, employeeId);
        return ps;
    }

    public boolean delete(int employeeId) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, employeeId);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int employeeId) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        return ps;
    }
}
