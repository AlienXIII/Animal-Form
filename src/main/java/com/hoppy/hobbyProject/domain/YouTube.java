package com.hoppy.hobbyProject.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter

public class YouTube {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long YtId;

    private String YtUrl;

}
