package com.example.jobscraper;

public class Job {
    
    private String title;
    private String company;
    private String location;
    private String salary;
    private String url;
    private String logoUrl;

    public Job(String title,String company,String location,String salary,String url,String logoUrl){
        this.title=title;
        this.company=company;
        this.location=location;
        this.salary=salary;
        this.url=url;
        this.logoUrl=logoUrl;
    }
    public String getTitle(){
        return title;
    }

    public String getCompany(){
        return company;
    }
    public String getLocation(){
        return location;
    }
    public String getSalary(){
        return salary;
    }
    public String getUrl(){
        return url;
    }
    public String getLogoUrl(){
        return logoUrl;
    }
}
