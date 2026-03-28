package com.example.fieldservice.controller;

import com.example.fieldservice.domain.Estimate;
import com.example.fieldservice.domain.Job;
import com.example.fieldservice.domain.ProgressStatus;
import com.example.fieldservice.service.EstimateService;
import com.example.fieldservice.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EstimateController {

    private final EstimateService estimateService;
    private final JobService jobService;

    public EstimateController(EstimateService estimateService, JobService jobService) {
        this.estimateService = estimateService;
        this.jobService = jobService;
    }

    @GetMapping("/estimates")
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) ProgressStatus status,
                       Model model) {
        model.addAttribute("estimates", estimateService.search(keyword, status));
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", ProgressStatus.values());
        return "estimates/list";
    }

    @GetMapping("/estimates/new")
    public String createForm(@RequestParam(required = false) Long jobId, Model model) {
        model.addAttribute("estimate", new Estimate());
        model.addAttribute("jobs", jobService.findAll());
        model.addAttribute("statuses", ProgressStatus.values());
        model.addAttribute("selectedJobId", jobId);
        return "estimates/form";
    }

    @PostMapping("/estimates")
    public String create(@ModelAttribute Estimate estimate,
                         @RequestParam Long jobId,
                         @RequestParam(required = false) List<String> itemName,
                         @RequestParam(required = false) List<Integer> amount,
                         @RequestParam(required = false) List<MultipartFile> photos) throws IOException {
        Job job = jobService.findById(jobId);
        estimate.setJob(job);

        List<String> safeItemNames = itemName == null ? new ArrayList<>() : itemName;
        List<Integer> safeAmounts = amount == null ? new ArrayList<>() : amount;
        while (safeAmounts.size() < safeItemNames.size()) {
            safeAmounts.add(0);
        }
        estimateService.saveEstimate(estimate, safeItemNames, safeAmounts, photos);
        return "redirect:/estimates";
    }

    @GetMapping("/estimates/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("estimate", estimateService.findById(id));
        model.addAttribute("statuses", ProgressStatus.values());
        return "estimates/detail";
    }

    @PostMapping("/estimates/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam ProgressStatus status) {
        estimateService.updateStatus(id, status);
        return "redirect:/estimates/" + id;
    }
}
