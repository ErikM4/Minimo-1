package edu.upc.dsa.models;

public class LlibreCatalogat
{
    String isbn;
    String titol;
    String editorial;
    int anyPublicacio;
    int numeroEdicio;
    String autor;
    String tematica;
    int quantitatDisponible;

    public LlibreCatalogat() {}

    // Constructor per crear un nou llibre al catàleg a partir d'un llibre físic
    public LlibreCatalogat(Llibre llibre)
    {
        this.isbn = llibre.getIsbn();
        this.titol = llibre.getTitol();
        this.editorial = llibre.getEditorial();
        this.anyPublicacio = llibre.getAnyPublicacio();
        this.numeroEdicio = llibre.getNumeroEdicio();
        this.autor = llibre.getAutor();
        this.tematica = llibre.getTematica();
        this.quantitatDisponible = 0;
    }

    //Getters i Setters

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }
    public int getQuantitatDisponible() { return quantitatDisponible; }
    public void setQuantitatDisponible(int quantitatDisponible) { this.quantitatDisponible = quantitatDisponible; }

    @Override
    public String toString()
    {
        return "LlibreCatalogat [isbn=" + isbn + ", titol=" + titol + ", quantitatDisponible=" + quantitatDisponible + "]";
    }
}
