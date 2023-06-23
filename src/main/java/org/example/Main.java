package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonProperty;

class ResponseList {
    public List<Response> data;

}

abstract class Response {
}

class ConsultationInfo extends Response {
    @JsonProperty("全国地方公共団体コード")
    private String code;
    @JsonProperty("都道府県名")
    private String prefectureName;
    @JsonProperty("曜日")
    private String dayOfWeek;
    @JsonProperty("受付_年月日")
    private String date;
    @JsonProperty("相談件数")
    private int numberOfConsultations;

    public String getPrefectureName() {
        return prefectureName;
    }

    public String getDate(){
        return date;
    }

    public int getNumberOfConsultations(){
        return numberOfConsultations;
    }
}

class AdditionalInfo extends Response {
    @JsonProperty("moreResults")
    private String moreResults;
    @JsonProperty("endCursor")
    private String endCursor;
    @JsonProperty("revision")
    private int revision;
    @JsonProperty("updated")
    private String updated;

    // getters and setters
}


public class Main {
    public static void main(String[] args) {
        try {
            String url = "https://api.data.metro.tokyo.lg.jp/v1/Covid19CallCenter?limit=100";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("User-Agent", "Java 11 HttpClient Bot")
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            String jsonStr = response.body();


            ObjectMapper mapper = new ObjectMapper();
            ResponseList list = mapper.readValue(jsonStr, ResponseList.class);
            for(Response i : list.data){
                if (i instanceof ConsultationInfo) {
                    var info = (ConsultationInfo) i;
                    System.out.println(info.getPrefectureName() + ", " + info.getDate() + ": " + info.getNumberOfConsultations());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}