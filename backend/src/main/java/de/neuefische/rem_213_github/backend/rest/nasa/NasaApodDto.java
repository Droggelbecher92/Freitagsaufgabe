package de.neuefische.rem_213_github.backend.rest.nasa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NasaApodDto {

    private String explanation;
    private String hdurl;
    private String title;
}
