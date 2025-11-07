package edu.upc.dsa.models;

public class Llibre
{
    String id;
    String isbn;
    String titol;
    String editorial;
    int anyPublicacio;
    int numeroEdicio;
    String autor;
    String tematica;

    public Llibre() {}

    public Llibre(String id, String isbn, String titol, String editorial, int anyPublicacio, int numeroEdicio, String autor, String tematica)
    {
        this.id = id;
        this.isbn = isbn;
        this.titol = titol;
        this.editorial = editorial;
        this.anyPublicacio = anyPublicacio;
        this.numeroEdicio = numeroEdicio;
        this.autor = autor;
        this.tematica = tematica;
    }

    //Getters i Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getAnyPublicacio() { return anyPublicacio; }
    public void setAnyPublicacio(int anyPublicacio) { this.anyPublicacio = anyPublicacio; }
    public int getNumeroEdicio() { return numeroEdicio; }
    public void setNumeroEdicio(int numeroEdicio) { this.numeroEdicio = numeroEdicio; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getTematica() { return tematica; }
    public void setTematica(String tematica) { this.tematica = tematica; }

    @Override
    public String toString()
    {
        return "Llibre [id=" + id + ", isbn=" + isbn + ", titol=" + titol + "]";
    }
}