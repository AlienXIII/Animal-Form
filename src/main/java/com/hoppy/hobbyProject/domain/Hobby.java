package com.hoppy.hobbyProject.domain;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.IOException;

@Entity
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Size(min = 5)
    private String name;
    @Size (min=1)
    private String description;
    private int currentImageID;

    @Lob
    private byte[] file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentImageID() {
        return currentImageID;
    }

    public void setCurrentImageID(int currentImageID) {
        this.currentImageID = currentImageID;
    }

    public byte[] getFile() {
        return file;
    }
        //setter zmieniamy z defaultowego na taki któy przyjmuje MultipartFile
        // i tu jedynie do naszego Loba byte[] file przypisujemy file.getBytes()

    public void setFile(MultipartFile file) {
            try {
                this.file = file.getBytes();
            }catch (IOException e){     //błędy zapisu pliku. Disconnect, baza niedostępna itp

        }
    }
}
