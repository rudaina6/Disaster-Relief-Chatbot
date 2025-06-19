package com.chatbot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private static final String DB_URL = DBManagerConfig.getDbUrl();
    private static final String DB_USER = DBManagerConfig.getDbUser();
    private static final String DB_PASSWORD = DBManagerConfig.getDbPassword();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    public static String getDroughts() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM droughts LIMIT 18";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching droughts.";
        }
        return result.toString();
    }

    public static String getEarthquakes() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM earthquakes LIMIT 18";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | Magnitude: ").append(rs.getFloat("magnitude")).append(" ").append(rs.getString("magnitude_scale"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getFloat("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching earthquakes.";
        }
        return result.toString();
    }

    public static String getEpidemics() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM epidemic LIMIT 18";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("🦠 ").append(rs.getString("event_name"))
                        .append(" (Cause: ").append(rs.getString("epidemic_cause")).append(") in ")
                        .append(rs.getString("location"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching epidemics.";
        }
        return result.toString();
    }

    public static String getFloods() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM floods LIMIT 18";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching floods.";
        }
        return result.toString();
    }
    public static String getDroughts(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM droughts WHERE LOWER(location) LIKE LOWER(?) LIMIT 18";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching droughts.";
        }
        return result.length() > 0 ? result.toString() : "No drought data found for " + location;
    }

    public static String getEarthquakes(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM earthquakes WHERE LOWER(location) LIKE LOWER(?) LIMIT 18";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | Magnitude: ").append(rs.getFloat("magnitude")).append(" ").append(rs.getString("magnitude_scale"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getFloat("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching earthquakes.";
        }
        return result.length() > 0 ? result.toString() : "No earthquake data found for " + location;
    }

    public static String getEpidemics(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM epidemic WHERE LOWER(location) LIKE LOWER(?) LIMIT 18";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🦠 ").append(rs.getString("event_name"))
                        .append(" (Cause: ").append(rs.getString("epidemic_cause")).append(") in ")
                        .append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching epidemics.";
        }
        return result.length() > 0 ? result.toString() : "No epidemic data found for " + location;
    }

    public static String getFloods(String location) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM floods WHERE LOWER(location) LIKE LOWER(?) LIMIT 18";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching floods.";
        }
        return result.length() > 0 ? result.toString() : "No flood data found for " + location;
    }
    public static String getAllDisasters(String location) {
        StringBuilder result = new StringBuilder();

        result.append("🌵 DROUGHTS:\n").append(getDroughts(location)).append("\n");
        result.append("🌊 FLOODS:\n").append(getFloods(location)).append("\n");
        result.append("🌍 EARTHQUAKES:\n").append(getEarthquakes(location)).append("\n");
        result.append("🦠 EPIDEMICS:\n").append(getEpidemics(location)).append("\n");

        return result.toString();
    }
    public static String getFloods(String location, String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM floods WHERE start_date >= ? AND LOWER(location) LIKE LOWER(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🌊 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching floods.";
        }
        return result.length() > 0 ? result.toString() : "No flood data found for the given filters.";
    }

    public static String getEpidemics(String location, String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM epidemic WHERE start_date >= ? AND LOWER(location) LIKE LOWER(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🦠 ").append(rs.getString("event_name"))
                        .append(" (Cause: ").append(rs.getString("epidemic_cause")).append(") in ")
                        .append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching epidemics.";
        }
        return result.length() > 0 ? result.toString() : "No epidemic data found for the given filters.";
    }

    public static String getEarthquakes(String location, String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM earthquakes WHERE start_date >= ? AND LOWER(location) LIKE LOWER(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | Magnitude: ").append(rs.getFloat("magnitude")).append(" ").append(rs.getString("magnitude_scale"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getFloat("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching earthquakes.";
        }
        return result.length() > 0 ? result.toString() : "No earthquake data found for the given filters.";
    }

    public static String getDroughts(String location, String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM droughts WHERE start_date >= ? AND LOWER(location) LIKE LOWER(?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🌾 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching droughts.";
        }
        return result.length() > 0 ? result.toString() : "No drought data found for the given filters.";
    }
    public static String getDroughtsByDate(String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM droughts WHERE start_date >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🌾 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching droughts.";
        }
        return result.length() > 0 ? result.toString() : "No drought data found after the given date.";
    }
    public static String getFloodsByDate(String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM floods WHERE start_date >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🌊 ").append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching floods.";
        }
        return result.length() > 0 ? result.toString() : "No flood data found after the given date.";
    }
    public static String getEarthquakesByDate(String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM earthquakes WHERE start_date >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("📍 ").append(rs.getString("location"))
                        .append(" | Magnitude: ").append(rs.getFloat("magnitude")).append(" ").append(rs.getString("magnitude_scale"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getFloat("total_affected"))
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching earthquakes.";
        }
        return result.length() > 0 ? result.toString() : "No earthquake data found after the given date.";
    }
    public static String getEpidemicsByDate(String startDate) {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM epidemic WHERE start_date >= ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append("🦠 ").append(rs.getString("event_name"))
                        .append(" (Cause: ").append(rs.getString("epidemic_cause")).append(") in ")
                        .append(rs.getString("location"))
                        .append(" | From: ").append(rs.getDate("start_date"))
                        .append(" to ").append(rs.getDate("end_date"))
                        .append(" | Deaths: ").append(rs.getInt("total_deaths"))
                        .append(" | Affected: ").append(rs.getInt("total_affected"))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error while fetching epidemics.";
        }
        return result.length() > 0 ? result.toString() : "No epidemic data found after the given date.";
    }
}