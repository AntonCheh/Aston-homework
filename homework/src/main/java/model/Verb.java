package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "verb")
public class Verb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String verbo;

    @OneToMany(mappedBy = "verb", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conjugation> conjugations;
}


