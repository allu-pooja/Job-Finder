package com.example.jobscraper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class JobScraperService {
    @Value("${adzuna.app_id}")
    private String app_id; 
   
    @Value("${adzuna.app_key}")
    private String app_key; 

    @PostConstruct
    public void init() {
        System.out.println("DEBUG: app_id = " + app_id);
        System.out.println("DEBUG: app_key = " + app_key);
    }
    public List<Job> fetchJobsFromAdzuna(String skill,int page) {
        List<Job> jobs = new ArrayList<>();
        
        try {
            String url = "https://api.adzuna.com/v1/api/jobs/in/search/" +page+
                    "?app_id=" + app_id+
                    "&app_key=" + app_key +
                    "&results_per_page=10" +
                    "&what=" + skill;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject obj = new JSONObject(response.body());
            JSONArray results = obj.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject job = results.getJSONObject(i);
                String title = job.getString("title");
                String company = job.getJSONObject("company").getString("display_name");
                String location = job.getJSONObject("location").getString("display_name");

                double salaryMin=job.optDouble("salary_min",0);
                double salaryMax=job.optDouble("salary_max",0);
                String salary=(salaryMin > 0 || salaryMax>0) ? salaryMin+" - "+salaryMax :"Not specified";
                String j_url=job.getString("redirect_url");
                JSONObject companylogo=job.optJSONObject("company");
                String logoUrl=(companylogo!=null) ? companylogo.optString("logo_url","") :"";
                jobs.add(new Job(title,company,location,salary,j_url,logoUrl));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;
    }
}
