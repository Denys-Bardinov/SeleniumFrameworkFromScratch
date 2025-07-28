package com.orangehrm.utilities;

import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    public static final Logger logger = LoggerManager.getLogger(DBConnection.class);


    private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getDBConnection() {
        try {
            logger.info("Starting DB connection");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("DB connection successful");
            return conn;
        } catch (SQLException e) {
            logger.error("Error while establishing DB connection");
            e.printStackTrace();
            return null;
        }
    }


    // Get the employee details in DB and store in Map
    public static Map<String, String> getEmployeeDetails(String employee_id) {
        String query = "Select emp_firstname, emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id = " + employee_id;

        Map<String, String> employeeDetails = new HashMap<>();

        try (Connection conn = getDBConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            logger.info("Executing query: " + query);
            if (rs.next()) {
                String firstName = rs.getString("emp_firstname");
                String middleName = rs.getString("emp_middle_name");
                String lastName = rs.getString("emp_lastname");

                // Store in a map
                employeeDetails.put("firstName", firstName);
                employeeDetails.put("middleName", middleName != null ? middleName:"");
                employeeDetails.put("lastName", lastName);
                logger.info("Query executed successfuly");
                logger.info("Employee Data fetched: " + employeeDetails);
            } else {
                logger.error("Employee not found");
            }
        } catch (Exception e) {
            logger.error("Error while executing query");
            e.printStackTrace();
        }
        return employeeDetails;
    }
}
