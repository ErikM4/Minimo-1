package edu.upc.dsa.services;

import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.models.dto.*;

import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Api(value = "/biblioteca", description = "Servei REST per a la Biblioteca")
@Path("/biblioteca")
public class BibliotecaService
{

    private BibliotecaManager bm;

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
        //201 Created
        return Response.status(201).entity(nouLector).build();
    }

    @POST
    @Path("/magatzem/llibres")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Emmagatzemar un llibre", notes = "Afegeix un nou llibre al sistema de magatzem (munts)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Llibre emmagatzemat correctament"),
            @ApiResponse(code = 400, message = "Dades del llibre invàlides")
    })
    public Response emmagatzemarLlibre(Llibre llibre)
    {
        if (llibre.getIsbn() == null || llibre.getTitol() == null)
        {
            return Response.status(400).entity(new MissatgesError("ISBN i Títol són camps obligatoris")).build();
        }
        this.bm.emmagatzemarUnLlibre(llibre);
        //204 No Content
        return Response.status(204).build();
    }

    @POST
    @Path("/cataleg/catalogar")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Catalogar el següent llibre", notes = "Processa el següent llibre del magatzem i l'afegeix al catàleg")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Llibre catalogat", response = LlibreCatalogat.class),
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
            @ApiResponse(code = 201, message = "Préstec realitzat", response = Prestec.class),
            @ApiResponse(code = 400, message = "Lector o Llibre no trobat", response = MissatgesError.class),
            @ApiResponse(code = 400, message = "No hi ha exemplars disponibles", response = MissatgesError.class)})

    public Response prestarLlibre(PeticionsDePrestecDTO prestecRequest)
    {
        try
        {
            Prestec p = this.bm.prestarUnLlibre(prestecRequest.getIdLector(), prestecRequest.getIsbn());
            //201 Created
            return Response.status(201).entity(p).build();
        }
        catch (LectorNoExisteixException | LlibreNoExisteixException e)
        {
            return Response.status(400).entity(new MissatgesError(e.getMessage())).build();
        }
        catch (NoHiHaExemplarsException e)
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
