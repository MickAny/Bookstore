package com.catalogue.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Column(name = "details")
    @Size(max = 10000)
    private String details;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "image")
    private String image;
}