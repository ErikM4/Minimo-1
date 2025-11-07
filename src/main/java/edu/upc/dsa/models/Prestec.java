package edu.upc.dsa.models;


public class Prestec
{
    String idPrestec;
    String idLector;
    String isbnLlibre; // Prestarem per ISBN (llibre catalogat)
    String dataPrestec;
    String dataDevolucio;
    String status; // ex: "En tràmit"

    public Prestec() {}

    public Prestec(String idPrestec, String idLector, String isbnLlibre, String dataPrestec, String dataDevolucio)
    {
        this.idPrestec = idPrestec;
        this.idLector = idLector;
        this.isbnLlibre = isbnLlibre;
        this.dataPrestec = dataPrestec;
        this.dataDevolucio = dataDevolucio;
        this.status = "En tràmit"; // Status inicial
    }

    //Getters i Setters

    public String getIdPrestec() { return idPrestec; }
    public void setIdPrestec(String idPrestec) { this.idPrestec = idPrestec; }
    public String getIdLector() { return idLector; }
    public void setIdLector(String idLector) { this.idLector = idLector; }
    public String getIsbnLlibre() { return isbnLlibre; }
    public void setIsbnLlibre(String isbnLlibre) { this.isbnLlibre = isbnLlibre; }
    public String getDataPrestec() { return dataPrestec; }
    public void setDataPrestec(String dataPrestec) { this.dataPrestec = dataPrestec; }
    public String getDataDevolucio() { return dataDevolucio; }
    public void setDataDevolucio(String dataDevolucio) { this.dataDevolucio = dataDevolucio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString()
    {
        return "Prestec [id=" + idPrestec + ", idLector=" + idLector + ", isbn=" + isbnLlibre + ", status=" + status + "]";
    }
}
