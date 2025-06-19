package com.chatbot;

import java.sql.*;

public class Shelters {

    private static final String DB_URL = DBManagerConfig.getDbUrl();
    private static final String DB_USER = DBManagerConfig.getDbUser();
    private static final String DB_PASSWORD = DBManagerConfig.getDbPassword();


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static String getAllShelters() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT province, city, address_location, contact_number, type, capacity FROM shelters ORDER BY province, city LIMIT 9";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("[")
                        .append(rs.getString("province"))
                        .append(" - ")
                        .append(rs.getString("city"))
                        .append("] ")
                        .append(rs.getString("type"))
                        .append(" (Capacity: ").append(rs.getInt("capacity")).append(")\n")
                        .append("📍 ").append(rs.getString("address_location")).append("\n")
                        .append("☎️ ").append(rs.getString("contact_number")).append("\n\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching shelters.";
        }

        return result.length() > 0 ? result.toString() : "No shelter data found.";
    }

    public static String getAllHospitals() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT province, district, name, type, address, phone_number, occupancy FROM hospitals ORDER BY province, district LIMIT 9";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("[")
                        .append(rs.getString("province"))
                        .append(" - ")
                        .append(rs.getString("district"))
                        .append("] ")
                        .append(rs.getString("name"))
                        .append(" (").append(rs.getString("type")).append(")\n")
                        .append("📍 ").append(rs.getString("address")).append("\n")
                        .append("☎️ ").append(rs.getString("phone_number")).append("\n")
                        .append("🛏️ Occupancy: ").append(rs.getInt("occupancy")).append("\n\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching hospitals.";
        }

        return result.length() > 0 ? result.toString() : "No hospital data found.";
    }

    public static String getSheltersByCityOrProvince(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT city, address_location, contact_number, type, capacity FROM shelters " +
                "WHERE (LOWER(city) = LOWER(?) OR LOWER(province) = LOWER(?)) AND capacity > 0 LIMIT 10";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, location);
            stmt.setString(2, location);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("[")
                        .append(rs.getString("city"))
                        .append("] ")
                        .append(rs.getString("type"))
                        .append(" Shelter - ")
                        .append(rs.getString("address_location"))
                        .append(" | Contact: ")
                        .append(rs.getString("contact_number"))
                        .append(" | Capacity: ")
                        .append(rs.getInt("capacity"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return result.length() > 0 ? result.toString() : "No shelter data found for " + location;
    }

    public static String getHospitalsByDistrictOrProvince(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT name, type, address, phone_number, occupancy FROM hospitals " +
                "WHERE LOWER(district) = LOWER(?) OR LOWER(province) = LOWER(?) LIMIT 10";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, location);
            stmt.setString(2, location);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append(rs.getString("name"))
                        .append(" (")
                        .append(rs.getString("type"))
                        .append(") - ")
                        .append(rs.getString("address"))
                        .append(" | Contact: ")
                        .append(rs.getString("phone_number"))
                        .append(" | Occupancy: ")
                        .append(rs.getInt("occupancy"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return result.length() > 0 ? result.toString() : "No hospital data found for " + location;
    }
}
