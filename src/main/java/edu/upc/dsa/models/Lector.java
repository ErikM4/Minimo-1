package edu.upc.dsa.models;

public class Lector
{
    String id;
    String nom;
    String cognoms;
    String dni;
    String dataNaixement;
    String llocNaixement;
    String adreca;

    public Lector() {}

    public Lector(String id, String nom, String cognoms, String dni, String dataNaixement, String llocNaixement, String adreca)
    {
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.dni = dni;
        this.dataNaixement = dataNaixement;
        this.llocNaixement = llocNaixement;
        this.adreca = adreca;
    }

    //Getters i Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getDataNaixement() { return dataNaixement; }
    public void setDataNaixement(String dataNaixement) { this.dataNaixement = dataNaixement; }
    public String getLlocNaixement() { return llocNaixement; }
    public void setLlocNaixement(String llocNaixement) { this.llocNaixement = llocNaixement; }
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }

    @Override
    public String toString()
    {
        return "Lector [id=" + id + ", nom=" + nom + " " + cognoms + ", dni=" + dni + "]";
    }
}
