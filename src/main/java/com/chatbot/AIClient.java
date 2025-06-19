package com.chatbot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AIClient {

    public static String sendPromptToOpenAI(String prompt) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4-turbo");

        JSONArray messages = new JSONArray();

        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content",
                        "You are an assistant with access to a database of emergency helplines, hospitals, shelters, and past disasters in Pakistan. " +
                        "Respond in machine-readable format. Use these rules:\n" +
                        "- For helplines by multiple different regions: respond with 'DB_QUERY:<region1>,<region2>,...' and if the user asks you for Pakistan or 'in the country', use the term 'general' as region\n" +
                        "- You are provided with a fixed list of official helpline services.\n" +
                        "- If the user asks for a helpline (e.g., \"ambulance helpline\", \"police emergency\", \"rescue help\"), match the request to the **most relevant service name** from the official list.\n" +
                        "- Match even if the user input is vague, partial, or uses a different phrasing (e.g., \"police helpline\" → \"Police Emergency\", or \"rangers help\" → \"Rangers Madadgar Helpline\").\n" +
                        "- Respond in the following format ONLY:\n" +
                        "  DB_QUERY_NAME:<best_matched_service_name>\n" +
                        "\n" +
                        "Official list of helpline names:\n" +
                        "- SDMA AJ&K\n" +
                        "- Rescue Service\n" +
                        "- Rangers Madadgar Helpline\n" +
                        "- Provincial Disaster Management Authority (PDMA) Punjab\n" +
                        "- Police Emergency\n" +
                        "- PDMA Punjab\n" +
                        "- PDMA KPK\n" +
                        "- Pakistan Red Crescent Society (PRCS)\n" +
                        "- Pakistan Medical Assistance Helpline\n" +
                        "- Pakistan Emergency Helpline (PEHEL)\n" +
                        "- National Disaster Management Authority (NDMA)\n" +
                        "- GBDMA\n" +
                        "- Fire Brigade\n" +
                        "- FDMA\n" +
                        "- Emergency Cell – Overseas Pakistanis Foundation (OPF)\n" +
                        "- Eidhi Helpline\n" +
                        "- Edhi Ambulance Service\n" +
                        "- Chhipa Ambulance Service\n" +
                        "- Ambulance Services\n" +
                        "- Aman Foundation Helpline\n" +
                        "- Al-Khidmat Foundation\n" +
                        "- If you can't find a specific region or the user asks for general helplines, use 'DB_QUERY:general'\n" +
                        "- For all helplines: 'DB_QUERY_ALL'\n" +
                        "- For shelters: 'DB_QUERY_SHELTERS:<location1>,<location2>,...'\n" +
                        "- For hospitals: 'DB_QUERY_HOSPITALS:<location1>,<location2>,...'\n" +
                        "- For both: 'DB_QUERY_HOSPITALS:<loc1>,<loc2>,... | DB_QUERY_SHELTERS:<loc3>,<loc4>,....'\n" +
                        "- For all hospitals if or if you cant find the location: 'DB_QUERY_ALL_HOSPITALS'\n" +
                        "- For all shelters or if you cant find the location: 'DB_QUERY_ALL_SHELTERS'\n" +
                        "- For all three (helplines, shelters, hospitals): 'DB_QUERY:<region1>,<region2>,... | DB_QUERY_SHELTERS:<location1>,<location2>,... | DB_QUERY_HOSPITALS:<location1>,<location2>,...'\n" +
                        "- For general disaster history: 'DB_QUERY_DISASTERS:ALL'\n" +
                        "- For droughts: 'DB_QUERY_DISASTERS:DROUGHTS'\n" +
                        "- For earthquakes: 'DB_QUERY_DISASTERS:EARTHQUAKES'\n" +
                        "- For epidemics: 'DB_QUERY_DISASTERS:EPIDEMICS'\n" +
                        "- For floods: 'DB_QUERY_DISASTERS:FLOODS'\n" +
                        "- For droughts in multiple locations: 'DB_QUERY_DISASTERS:DROUGHTS:<location1>,<location2>,...'\n" +
                        "- For floods in multiple locations: 'DB_QUERY_DISASTERS:FLOODS:<location1>,<location2>,...'\n" +
                        "- For earthquakes in multiple locations: 'DB_QUERY_DISASTERS:EARTHQUAKES:<location1>,<location2>,...'\n" +
                        "- For epidemics in multiple locations: 'DB_QUERY_DISASTERS:EPIDEMICS:<location1>,<location2>,...'\n" +
                        "- To get all types of past disasters in a location: 'DB_QUERY_ALL_DISASTERS:<location1>,<location2>,...'\n" +
                        "- For different disasters in different locations: 'DB_QUERY_MIXED:<disaster1>=<location1>,<disaster2>=<location2>,...'\n" +
                        "  Example: 'DB_QUERY_MIXED:flood=Sindh,earthquake=Balochistan'\n" +
                        "- For a specific disaster in a location after a certain date (e.g., 'floods in Sindh after 2010'), use: 'DB_QUERY_MIXED_DATED:<disaster>=<location>=<date>'\n" +
                        "  Example: 'DB_QUERY_MIXED_DATED:flood=Sindh=2010-01-01'\n" +
                        "- For multiple such queries, separate with commas:\n" +
                        "  Example: 'DB_QUERY_MIXED_DATED:flood=Sindh=2010-01-01,earthquake=Balochistan=2005-10-08'\n" +
                        "- For disaster data filtered only by date (no specific location): 'DB_QUERY_DATED:<disaster1>=<yyyy1>,<disaster2>=<yyyy2>,...'\n" +
                        "  Example: 'DB_QUERY_DATED:floods=2020,epidemics=2021'\n" +
                        "- When users mention dates in natural language (e.g., 'after 2010', 'since 2022', 'in 2005'), convert them to the format 'yyyy-mm-dd' using the first day of the year or month.\n" +
                        "- If the user is distressed, reassure them and mention you can provide helplines, hospitals, or shelters.\n" +
                        "- For unrelated queries, respond with a generic message like you can only help with disasters and the stats regarding different disasters.\n" +
                        "- If a user asks for helplines in a location that might not exist in the database, use 'DB_QUERY:general' to provide general helplines instead of returning no results."
        );

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        messages.put(systemMessage);
        messages.put(userMessage);

        requestBody.put("messages", messages);

        URL url = new URL(DBManagerConfig.getApiUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + DBManagerConfig.getApiKey());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes());
            os.flush();
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        JSONObject json = new JSONObject(response.toString());
        JSONArray choices = json.getJSONArray("choices");
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        return message.getString("content");
    }
}
