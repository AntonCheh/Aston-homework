package dao;

import lombok.Data;

@Data
public class SpanishWord {
    private int id;
    private String yo;
    private String tu;
    private String el_ella;
    private String vosotros;
    private String nosotros;
    private String ellos;

    public SpanishWord(int id, String yo, String tu, String el_ella, String vosotros, String nosotros, String ellos) {
        this.id = id;
        this.yo = yo;
        this.tu = tu;
        this.el_ella = el_ella;
        this.vosotros = vosotros;
        this.nosotros = nosotros;
        this.ellos = ellos;
    }
}
