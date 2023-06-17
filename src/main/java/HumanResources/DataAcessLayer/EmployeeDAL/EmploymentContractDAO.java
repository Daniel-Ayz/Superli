package HumanResources.DataAcessLayer.EmployeeDAL;

import HumanResources.BusinessLayer.EmployeeModule.EmploymentContract;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmploymentContractDAO extends DAO {
    private static final String TABLE_NAME = "EmploymentContract";
    private static final String COLUMN_EMPLOYEE_ID = "employeeId";
    private static final String COLUMN_START_DATE = "startDate";
    private static final String COLUMN_TERMS_AND_CONDITIONS = "termsAndConditions";
    private final SimpleDateFormat sdf;

    public EmploymentContractDAO() {
        super(TABLE_NAME);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public EmploymentContract getById(int employeeId) {
        EmploymentContract result = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetById(con, employeeId);
             ResultSet rs = ps.executeQuery()) {

            //process ResultSet
            while(rs.next()){
                String startDateString = rs.getString(COLUMN_START_DATE);
                String termsAndConditions = rs.getString(COLUMN_TERMS_AND_CONDITIONS);
                Date date = sdf.parse(startDateString); // convert text to date
                result = new EmploymentContract(date, termsAndConditions);
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

    public boolean insert(int employeeId, Date startDate, String termsAndConditions) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con, employeeId, sdf.format(startDate), termsAndConditions);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con, int employeeId, String startDate, String termsAndConditions) throws SQLException {
        String sql = String.format("INSERT INTO %s(%s,%s,%s) VALUES(?,?,?)", tableName, COLUMN_EMPLOYEE_ID, COLUMN_START_DATE, COLUMN_TERMS_AND_CONDITIONS);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, employeeId);
        ps.setString(2, startDate);
        ps.setString(3, termsAndConditions);
        return ps;
    }

    public Map<Integer, EmploymentContract> selectAll(){
        Map<Integer, EmploymentContract> result = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){
                int employeeId = rs.getInt(COLUMN_EMPLOYEE_ID);
                String startDateString = rs.getString(COLUMN_START_DATE);
                String termsAndConditions = rs.getString(COLUMN_TERMS_AND_CONDITIONS);
                Date startDate = sdf.parse(startDateString); // convert text to date
                result.put(employeeId, new EmploymentContract(startDate, termsAndConditions));
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

    public boolean update(int employeeId, Date startDate, String termsAndConditions) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementUpdate(con, employeeId, sdf.format(startDate), termsAndConditions);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementUpdate(Connection con, int employeeId, String startDate, String termsAndConditions) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tableName, COLUMN_START_DATE, COLUMN_TERMS_AND_CONDITIONS, COLUMN_EMPLOYEE_ID);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, startDate);
        ps.setString(2, termsAndConditions);
        ps.setInt(3, employeeId);
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
