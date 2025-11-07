package edu.upc.dsa.models.dto;

// Un DTO simple per encapsular la petició de préstec
public class PrestecRequestDTO
{
    String idLector;
    String isbn;

    // Constructor buit, getters i setters són necessaris per a JAX-RS
    public PrestecRequestDTO() {}

    public PrestecRequestDTO(String idLector, String isbn)
    {
        this.idLector = idLector;
        this.isbn = isbn;
    }

    public String getIdLector()
    {
        return idLector;
    }

    public void setIdLector(String idLector)
    {
        this.idLector = idLector;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }
}
