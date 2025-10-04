package com.example.jobscraper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobController {

    private final JobScraperService service;

    public JobController(JobScraperService service){
        this.service=service;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/fetch")
    public ModelAndView fetchJobs(
            @RequestParam String name,
            @RequestParam String skills,
            @RequestParam(defaultValue = "1") int page) {

        List<Job> jobs = service.fetchAllJobs(skills,page);

        ModelAndView mv = new ModelAndView("target");
        mv.addObject("name", name);
        mv.addObject("skills", skills);
        mv.addObject("jobs", jobs);
         mv.addObject("page", page);

        return mv;
    }
}
