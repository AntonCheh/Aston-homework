package dto;

import lombok.Data;

@Data
public class ConjugationDTO {
    private int id;
    private String yo;
    private String tu;
    private String el_ella;
    private String vosotros;
    private String nosotros;
    private String ellos;
    private int verbId;
}