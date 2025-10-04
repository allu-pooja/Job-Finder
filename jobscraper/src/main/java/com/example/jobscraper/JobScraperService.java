package com.example.jobscraper;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Component
public class JobScraperService {

    List<JobApiConfig> apiList=new ArrayList<>();

    @Value("${adzuna.app_id}")
    private String app_id; 
   
    @Value("${adzuna.app_key}")
    private String app_key; 

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        restTemplate=new RestTemplate();
        objectMapper=new ObjectMapper();
        System.out.println("DEBUG: app_id = " + app_id);
        System.out.println("DEBUG: app_key = " + app_key);

        //intialize api
        apiList.add(new JobApiConfig("Adzuna","https://api.adzuna.com/v1/api/jobs/in/search/",app_id,app_key,10));
    }


    public List<Job> fetchJobsFromAdzuna(JobApiConfig apiConfig,String skill,int page) {
        List<Job> jobs = new ArrayList<>();
        
        try {
            String url = UriComponentsBuilder.fromUriString(apiConfig.getBaseUrl() +page)
                        .queryParam("app_id", apiConfig.getAppId())
                        .queryParam("app_key", apiConfig.getAppKey())
                        .queryParam("results_per_page", apiConfig.getResultsPerPage())
                        .queryParam("what", skill)
                        .build(true)
                        .toUriString();

            //httpclient
           /* HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            // json parsing
            JSONObject obj = new JSONObject(response.body());
            JSONArray results = obj.getJSONArray("results");
            */

            // restTemplate
            ResponseEntity<String> response=restTemplate.getForEntity(url, String.class);

            //JSON parsing using jackson

            JsonNode root=objectMapper.readTree(response.getBody());
            int count=root.path("count").asInt();
            System.out.println("Total number of jobs: "+count);
            JsonNode results=root.path("results");
            
            for (JsonNode node:results) {
                Job job=new Job();

                job.setTitle(node.path("title").asText());

                job.setCompany(node.path("company").path("display_name").asText("Not specified"));

                job.setLocation(node.path("location").path("display_name").asText("Not specified"));

                if(node.has("salary_min") && node.has("salary_max")){
                    job.setSalary(node.path("salary_min").asText() +"-" + node.path("salary_max").asText());
                }
                else{
                    job.setSalary("Not specified");
                }

                job.setUrl(node.path("redirect_url").asText("Not specified"));

                job.setLogoUrl(node.path("company").path("logo_url").asText(null));
                
                jobs.add(job);
            }

        } catch (Exception e) {
            System.err.println("Failed to fetch jobs: "+e.getMessage());
        }
        return jobs;
    }

    public List<Job> fetchAllJobs(String skill,int page){
        List<Job> allJobs=new ArrayList<>();
        for(JobApiConfig api:apiList){
            allJobs.addAll(fetchJobsFromAdzuna(api, skill, page));
        }
        return allJobs;
    }
}
