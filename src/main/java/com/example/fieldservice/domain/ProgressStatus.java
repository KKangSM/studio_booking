package com.example.fieldservice.domain;

public enum ProgressStatus {
    QUOTE("견적"),
    CONSTRUCTION("시공"),
    COMPLETED_AS("완료/AS");

    private final String label;

    ProgressStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
