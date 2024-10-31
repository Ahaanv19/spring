package com.nighthawk.spring_portfolio.mvc.issue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import java.util.ArrayList;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String bathroom;

    private String issue;
    
    private int count;

    public Issue(String bathroomName, String issue, int count)
    {
        this.bathroom = bathroomName;
        this.issue = issue;
        this.count = count;
    }

    public static Issue[] init()
    {
        ArrayList<Issue> issues = new ArrayList<>();
        issues.add(new Issue("Bathroom 1", "No Door Lock", 0));
        issues.add(new Issue("Bathroom 1", "No Toilet Paper", 0));
        issues.add(new Issue("Bathroom 1", "No Stall Doors", 0));

        issues.add(new Issue("Bathroom 2", "No Door Lock", 0));
        issues.add(new Issue("Bathroom 2", "No Toilet Paper", 0));
        issues.add(new Issue("Bathroom 2", "No Stall Doors", 0));

        issues.add(new Issue("Bathroom 3", "No Door Lock", 0));
        issues.add(new Issue("Bathroom 3", "No Toilet Paper", 0));
        issues.add(new Issue("Bathroom 3", "No Stall Doors", 0));

        return issues.toArray(new Issue[0]);
    }
}
