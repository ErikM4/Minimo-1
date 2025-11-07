package edu.upc.dsa.services;

import edu.upc.dsa.BibliotecaManager;
import edu.upc.dsa.BibliotecaManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Llibre;
import edu.upc.dsa.models.LlibreCatalogat;
import edu.upc.dsa.models.Prestec;
import edu.upc.dsa.models.dto.MissatgesError;
import edu.upc.dsa.models.dto.PrestecRequestDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/biblioteca", description = "Servei REST per a la Biblioteca")
@Path("/biblioteca")
public class BibliotecaService
{

    private BibliotecaManager bm;

    public BibliotecaService()
    {
        this.bm = BibliotecaManagerImpl.getInstance();

        if (this.bm.sizeLectors() == 0)
        {
            // Dades inicials
            this.bm.afegirUnNouLector(new Lector("001", "Erik", "Medialdea", "47668655M", "47668655M", null, null));
            this.bm.emmagatzemarUnLlibre(new Llibre("id001", "ISBN-001", "One Piece Tomo 1", "NormaEditorial", 1954, 1, "Eichiro Oda", "Shonnen"));
            try
            {
                this.bm.catalogarUnLlibre();
            }
            catch (MagatzemBuitException e) {}
        }
    }

    @POST
    @Path("/lectors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Afegir un nou lector", notes = "Afegeix un nou lector o actualitza un existent si l'ID coincideix")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lector afegit/actualitzat", response = Lector.class)})
    public Response afegirLector(Lector lector)
    {
        if (lector.getId() == null || lector.getNom() == null)
        {
            return Response.status(400).entity(new MissatgesError("ID i Nom són camps obligatoris")).build();
        }
        Lector nouLector = this.bm.afegirUnNouLector(lector);
        // Retornem 201 Created
        return Response.status(200).entity(nouLector).build();
    }

    @POST
    @Path("/magatzem/llibres")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Emmagatzemar un llibre", notes = "Afegeix un nou llibre al sistema de magatzem (munts)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Llibre emmagatzemat correctament"),
            @ApiResponse(code = 400, message = "Dades del llibre invàlides")
    })
    public Response emmagatzemarLlibre(Llibre llibre)
    {
        if (llibre.getIsbn() == null || llibre.getTitol() == null)
        {
            return Response.status(400).entity(new MissatgesError("ISBN i Títol són camps obligatoris")).build();
        }
        this.bm.emmagatzemarUnLlibre(llibre);
        // Retornem 204 No Content
        return Response.status(200).build();
    }

    @POST
    @Path("/cataleg/catalogar")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Catalogar el següent llibre", notes = "Processa el següent llibre del magatzem i l'afegeix al catàleg")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Llibre catalogat", response = LlibreCatalogat.class),
            @ApiResponse(code = 404, message = "Magatzem buit", response = MissatgesError.class)})

    public Response catalogarLlibre()
    {
        try
        {
            LlibreCatalogat lc = this.bm.catalogarUnLlibre();
            //200 OK
            return Response.status(200).entity(lc).build();
        }
        catch (MagatzemBuitException e)
        {
            return Response.status(404).entity(new MissatgesError(e.getMessage())).build();
        }
    }

    @POST
    @Path("/prestecs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Realitzar un nou préstec", notes = "Crea un nou préstec d'un llibre a un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Préstec realitzat", response = Prestec.class),
            @ApiResponse(code = 400, message = "Lector o Llibre no trobat", response = MissatgesError.class),
            @ApiResponse(code = 400, message = "No hi ha exemplars disponibles", response = MissatgesError.class)})

    public Response prestarLlibre(PrestecRequestDTO prestecRequest)
    {
        try
        {
            Prestec p = this.bm.prestarUnLlibre(prestecRequest.getIdLector(), prestecRequest.getIsbn());
            //201 Created
            return Response.status(200).entity(p).build();
        }
        catch (LectorNoExisteixException | LlibreNoExisteixException e)
        {
            return Response.status(400).entity(new MissatgesError(e.getMessage())).build();
        } catch (NoHiHaExemplarsException e)
        {
            return Response.status(400).entity(new MissatgesError(e.getMessage())).build();
        }
    }

    @GET
    @Path("/lectors/{id}/prestecs")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consultar préstecs d'un lector", notes = "Retorna la llista de tots els préstecs d'un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Llista de préstecs", response = Prestec.class, responseContainer = "List"),})
    public Response consultarPrestecsLector(@PathParam("id") String idLector) {
        List<Prestec> prestecs = this.bm.consultarPrestecsLector(idLector);

        GenericEntity<List<Prestec>> entity = new GenericEntity<List<Prestec>>(prestecs) {};

        //200 OK
        return Response.status(200).entity(entity).build();
    }
}
