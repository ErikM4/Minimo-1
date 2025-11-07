package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.LlibreCatalogat;
import edu.upc.dsa.models.Prestec;

import org.apache.log4j.Logger;

import java.util.*;
import java.text.SimpleDateFormat; // <--- AFEGEIX AQUEST
import java.util.Calendar;


public class BibliotecaManagerImpl implements BibliotecaManager
{

    private static final Logger logger = Logger.getLogger(BibliotecaManagerImpl.class.getName());
    private static BibliotecaManager instance;

    private Map<String, Lector> lectorsMap;
    private LinkedList<Stack<Llibre>> magatzem; // Cua de Piles
    private Map<String, LlibreCatalogat> cataleg;
    private Map<String, List<Prestec>> prestecsPerLectorMap;

    private BibliotecaManagerImpl()
    {
        this.lectorsMap = new HashMap<>();
        this.magatzem = new LinkedList<>();
        this.cataleg = new HashMap<>();
        this.prestecsPerLectorMap = new HashMap<>();
        logger.info("BibliotecaManagerImpl instanciado (constructor privado)");
    }

    public static BibliotecaManager getInstance()
    {
        if (instance == null) {
            logger.info("Creando nueva instancia de BibliotecaManagerImpl");
            instance = new BibliotecaManagerImpl();
        }
        return instance;
    }

    @Override
    public Lector afegirUnNouLector(Lector lector)
    {
        logger.info("Inicio: afegirUnNouLector(" + lector + ")");
        this.lectorsMap.put(lector.getId(), lector);
        logger.info("Fin: Lector afegit/actualitzat: " + lector.getNom());
        return lector;
    }

    @Override
    public void emmagatzemarUnLlibre(Llibre llibre)
    {
        logger.info("Inicio: emmagatzemarUnLlibre(" + llibre + ")");

        Stack<Llibre> ultimMunt;

        if (this.magatzem.isEmpty())
        {
            logger.info("No hi ha munts, es crearà el primer munt.");
            ultimMunt = new Stack<>();
            this.magatzem.add(ultimMunt);
        } else {
            ultimMunt = this.magatzem.getLast();
        }

        if (ultimMunt.size() >= 10)
        {
            logger.info("L'últim munt (mida " + ultimMunt.size() + ") està ple. Es crearà un nou munt.");
            ultimMunt = new Stack<>();
            this.magatzem.add(ultimMunt);
        }

        ultimMunt.push(llibre);
        logger.info("El llibre " + llibre.getId() + " ha sigut apilat al munt " + (this.magatzem.size() - 1) + ". Mida actual del munt: " + ultimMunt.size());
    }

    @Override
    public LlibreCatalogat catalogarUnLlibre() throws MagatzemBuitException
    {

        if (this.magatzem.isEmpty())
        {
            // **ARREGLAT:** Passar el missatge a l'excepció
            String errorMsg = "Error: El magatzem és completament buit.";
            logger.error(errorMsg);
            throw new MagatzemBuitException(errorMsg);
        }

        Stack<Llibre> primerMunt = this.magatzem.peek();

        if (primerMunt == null || primerMunt.isEmpty())
        {
            logger.warn("El primer munt estava buit, ha sigut descartat.");
            this.magatzem.poll();

            if (this.magatzem.isEmpty())
            {
                String errorMsg = "Error: No queden més munts per catalogar.";
                logger.error(errorMsg);
                throw new MagatzemBuitException(errorMsg);
            }
            primerMunt = this.magatzem.peek();
        }

        Llibre llibreACatalogar = primerMunt.pop();
        logger.info("Llibre extret del munt: " + llibreACatalogar);

        if (primerMunt.isEmpty())
        {
            logger.info("El munt ha quedat buit, el traiem de la cua.");
            this.magatzem.poll();
        }

        LlibreCatalogat lc = this.cataleg.get(llibreACatalogar.getIsbn());

        if (lc != null)
        {
            logger.info("El llibre (ISBN: " + lc.getIsbn() + ") ja existia al catàleg. Incrementant quantitat.");
            lc.setQuantitatDisponible(lc.getQuantitatDisponible() + 1);
        }
        else
        {
            logger.info("El llibre (ISBN: " + llibreACatalogar.getIsbn() + ") és nou al catàleg.");
            lc = new LlibreCatalogat(llibreACatalogar);
            lc.setQuantitatDisponible(1);
            this.cataleg.put(lc.getIsbn(), lc);
        }

        logger.info("Fin: catalogarUnLlibre. Estat catàleg: " + lc);
        return lc;
    }

