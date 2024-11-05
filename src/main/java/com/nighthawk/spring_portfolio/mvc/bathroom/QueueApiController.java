package com.nighthawk.spring_portfolio.mvc.bathroom;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;

@RestController
@RequestMapping("/api/queue")
public class QueueApiController {

    @Autowired
    private QueueJPARepository repository;

    // DTO class for queue entries
    @Getter
    public static class QueueDto {
        private String teacherName;
        private String studentName;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addToQueue(@RequestBody QueueDto queueDto) {
        // Check if a queue entry for the teacher already exists
        Optional<BathroomQueue> existingQueue = repository.findByTeacherName(queueDto.getTeacherName());
        if (existingQueue.isPresent()) {
            existingQueue.get().addStudent(queueDto.getStudentName());
            repository.save(existingQueue.get());
        }
        else {
            BathroomQueue newQueue = new BathroomQueue(queueDto.getTeacherName(), queueDto.getStudentName());
            repository.save(newQueue);
        }
        return new ResponseEntity<>(queueDto.getStudentName() + " was added to " + queueDto.getTeacherName(), HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://127.0.0.1:4100")
    @DeleteMapping("/remove")
    public ResponseEntity<Object> removeFromQueue(@RequestBody QueueDto queueDto) {
        Optional<BathroomQueue> queueEntry = repository.findByTeacherName(queueDto.getTeacherName());
        if (queueEntry.isPresent()) {
            BathroomQueue bathroomQueue = queueEntry.get();
            try {
                bathroomQueue.removeStudent(queueDto.getStudentName());
                repository.save(bathroomQueue);
                return new ResponseEntity<>("Removed " + queueDto.getStudentName() + " from " + queueDto.getTeacherName() + "'s queue", HttpStatus.OK);
            } 
            catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        
        return new ResponseEntity<>("Queue for " + queueDto.getTeacherName() + " not found", HttpStatus.NOT_FOUND);
    }
    @PostMapping("/approve")
    public ResponseEntity<Object> approveFrontStudent(@RequestBody QueueDto queueDto) {
        Optional<BathroomQueue> queueEntry = repository.findByTeacherName(queueDto.getTeacherName());
        if (queueEntry.isPresent()) {
            BathroomQueue bathroomQueue = queueEntry.get();
            String frontStudent = bathroomQueue.getFrontStudent();
            if (frontStudent != null && frontStudent.equals(queueDto.getStudentName())) {
                bathroomQueue.approveFrontStudent();
                repository.save(bathroomQueue);
                return new ResponseEntity<>("Approved " + queueDto.getStudentName(), HttpStatus.OK);
            } 
            else {
                return new ResponseEntity<>("Student is not at the front of the queue", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Queue for " + queueDto.getTeacherName() + " not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BathroomQueue>> getAllQueues() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }
}