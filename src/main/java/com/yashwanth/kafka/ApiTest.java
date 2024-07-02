package com.yashwanth.kafka;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ApiTest {
    public static void main(String[] args) {
        //System.out.println(bestInGenre("Action"));
        //System.out.println(bestInGenre("Animation"));
        System.out.println(getMedicalrecords("Dr Arnold Bullock", 2));
    }

    public static String bestInGenre(String genre) {
        String result = "";
        try {
            int page = 1, totalPages = 0;
            double maxRating = -1;
            do {
                URL url = new URL(String.format("https://jsonmock.hackerrank.com/api/tvseries?page=%s", page));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONParser parser = new JSONParser();
                JSONObject res = (JSONObject) parser.parse(response.toString());
                if (totalPages == 0) {
                    totalPages = (int) (long) res.get("total_pages");
                }
                page++;
                JSONArray data = (JSONArray) res.get("data");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject series = (JSONObject) data.get(i);
                    String name = (String) series.get("name");
                    String genres = (String) series.get("genre");
                    if (genres.contains(genre)) {
                        double rating = getDoubleValue(series.get("imdb_rating"));
                        if (maxRating < rating) {
                            maxRating = rating;
                            result = name;
                        } else if (maxRating == rating && result.compareTo(name) > 0) {
                            result = name;
                        }
                    }
                }
            } while (page < totalPages + 1);
        } catch (Exception e) {

            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    private static Double getDoubleValue(Object value) {
        if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unexpected number type");
        }
    }

    private static List<Integer> getMedicalrecords(String docName, Integer dId) {
        try {
            int page = 1, totalPages = 0;
            int maxTemp=Integer.MIN_VALUE,minTemp=Integer.MAX_VALUE;
            do {
                URL url = new URL(String.format("https://jsonmock.hackerrank.com/api/medical_records?page=%s", page));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();


                JSONParser parser = new JSONParser();
                JSONObject res = (JSONObject) parser.parse(response.toString());
                if (totalPages == 0) {
                    totalPages = (int) (long) res.get("total_pages");
                }
                page++;
                JSONArray data = (JSONArray) res.get("data");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject mr = (JSONObject) data.get(i);
                    JSONObject doctor = (JSONObject) mr.get("doctor");
                    JSONObject diagnosis = (JSONObject) mr.get("diagnosis");
                    JSONObject vitals = (JSONObject) mr.get("vitals");

                    String dName = (String) doctor.get("name");
                    int diagId = (int) (long) diagnosis.get("id");

                    if(dName.equals(docName) && dId==diagId){
                        int temp=getDoubleValue(vitals.get("bodyTemperature")).intValue();
                        if(minTemp>temp){
                            minTemp=temp;
                        }
                        if(maxTemp<temp){
                            maxTemp=temp;
                        }
                    }
                }
            } while (page < totalPages + 1);
            return Arrays.asList(minTemp,maxTemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
