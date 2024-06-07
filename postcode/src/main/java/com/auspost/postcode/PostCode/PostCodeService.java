package com.auspost.postcode.PostCode;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auspost.postcode.Suburb.Suburb;
import com.auspost.postcode.Suburb.SuburbService;
import com.auspost.postcode.exceptions.ServiceValidationException;
import com.auspost.postcode.exceptions.ValidationErrors;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class PostCodeService {
    private static final Logger fullLogsLogger = LogManager.getLogger("fullLogs");

    @Autowired
    private PostCodeRepository repo;

    @Autowired
    private SuburbService suburbService;

    public PostCode createPostCode(@Valid PostCodeDTO data) throws ServiceValidationException {
        PostCode newPostCode = new PostCode();
        ValidationErrors errors = new ValidationErrors();

        String trimmedPostCodeField = data.getPostcode().trim();

        if (trimmedPostCodeField.isEmpty()) {
            errors.addError("PostCode", "Postcode field needs to have a value.");
        }

        if (trimmedPostCodeField.length() != 4) {
            errors.addError("PostCode", "Postcode field must have length of 4 characters.");
        }

        if (!trimmedPostCodeField.matches("[\\d]{4}")) {
            errors.addError("PostCode", "Postcode field should only contain numbers.");
        }

        newPostCode.setPostcode(trimmedPostCodeField);

        Set<Long> suburbIds = new HashSet<>(data.getSuburbIds());
        for (Long id : suburbIds) {
            Optional<Suburb> maybeSuburb = this.suburbService.findById(id);

            if (maybeSuburb.isEmpty()) {
                errors.addError("Suburb", String.format("Suburb with id %s does not exist",
                        id));
            } else {
                newPostCode.setAssociatedSuburbs(maybeSuburb.get());
            }
        }

        // attempt all validation before throwing an error
        if (errors.hasErrors()) {
            throw new ServiceValidationException(errors);
        }

        PostCode savedPostCode = this.repo.save(newPostCode);
        fullLogsLogger.info("Created new PostCode in db with ID: " + savedPostCode.getId());
        return savedPostCode;
    }

    // will return an empty array which negate error handling needs
    public List<PostCode> findAllPostCodes() {
        List<PostCode> postCodes = this.repo.findAll();
        fullLogsLogger.info("Sourced all postcodes from the db");
        return postCodes;
    }

    public Optional<PostCode> findById(Long id) {
        Optional<PostCode> foundPostCode = this.repo.findById(id);
        fullLogsLogger.info("Located PostCode in db with ID: " + foundPostCode.get().getId());
        return foundPostCode;
    }
}
