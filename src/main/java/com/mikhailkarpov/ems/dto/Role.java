package com.mikhailkarpov.ems.dto;

public enum Role {

    PROJECT_MANAGER("Project Manager"), TEAM_MEMBER ("Team Member");

    public static final Role[] ALL = {PROJECT_MANAGER, TEAM_MEMBER};

    private String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
