package edu.upc.dsa;

import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.LlibreCatalogat;
import edu.upc.dsa.models.Prestec;
import edu.upc.dsa.exceptions.*;

import java.util.List;

public interface BibliotecaManager {

    public Lector afegirUnNouLector(Lector lector);

    public void emmagatzemarUnLlibre(Llibre llibre);

    public LlibreCatalogat catalogarUnLlibre() throws MagatzemBuitException;

    public Prestec prestarUnLlibre(String idLector, String isbn)
            throws LectorNoExisteixException, LlibreNoExisteixException, NoHiHaExemplarsException;

    public List<Prestec> consultarPrestecsLector(String idLector);

    // Mètodes auxiliars per a proves (JUnit)
    public int sizeLectors();
    public int sizeCataleg();
    public int sizeMagatzemMunts();
    public int sizeMagatzemLlibres();
    public void clear();
}