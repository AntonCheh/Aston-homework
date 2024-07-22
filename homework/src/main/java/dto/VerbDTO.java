package dto;

import lombok.Data;

import java.util.List;

@Data
public class VerbDTO {
    private int id;
    private String infinitive;
    private List<ConjugationDTO> conjugations;
}
