package com.example.userservice.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonDeserialize;

@Entity
@Getter
@Setter
@JsonDeserialize(as=Role.class)
public class Role extends BaseModel{

    private  String role;
}
