package com.example.SGUCharity_Project.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "fundraisingcampaign_model")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingCampaign_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "goal_amount")
    private double goalAmount;

    @Column(name = "amount_raised")
    private double amountRaised;

    private String category;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "detail_url")
    private String detailUrl;

    @Transient
    public int getProgressPercentage() {
        if (goalAmount > 0) {
            return (int) Math.round((amountRaised * 100) / goalAmount);
        } else {
            return 0;
        }
    }

    @Transient
    public boolean isActive() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate end = LocalDate.parse(endDate, formatter);
        return !today.isAfter(end);
    }
}
