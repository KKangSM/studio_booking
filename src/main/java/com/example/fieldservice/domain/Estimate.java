package com.example.fieldservice.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @NotNull
    private Job job;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus = ProgressStatus.QUOTE;

    private String title;
    private String note;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoFile> photos = new ArrayList<>();

    public int getTotalAmount() {
        return items.stream().mapToInt(EstimateItem::getAmount).sum();
    }

    public void addItem(EstimateItem item) {
        item.setEstimate(this);
        items.add(item);
    }

    public void addPhoto(PhotoFile photo) {
        photo.setEstimate(this);
        photos.add(photo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<EstimateItem> getItems() {
        return items;
    }

    public void setItems(List<EstimateItem> items) {
        this.items = items;
    }

    public List<PhotoFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoFile> photos) {
        this.photos = photos;
    }
}
