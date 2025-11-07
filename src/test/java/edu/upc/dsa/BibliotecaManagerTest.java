package edu.upc.dsa;

// Imports de JUnit 4
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Import del Logger
import org.apache.log4j.Logger;

// Imports del nostre codi
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.LlibreCatalogat;
import edu.upc.dsa.models.Prestec;

import java.util.Date;
import java.util.List;

public class BibliotecaManagerTest
{

    // Instància del Manager
    BibliotecaManager bm;

    private static final Logger logger = Logger.getLogger(BibliotecaManagerTest.class.getName());

    @Before
    public void setUp()
    {
        this.bm = BibliotecaManagerImpl.getInstance();
    }

    @After
    public void tearDown() {
        this.bm.clear();
    }

    @Test
    public void testAfegirLectorEmmagatzemarCatalogarIPrestar() throws Exception
    {
        // 1. Comprovar que el manager és buit
        assertEquals(0, this.bm.sizeLectors());
        assertEquals(0, this.bm.sizeCataleg());
        assertEquals(0, this.bm.sizeMagatzemLlibres());

        // 2. Afegir un Lector
        Lector lector = new Lector("001", "Erik", "Medialdea", "47668655M", "07/04/2004", "Barcelona", "Sant Boi");

        this.bm.afegirUnNouLector(lector);
        assertEquals(1, this.bm.sizeLectors());

        // 3. Emmagatzemar un Llibre
        Llibre llibre = new Llibre("001", "123456789", "One Piece", "Norma", 2007, 1, "Eichiro Oda", "Shonnen");
        this.bm.emmagatzemarUnLlibre(llibre);
        assertEquals(1, this.bm.sizeMagatzemLlibres());
        assertEquals(1, this.bm.sizeMagatzemMunts());

        // 4. Catalogar el llibre
        LlibreCatalogat lc = this.bm.catalogarUnLlibre();
        assertEquals(0, this.bm.sizeMagatzemLlibres());
        assertEquals(1, this.bm.sizeCataleg());
        assertEquals("123456789", lc.getIsbn());
        assertEquals(1, lc.getQuantitatDisponible());

        // 5. Prestar el llibre
        Prestec p = this.bm.prestarUnLlibre("001", "123456789");
        assertNotNull(p);
        assertEquals("En tràmit", p.getStatus());
        assertEquals(0, lc.getQuantitatDisponible());

        // 6. Consultar els préstecs del lector
        List<Prestec> prestecs = this.bm.consultarPrestecsLector("001");
        assertEquals(1, prestecs.size());
        assertEquals(p.getIdPrestec(), prestecs.get(0).getIdPrestec());
    }

    //Prova específica de l'estructura del Magatzem (Cua de Piles)
    @Test
    public void testLogicaMagatzemMunts() throws Exception
    {
        // 1. Afegir 10 llibres (per omplir el primer munt)
        for (int i = 0; i < 10; i++)
        {
            this.bm.emmagatzemarUnLlibre(new Llibre("id" + i, "isbn" + i, "Llibre " + i, "Ed", 2000, 1, "Autor", "Tema"));
        }
        assertEquals(10, this.bm.sizeMagatzemLlibres());
        assertEquals(1, this.bm.sizeMagatzemMunts());

        // 2. Afegir el llibre número 11 per que es crei un munt nou
        Llibre llibre11 = new Llibre("id10", "isbn10", "Llibre 10", "Ed", 2000, 1, "Autor", "Tema");
        this.bm.emmagatzemarUnLlibre(llibre11);
        assertEquals(11, this.bm.sizeMagatzemLlibres());
        assertEquals(2, this.bm.sizeMagatzemMunts());

        // 3. Catalogar el primer llibre
        LlibreCatalogat lc1 = this.bm.catalogarUnLlibre();
        assertEquals("isbn9", lc1.getIsbn());
        assertEquals(10, this.bm.sizeMagatzemLlibres());
        assertEquals(2, this.bm.sizeMagatzemMunts());

        // 4. Catalogar 9 llibres més per que es buidi el primer munt
        for (int i = 0; i < 9; i++)
        {
            this.bm.catalogarUnLlibre();
        }

        // 5. Comprovar estat
        assertEquals(1, this.bm.sizeMagatzemLlibres());
        assertEquals(1, this.bm.sizeMagatzemMunts());

        // 6. Catalogar l'últim llibre
        LlibreCatalogat lc11 = this.bm.catalogarUnLlibre();
        assertEquals("isbn10", lc11.getIsbn());
        assertEquals(0, this.bm.sizeMagatzemLlibres());
        assertEquals(0, this.bm.sizeMagatzemMunts());
    }

    //Prova dels Excepcions

    @Test(expected = MagatzemBuitException.class)
    public void testCatalogarMagatzemBuit() throws MagatzemBuitException
    {
        logger.info("Iniciant testCatalogarMagatzemBuit");
        assertEquals(0, this.bm.sizeMagatzemLlibres());
        this.bm.catalogarUnLlibre();
    }

    @Test(expected = LectorNoExisteixException.class)
    public void testPrestarLectorNoExisteix() throws Exception
    {
        logger.info("Iniciant testPrestarLectorNoExisteix");
        this.bm.emmagatzemarUnLlibre(new Llibre("id1", "isbn1", "T", "A", 2000, 1, "A", "T"));
        this.bm.catalogarUnLlibre();
        assertEquals(1, this.bm.sizeCataleg());

        this.bm.prestarUnLlibre("lectorQueNoExisteix", "isbn1");
    }

    @Test(expected = LlibreNoExisteixException.class)
    public void testPrestarLlibreNoExisteix() throws Exception
    {
        logger.info("Iniciant testPrestarLlibreNoExisteix");
        this.bm.afegirUnNouLector(new Lector("lector001", "Anna", "Soler", "87654321Z", null, null, null));
        this.bm.prestarUnLlibre("lector001", "isbnQueNoExisteix");
    }

    @Test(expected = NoHiHaExemplarsException.class)
    public void testPrestarSenseExemplars() throws Exception
    {
        logger.info("Iniciant testPrestarSenseExemplars");

        this.bm.afegirUnNouLector(new Lector("lector001", "Anna", "Soler", "87654321Z", null, null, null));
        this.bm.emmagatzemarUnLlibre(new Llibre("id1", "isbn1", "T", "A", 2000, 1, "A", "T"));

        //Retorn de catalogarUnLlibre()
        LlibreCatalogat lc = this.bm.catalogarUnLlibre();
        assertEquals(1, lc.getQuantitatDisponible());

        // 1. Fer el primer préstec (correcte)
        this.bm.prestarUnLlibre("lector001", "isbn1");

        // Comprovar que ara n'hi ha 0
        assertEquals(0, lc.getQuantitatDisponible());

        // 2. Intentar tornar a prestar el mateix llibre
        this.bm.prestarUnLlibre("lector001", "isbn1");
    }
}