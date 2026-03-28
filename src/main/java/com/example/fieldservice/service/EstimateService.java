package com.example.fieldservice.service;

import com.example.fieldservice.domain.Estimate;
import com.example.fieldservice.domain.EstimateItem;
import com.example.fieldservice.domain.PhotoFile;
import com.example.fieldservice.domain.ProgressStatus;
import com.example.fieldservice.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final Path uploadDir;

    public EstimateService(EstimateRepository estimateRepository, @Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.estimateRepository = estimateRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public List<Estimate> search(String keyword, ProgressStatus status) {
        String safeKeyword = keyword == null ? "" : keyword.trim();
        return estimateRepository.search(safeKeyword, status);
    }

    public Estimate findById(Long id) {
        return estimateRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("견적 정보를 찾을 수 없습니다. id=" + id));
    }

    public List<Estimate> findByJobId(Long jobId) {
        return estimateRepository.findByJobIdOrderByCreatedAtDesc(jobId);
    }

    @Transactional
    public Estimate saveEstimate(Estimate estimate, List<String> itemNames, List<Integer> amounts, List<MultipartFile> photos) throws IOException {
        for (int i = 0; i < itemNames.size(); i++) {
            String itemName = itemNames.get(i);
            Integer amount = amounts.get(i);
            if (itemName == null || itemName.isBlank()) {
                continue;
            }
            EstimateItem item = new EstimateItem();
            item.setItemName(itemName.trim());
            item.setAmount(amount == null ? 0 : Math.max(amount, 0));
            estimate.addItem(item);
        }

        if (photos != null) {
            for (MultipartFile photo : photos) {
                if (photo == null || photo.isEmpty()) {
                    continue;
                }
                String storedName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
                Path target = uploadDir.resolve(storedName);
                Files.copy(photo.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                PhotoFile photoFile = new PhotoFile();
                photoFile.setOriginalName(photo.getOriginalFilename());
                photoFile.setStoredName(storedName);
                photoFile.setFilePath(target.toString());
                estimate.addPhoto(photoFile);
            }
        }

        return estimateRepository.save(estimate);
    }

    @Transactional
    public void updateStatus(Long estimateId, ProgressStatus status) {
        Estimate estimate = findById(estimateId);
        estimate.setProgressStatus(status);
    }

    public Resource loadPhoto(String storedName) throws MalformedURLException {
        Path filePath = uploadDir.resolve(storedName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + storedName);
        }
        return resource;
    }
}
