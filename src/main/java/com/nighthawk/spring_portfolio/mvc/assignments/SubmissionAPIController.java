package com.nighthawk.spring_portfolio.mvc.assignments;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionAPIController {

    @Autowired
    private SubmissionJPA submissionRepo;

    @Autowired
    private AssignmentJpaRepository assignmentRepo;

    @Autowired
    private PersonJpaRepository personRepo;

    @GetMapping
    public ResponseEntity<?> getAllSubmissions() {
        List<Submission> submissions = submissionRepo.findAll();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }
    @Transactional
    @PostMapping("/grade/{submissionId}")
    public ResponseEntity<?> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestParam Double grade,
            @RequestParam(required = false) String feedback) {
        
        Submission submission = submissionRepo.findById(submissionId).orElse(null);
        
        if (submission != null) {
            submission.setGrade(grade);
            submission.setFeedback(feedback);
            
            Submission updatedSubmission = submissionRepo.save(submission);
            
            return new ResponseEntity<>(updatedSubmission, HttpStatus.OK);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Submission not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @Transactional
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        // Log the incoming request
        System.out.println("Fetching submissions for assignment ID: " + assignmentId);

        // Verify the assignment exists first
        Assignment assignment = assignmentRepo.findById(assignmentId)
            .orElse(null);
        
        if (assignment == null) {
            System.out.println("Assignment not found with ID: " + assignmentId);
            return new ResponseEntity<>(
                Collections.singletonMap("error", "Assignment not found"), 
                HttpStatus.NOT_FOUND
            );
        }

        // Find submissions for this assignment
        List<Submission> submissions = submissionRepo.findByAssignmentId(assignmentId);
        
        System.out.println("Found " + submissions.size() + " submissions");
        for(Submission s: submissions){
            s.printInfo();
            //System.out.println(s.getAssignment().toString());
        }

        ResponseEntity<List<Submission>> response = new ResponseEntity<>(submissions, HttpStatus.OK);
        System.out.println(response);
        
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }
}