package edu.upc.dsa.models.dto;

public class PeticionsDePrestecDTO
{
    String idLector;
    String isbn;

    public PeticionsDePrestecDTO() {}

    public PeticionsDePrestecDTO(String idLector, String isbn)
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
