package com.hoppy.hobbyProject.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Locale;

//Lombok z automatu dla ciebie robi settery i gettery. nic nie musisz dodawać
//jeśli jakiś setter czy też getter ma działać inaczej niż standardowo to robisz jak poniżej
//a jak chcesz zeby nie wszystkie fieldy mialy settera/gettera to adnotacje dajesz przy fieldzie
//ma jeszcze kilka przydatnych rzeczy jak hashCode i equals
//Slf4j i log to są Loggery czyli takie system.out.println tylko dużo lżejszy bardziej intuicyjny
//i stosowany do np zapisywania logów w pliku
@Entity
@Getter
@Setter
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Size(min = 5)
    private String name;
    @Size (min=1)
    @Lob
    private String description;
    private String currentImage;

    @ElementCollection
    private List<String> fileNames;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ElementCollection
    private List<String> youtubeVideos;

    @Lob
    String amazonCode;

    Boolean disabled;

}
