package com.nighthawk.spring_portfolio.mvc.Grades;


import com.nighthawk.spring_portfolio.mvc.person.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(unique=true)
    private String assignmentName;


    //public Assignment(String assignmentName){
        //this.assignmentName = assignmentName; 
    //}


   
}

