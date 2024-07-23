package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "conjugation")
public class Conjugation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String yo;

    public String tu;

    public String el_ella;

    public String vosotros;

    public String nosotros;

    public String ellos;

    @ManyToOne
    @JoinColumn(name = "verb_id")
    private Verb verb;
}
