package HumanResources.DataAcessLayer.ShiftDAL;

import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShiftDAO extends DAO {
    private static final String TABLE_NAME = "Shift";

    public ShiftDAO() {
        super(TABLE_NAME);
    }

    public Shift getByShiftID(int shiftID) {
        Shift shift = null;
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementGetByDate(con, shiftID);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()) {
                String sDate = rs.getString("shiftDate");
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                ShiftType sType = ShiftType.valueOf(rs.getString("shiftType"));
                shift = new Shift(rs.getInt("shiftID"), date, sType, rs.getInt("branchID"));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        } catch (ParseException e){
            //e.printStackTrace();
        }
        //finally, return result
        return shift;
    }

    private PreparedStatement createPreparedStatementGetByDate(Connection con, int shiftID) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE shiftID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        return ps;
    }

    public boolean insert(int shiftID, String shiftDate, String shiftType, int branchID) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementInsert(con,shiftID, shiftDate,shiftType,branchID);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementInsert(Connection con,int shiftID, String shiftDate, String shiftType,int branchID) throws SQLException {
        String sql = String.format("INSERT INTO %s(shiftID,shiftDate,shiftType,branchID) VALUES(?,?,?,?)", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        ps.setString(2, shiftDate);
        ps.setString(3, shiftType);
        ps.setInt(4, branchID);
        return ps;
    }

    public ArrayList<Shift> selectAll(){
        ArrayList<Shift> shifts = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementSelectAll(con);
             ResultSet rs = ps.executeQuery()) {
            //process ResultSet
            while(rs.next()){

                String sDate = rs.getString("shiftDate");
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                ShiftType sType = ShiftType.valueOf(rs.getString("shiftType"));
                shifts.add(new Shift(rs.getInt("shiftID"),date,sType,rs.getInt("branchID")));
            }
            //end of processing
        } catch (SQLException e) {
            //e.printStackTrace();
        } catch (ParseException e){
            //e.printStackTrace();
        }
        //finally, return result
        return shifts;
    }

    private PreparedStatement createPreparedStatementSelectAll(Connection con) throws SQLException {
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }


    public boolean delete(int shiftID) {
        try (Connection con = connect();
             PreparedStatement ps = createPreparedStatementDelete(con, shiftID);) {

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement createPreparedStatementDelete(Connection con, int shiftID) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE shiftID = ?", TABLE_NAME);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, shiftID);
        return ps;
    }
}
