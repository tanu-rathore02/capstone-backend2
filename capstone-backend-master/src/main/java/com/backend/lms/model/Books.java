package com.backend.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int availability;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categories;
}
