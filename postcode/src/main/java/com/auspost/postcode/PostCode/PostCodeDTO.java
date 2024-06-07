package com.auspost.postcode.PostCode;

import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PostCodeDTO {
    private Long id; // for update - not needed for create

    @NotNull
    private String postcode;

    private Set<Long> suburbIds = new HashSet<>();

    public Set<Long> getSuburbIds() {
        return suburbIds;
    }

    public void setSuburbIds(Set<Long> suburbIds) {
        this.suburbIds = suburbIds;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode.toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
