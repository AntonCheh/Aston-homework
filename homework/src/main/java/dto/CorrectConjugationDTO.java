package dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import model.Verb;

@Data
public class CorrectConjugationDTO {
    public int id;

    public String yo;

    public String tu;

    public String el_ella;

    public String vosotros;

    public String nosotros;

    public String ellos;

   // public Verb verb;

}
