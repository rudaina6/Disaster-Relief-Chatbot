package com.chatbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatbotService {

    private final EmergencyContact ec = new EmergencyContact();
    private final Shelters s = new Shelters();
    private final Statistics st = new Statistics();
    private final AIClient aiClient = new AIClient();

    public String processUserInput(String userPrompt) {
        StringBuilder output = new StringBuilder();
        try {
            String aiResponse = aiClient.sendPromptToOpenAI(userPrompt);

            if (aiResponse.contains("DB_QUERY_HOSPITALS:") || aiResponse.contains("DB_QUERY_SHELTERS:") || aiResponse.contains("DB_QUERY:") || aiResponse.contains("DB_QUERY_NAME:")){

                // ✅ Hospitals
                if (aiResponse.contains("DB_QUERY_HOSPITALS:")) {
                    String hospitalsPart = aiResponse.split("DB_QUERY_HOSPITALS:")[1].split("\\|")[0].trim();
                    String[] hospitalLocations = hospitalsPart.split(",");
                    for (String loc : hospitalLocations) {
                        String hospitalData = s.getHospitalsByDistrictOrProvince(loc.trim());
                        output.append("\uD83C\uDFE5 Hospitals in ").append(loc.trim()).append(":\n").append(hospitalData).append("\n");
                    }
                }

                // ✅ Shelters
                if (aiResponse.contains("DB_QUERY_SHELTERS:")) {
                    String sheltersPart = aiResponse.split("DB_QUERY_SHELTERS:")[1].split("\\|")[0].trim();
                    String[] shelterLocations = sheltersPart.split(",");
                    for (String loc : shelterLocations) {
                        String shelterData = s.getSheltersByCityOrProvince(loc.trim());
                        output.append("\uD83C\uDFE0 Shelters in ").append(loc.trim()).append(":\n").append(shelterData).append("\n");
                    }
                }

                // ✅ Helplines
                if (aiResponse.contains("DB_QUERY:")) {
                    String helplinePart = aiResponse.split("DB_QUERY:")[1].split("\\|")[0].trim();
                    String[] helplineRegions = helplinePart.split(",");
                    for (String region : helplineRegions) {
                        String helplineData = ec.getHelplinesByRegion(region.trim());
                        output.append("\uD83D\uDCDE Helplines in ").append(region.trim()).append(":\n").append(helplineData).append("\n");
                    }
                }
                if (aiResponse.contains("DB_QUERY_NAME:")) {
                    String helplineName = aiResponse.split(":")[1].trim();
                    String helplineData = ec.getHelplineByName(helplineName);
                    output.append("📞 Helpline Info for ").append(helplineName).append(":\n").append(helplineData).append("\n");
                }
            }
            else if (aiResponse.equals("DB_QUERY_ALL")) {
                output.append("\uD83D\uDCDE All Helpline Info:\n").append(ec.getAllHelplines());
            } else if (aiResponse.equals("DB_QUERY_ALL_HOSPITALS")) {
                output.append("\uD83C\uDFE5 All Hospitals:\n").append(s.getAllHospitals());
            } else if (aiResponse.equals("DB_QUERY_ALL_SHELTERS")) {
                output.append("\uD83C\uDFE0 All Shelters:\n").append(s.getAllShelters());
            } else if (aiResponse.startsWith("DB_QUERY:")) {
                String regionsPart = aiResponse.split(":")[1].trim();
                String[] regions = regionsPart.split(",");

                for (String region : regions) {
                    region = region.trim();
                    output.append("\uD83D\uDCDE Helpline Info for ").append(region).append(":\n").append(ec.getHelplinesByRegion(region)).append("\n");
                }
            } else if (aiResponse.equals("DB_QUERY_DISASTERS:ALL")) {
                output.append("\uD83C\uDF0D Previous Disaster Data:\n");
                output.append("\uD83E\uDDA0 Epidemics:\n").append(st.getEpidemics()).append("\n");
                output.append("\uD83C\uDF0D Earthquakes:\n").append(st.getEarthquakes()).append("\n");
                output.append("\uD83C\uDF35 Droughts:\n").append(st.getDroughts()).append("\n");
                output.append("\uD83C\uDF0A Floods:\n").append(st.getFloods()).append("\n");
            }
            else if (aiResponse.startsWith("DB_QUERY_MIXED_DATED:")) {
                String input = aiResponse.substring("DB_QUERY_MIXED_DATED:".length()).trim();
                String[] queries = input.split(",");

                output.append("📊 Combined Disaster Data with Dates:\n");

                for (String q : queries) {
                    String[] parts = q.split("=");
                    if (parts.length != 3) continue;

                    String type = parts[0].trim().toLowerCase();
                    String location = parts[1].trim();
                    String dateStr = parts[2].trim();

                    String startDate = null;
                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(dateStr);
                    if (matcher.find()) {
                        startDate = matcher.group(1) + "-01-01";
                    }

                    switch (type) {
                        case "flood":
                        case "floods":
                            output.append("🌊 Floods in ").append(location).append(" since ").append(startDate).append(":\n")
                                    .append(st.getFloods(location, startDate)).append("\n");
                            break;
                        case "earthquake":
                        case "earthquakes":
                            output.append("🌍 Earthquakes in ").append(location).append(" since ").append(startDate).append(":\n")
                                    .append(st.getEarthquakes(location, startDate)).append("\n");
                            break;
                        case "drought":
                        case "droughts":
                            output.append("🌵 Droughts in ").append(location).append(" since ").append(startDate).append(":\n")
                                    .append(st.getDroughts(location, startDate)).append("\n");
                            break;
                        case "epidemic":
                        case "epidemics":
                            output.append("🦠 Epidemics in ").append(location).append(" since ").append(startDate).append(":\n")
                                    .append(st.getEpidemics(location, startDate)).append("\n");
                            break;
                        default:
                            output.append("⚠️ Unknown disaster type: ").append(type).append("\n");
                    }
                }
            }
            else if (aiResponse.startsWith("DB_QUERY_ALL_DISASTERS:")) {
                String location = aiResponse.split(":")[1].trim();
                output.append("\uD83D\uDCCB All Disasters in ").append(location).append(":\n").append(st.getAllDisasters(location));
            }
            else if (aiResponse.startsWith("DB_QUERY_DISASTERS:")) {
                String[] parts = aiResponse.split(":");
                String disasterType = parts[1].trim().toUpperCase();
                String location = parts.length > 2 ? parts[2].trim() : null;

                switch (disasterType) {
                    case "DROUGHTS":
                        output.append("\uD83C\uDF35 Drought Data:\n").append(location != null ? st.getDroughts(location) : st.getDroughts());
                        break;
                    case "EARTHQUAKES":
                        output.append("\uD83C\uDF0D Earthquake Data:\n").append(location != null ? st.getEarthquakes(location) : st.getEarthquakes());
                        break;
                    case "EPIDEMICS":
                        output.append("\uD83E\uDDA0 Epidemic Data:\n").append(location != null ? st.getEpidemics(location) : st.getEpidemics());
                        break;
                    case "FLOODS":
                        output.append("\uD83C\uDF0A Flood Data:\n").append(location != null ? st.getFloods(location) : st.getFloods());
                        break;
                    default:
                        output.append("\u274C Unknown disaster type.");
                }
            }
            else if (aiResponse.startsWith("DB_QUERY_DATED:")) {
                String input = aiResponse.substring("DB_QUERY_DATED:".length()).trim();
                String[] queries = input.split(",");

                output.append("📅 Disaster Data after Specific Dates:\n");

                for (String q : queries) {
                    String[] parts = q.split("=");
                    if (parts.length != 2) continue;

                    String type = parts[0].trim().toLowerCase();
                    String dateStr = parts[1].trim();

                    String startDate = null;
                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(dateStr);
                    if (matcher.find()) {
                        startDate = matcher.group(1) + "-01-01";
                    }

                    if (startDate == null) continue;

                    switch (type) {
                        case "flood":
                        case "floods":
                            output.append("🌊 Floods since ").append(startDate).append(":\n")
                                    .append(st.getFloodsByDate(startDate)).append("\n");
                            break;
                        case "earthquake":
                        case "earthquakes":
                            output.append("🌍 Earthquakes since ").append(startDate).append(":\n")
                                    .append(st.getEarthquakesByDate(startDate)).append("\n");
                            break;
                        case "drought":
                        case "droughts":
                            output.append("🌵 Droughts since ").append(startDate).append(":\n")
                                    .append(st.getDroughtsByDate(startDate)).append("\n");
                            break;
                        case "epidemic":
                        case "epidemics":
                            output.append("🦠 Epidemics since ").append(startDate).append(":\n")
                                    .append(st.getEpidemicsByDate(startDate)).append("\n");
                            break;
                        default:
                            output.append("⚠️ Unknown disaster type: ").append(type).append("\n");
                    }
                }
            }
            else if (aiResponse.startsWith("DB_QUERY_MIXED:")) {
                String input = aiResponse.substring("DB_QUERY_MIXED:".length()).trim();
                String[] queries = input.split(",");

                output.append("\uD83D\uDCCA Combined Disaster Data:\n");

                for (String q : queries) {
                    String[] parts = q.split("=");
                    if (parts.length != 2) continue;

                    String type = parts[0].trim().toLowerCase();
                    String location = parts[1].trim();

                    switch (type) {
                        case "flood":
                        case "floods":
                            output.append("\uD83C\uDF0A Floods in ").append(location).append(":\n").append(st.getFloods(location)).append("\n");
                            break;
                        case "earthquake":
                        case "earthquakes":
                            output.append("\uD83C\uDF0D Earthquakes in ").append(location).append(":\n").append(st.getEarthquakes(location)).append("\n");
                            break;
                        case "drought":
                        case "droughts":
                            output.append("\uD83C\uDF35 Droughts in ").append(location).append(":\n").append(st.getDroughts(location)).append("\n");
                            break;
                        case "epidemic":
                        case "epidemics":
                            output.append("\uD83E\uDDA0 Epidemics in ").append(location).append(":\n").append(st.getEpidemics(location)).append("\n");
                            break;
                        default:
                            output.append("\u26A0\uFE0F Unknown disaster type: ").append(type).append("\n");
                    }
                }
            }
            else {
                output.append("").append(aiResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            output.append("Sorry, an error occurred while processing your request.");
        }

        return output.toString();
    }

}
