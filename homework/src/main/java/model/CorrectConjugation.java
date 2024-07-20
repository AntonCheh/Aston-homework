package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "correct_conjugation")
public class CorrectConjugation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "yo")
    public String yo;

    @Column(name = "tu")
    public String tu;

    @Column(name = "el_ella")
    public String el_ella;

    @Column(name = "vosotros")
    public String vosotros;

    @Column(name = "nosotros")
    public String nosotros;

    @Column(name = "ellos")
    public String ellos;

    @ManyToOne
    @JoinColumn(name = "verb_id")
    public Verb verb;

}