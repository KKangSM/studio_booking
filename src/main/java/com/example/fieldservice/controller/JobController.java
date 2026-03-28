package com.example.fieldservice.controller;

import com.example.fieldservice.domain.Job;
import com.example.fieldservice.service.EstimateService;
import com.example.fieldservice.service.JobService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobController {

    private final JobService jobService;
    private final EstimateService estimateService;

    public JobController(JobService jobService, EstimateService estimateService) {
        this.jobService = jobService;
        this.estimateService = estimateService;
    }

    @GetMapping("/jobs")
    public String list(@RequestParam(defaultValue = "") String keyword, Model model) {
        model.addAttribute("jobs", jobService.search(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("jobForm", new Job());
        return "jobs/list";
    }

    @PostMapping("/jobs")
    public String create(@Valid @ModelAttribute("jobForm") Job job, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("jobs", jobService.findAll());
            model.addAttribute("keyword", "");
            return "jobs/list";
        }
        jobService.save(job);
        return "redirect:/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        model.addAttribute("estimates", estimateService.findByJobId(id));
        return "jobs/detail";
    }
}
