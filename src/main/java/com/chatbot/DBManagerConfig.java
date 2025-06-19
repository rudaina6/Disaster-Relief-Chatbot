package com.chatbot;

import io.github.cdimascio.dotenv.Dotenv;

public class DBManagerConfig {

    static Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String API_URL = dotenv.get("API_URL");

    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }
}

