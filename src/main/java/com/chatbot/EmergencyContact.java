package com.chatbot;

import java.sql.*;

public class EmergencyContact {

    private static final String DB_URL = DBManagerConfig.getDbUrl();
    private static final String DB_USER = DBManagerConfig.getDbUser();
    private static final String DB_PASSWORD = DBManagerConfig.getDbPassword();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static String getHelplinesByRegion(String region) {
        StringBuilder result = new StringBuilder();
        String query;

        if (region.equalsIgnoreCase("general")) {
            query = "SELECT name, phone_number FROM helplines WHERE LOWER(region) = 'general' OR region IS NULL LIMIT 9";
        } else {
            query = "SELECT name, phone_number FROM helplines WHERE LOWER(region) = LOWER(?) LIMIT 9";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!region.equalsIgnoreCase("general")) {
                stmt.setString(1, region);
            }

            ResultSet rs = stmt.executeQuery();
            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                result.append(rs.getString("name"))
                        .append(" - ")
                        .append(rs.getString("phone_number"))
                        .append("\n");
            }

            if (!hasResults && !region.equalsIgnoreCase("general")) {
                return getHelplinesByRegion("general");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return result.length() > 0 ? result.toString() : "No helpline data found for " + region;
    }

    public static String getAllHelplines() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT region, name, phone_number FROM helplines ORDER BY region LIMIT 15";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("[")
                        .append(rs.getString("region"))
                        .append("] ")
                        .append(rs.getString("name"))
                        .append(" - ")
                        .append(rs.getString("phone_number"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return result.toString();
    }

    public static String getHelplineByName(String serviceName) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT region, name, phone_number FROM helplines WHERE LOWER(name) LIKE LOWER(?) LIMIT 5";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + serviceName + "%");

            ResultSet rs = stmt.executeQuery();
            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                result.append("[")
                        .append(rs.getString("region"))
                        .append("] ")
                        .append(rs.getString("name"))
                        .append(" - ")
                        .append(rs.getString("phone_number"))
                        .append("\n");
            }

            if (!hasResults) {
                return "No helpline found for service name: " + serviceName;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return result.toString();
    }

}
