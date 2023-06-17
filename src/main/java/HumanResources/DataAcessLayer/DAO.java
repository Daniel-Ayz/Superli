package HumanResources.DataAcessLayer;

import java.sql.*;

public abstract class DAO {
    protected String tableName;
    private static final String DB_URL = "jdbc:sqlite:hr_database.sqlite";

    public DAO(String tableName){
        this.tableName = tableName;
        createTables(connect());
    }
        /**
         * Connect to a sample database
         */
    protected Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            String sql = "PRAGMA foreign_keys = ON";
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void deleteAll() {
        String sql = "DELETE FROM " + tableName;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void createTables(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS \"example\" (\n" +
                "\t\"Id\"\tINTEGER,\n" +
                "\t\"Name\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"Id\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Branch\" (\n" +
                "\t\"branchId\"\tINTEGER,\n" +
                "\t\"morningStartTime\"\tTEXT,\n" +
                "\t\"morningEndTime\"\tTEXT,\n" +
                "\t\"nightStartTime\"\tTEXT,\n" +
                "\t\"nightEndTime\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"branchId\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Employee\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"personalInfo\"\tTEXT,\n" +
                "\t\"baseSalary\"\tNUMERIC,\n" +
                "\t\"password\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"employeeId\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShiftId\" (\n" +
                "\t\"shiftId\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"shiftId\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"BankInformation\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"bankName\"\tTEXT,\n" +
                "\t\"bankAccountNumber\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"employeeId\"),\n" +
                "\tFOREIGN KEY(\"employeeId\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Driver\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"driverLicense\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"employeeId\"),\n" +
                "\tFOREIGN KEY(\"employeeId\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"EmployeeRoles\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"role\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"employeeId\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"employeeId\",\"role\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"EmploymentContract\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"startDate\"\tTEXT,\n" +
                "\t\"termsAndConditions\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"employeeId\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"employeeId\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Salary\" (\n" +
                "\t\"employeeId\"\tINTEGER,\n" +
                "\t\"date\"\tTEXT,\n" +
                "\t\"salary\"\tNUMERIC,\n" +
                "\t\"bonus\"\tNUMERIC,\n" +
                "\tFOREIGN KEY(\"employeeId\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"employeeId\",\"date\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"Shift\" (\n" +
                "\t\"shiftDate\"\tTEXT,\n" +
                "\t\"shiftType\"\tTEXT,\n" +
                "\t\"shiftID\"\tINTEGER,\n" +
                "\t\"branchID\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"branchID\") REFERENCES \"Branch\"(\"branchId\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"shiftID\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShiftApproved\" (\n" +
                "\t\"shiftID\"\tINTEGER,\n" +
                "\t\"employeeID\"\tINTEGER,\n" +
                "\t\"role\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"shiftID\") REFERENCES \"Shift\"(\"shiftID\") ON DELETE CASCADE\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShiftBlocked\" (\n" +
                "\t\"employeeID\"\tINTEGER,\n" +
                "\t\"shiftID\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"shiftID\") REFERENCES \"Shift\"(\"shiftID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"employeeID\",\"shiftID\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShiftRequested\" (\n" +
                "\t\"shiftID\"\tINTEGER,\n" +
                "\t\"employeeID\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"Employee\"(\"employeeId\") ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"shiftID\") REFERENCES \"Shift\"(\"shiftID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"shiftID\",\"employeeID\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS \"ShiftRequired\" (\n" +
                "\t\"shiftID\"\tINTEGER,\n" +
                "\t\"Role\"\tTEXT,\n" +
                "\t\"amount\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"shiftID\") REFERENCES \"Shift\"(\"shiftID\") ON DELETE CASCADE,\n" +
                "\tPRIMARY KEY(\"Role\",\"shiftID\")\n" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
