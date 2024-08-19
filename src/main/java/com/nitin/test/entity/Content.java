package com.nitin.test.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "content", schema = "public")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "caption")
    private String caption;

    @Column(name = "title")
    private String title;

    @Column(name = "name")
    private String name;

}