    @Override
    public Prestec prestarUnLlibre(String idLector, String isbn) throws LectorNoExisteixException, LlibreNoExisteixException, NoHiHaExemplarsException
    {

        logger.info("Inicio: prestarUnLlibre(idLector=" + idLector + ", isbn=" + isbn + ")");

        Lector lector = this.lectorsMap.get(idLector);
        if (lector == null)
        {
            String errorMsg = "Error: Lector no trobat. ID: " + idLector;
            logger.error(errorMsg);
            throw new LectorNoExisteixException(errorMsg);
        }

        LlibreCatalogat lc = this.cataleg.get(isbn);
        if (lc == null)
        {
            String errorMsg = "Error: Llibre no trobat al catàleg. ISBN: " + isbn;
            logger.error(errorMsg);
            throw new LlibreNoExisteixException(errorMsg);
        }

        if (lc.getQuantitatDisponible() == 0)
        {
            String errorMsg = "Error: No queden exemplars disponibles. ISBN: " + isbn;
            logger.error(errorMsg);
            throw new NoHiHaExemplarsException(errorMsg);
        }

        logger.info("Validacions correctes. Realitzant préstec.");
        lc.setQuantitatDisponible(lc.getQuantitatDisponible() - 1);
        String idPrestec = java.util.UUID.randomUUID().toString(); // ID aleatori
        // 1. Definim el format de text que volem
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // 2. Obtenim els objectes Date
        Date dataPrestecObject = new Date(); // Data actual
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataPrestecObject);
        cal.add(Calendar.DAY_OF_YEAR, 15);
        Date dataDevolucioObject = cal.getTime();

        // 3. Els convertim a String
        String dataPrestecString = df.format(dataPrestecObject);
        String dataDevolucioString = df.format(dataDevolucioObject);

        // 4. Cridem el constructor amb els Strings
        Prestec prestec = new Prestec(idPrestec, idLector, isbn, dataPrestecString, dataDevolucioString);
        this.prestecsPerLectorMap.putIfAbsent(idLector, new ArrayList<>());
        this.prestecsPerLectorMap.get(idLector).add(prestec);

        logger.info("Fin: Préstec creat correctament: " + prestec);
        return prestec;
    }

    @Override
    public List<Prestec> consultarPrestecsLector(String idLector)
    {
        logger.info("Inicio: consultarPrestecsLector(idLector=" + idLector + ")");
        List<Prestec> prestecs = this.prestecsPerLectorMap.get(idLector);
        if (prestecs == null)
        {
            logger.info("El lector " + idLector + " no té cap préstec. Retornant llista buida.");
            return new ArrayList<>();
        }
        logger.info("Fin: Retornant " + prestecs.size() + " préstecs per al lector " + idLector);
        return prestecs;
    }

    // --- Mètodes auxiliars per a proves ---

    @Override
    public int sizeLectors()
    {
        return this.lectorsMap.size();
    }
    @Override
    public int sizeCataleg()
    {
        return this.cataleg.size();
    }
    @Override
    public int sizeMagatzemMunts()
    {
        return this.magatzem.size();
    }
    @Override
    public int sizeMagatzemLlibres()
    {
        int total = 0;
        for (Stack<Llibre> munt : this.magatzem) {
            total += munt.size();
        }
        return total;
    }
    @Override
    public void clear()
    {
        instance = null;
        this.lectorsMap.clear();
        this.magatzem.clear();
        this.cataleg.clear();
        this.prestecsPerLectorMap.clear();
        logger.info("Instancia de BibliotecaManagerImpl resetejada (clear)");
    }
}