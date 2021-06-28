package com.skenario.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Building {

    @Id
    private String id;
    private String name;
    private String street;
    private String number;
    private String postalCode;
    private String city;
    private String country;
    private String description;


}
