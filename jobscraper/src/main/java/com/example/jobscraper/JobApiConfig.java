package com.example.jobscraper;

public class JobApiConfig{
    private String name;
    private String baseUrl;
    private String appId;
    private String appKey;
    private int resultsPerPage;

    public JobApiConfig() {
    }

    public JobApiConfig(String name, String baseUrl, String appId, String appKey, int resultsPerPage) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.appKey = appKey;
        this.resultsPerPage = resultsPerPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}