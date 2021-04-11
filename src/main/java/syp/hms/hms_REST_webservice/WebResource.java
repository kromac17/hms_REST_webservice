package syp.hms.hms_REST_webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/resource")
public class WebResource {
    static
    {
        DAL dal = new DAL();
        try {
            int id = dal.newAuftrag(
                    new Anfrage(
                            new Firma("Aldrian Stiegen GmbH", "Frau", "Carina", "Aldrian", "carina@stiegen.at", "0660 1234567"),
                            new Leistung(true, true, "Stadtmesse", "AT", "Graz", LocalDate.now(), LocalDate.now().plusDays(1), "See"),
                            new Sendung(false, null, "Spezial LKW", 3, true, ""),
                            new Ladestelle("Austria", "Leibnitz", "8430", LocalDateTime.now()),
                            new Ladestelle("Austria", "Graz", "8530", LocalDateTime.now().plusDays(1)),
                            new Rueckverladung(false, null),
                            ""
                    ),
                    new Status("Angefragt", "Ihre anfrage ist in unserem System eingelangt.")
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static Boolean debug = true;
    @POST
    @Path("lands")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertLands(List<Land> landList) throws SQLException, ClassNotFoundException {
        if(debug){
            System.out.println(landList);
            DAL dal = new DAL();
            dal.insertLands(landList);
            return Response.noContent().status(Response.Status.OK).build();
        }
        return Response.noContent().status(Response.Status.METHOD_NOT_ALLOWED).build();
    }

    @GET
    @Path("lands")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLands() throws SQLException, ClassNotFoundException {
        DAL dal = new DAL();
        List<Land> landList = dal.getAllLands();
        return Response.ok(landList).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Auftrag> getAllEnquiries() throws SQLException, ClassNotFoundException {
        DAL dal = new DAL();
        List<Auftrag> list = dal.getAll();

        return list;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnquirieById(@PathParam("id") int id) throws ClassNotFoundException {
        DAL dal = new DAL();
        Auftrag auftrag;
        try {
            auftrag = dal.getAuftrag(id);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(auftrag).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEnquirie(@PathParam("id") int id) throws ClassNotFoundException {
        DAL dal = new DAL();
        try {
            dal.deleteAuftrag(id);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newCapture(@Context UriInfo uriInfo, Anfrage anfrage) throws SQLException, ClassNotFoundException {
        DAL dal = new DAL();
        Status status = new Status("Angefragt", "Ihre anfrage ist in unserem System eingelangt.");
        int id = dal.newAuftrag(anfrage, status);
        if(id > 0){
            URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
            return Response.created(location).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /*
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCapture(@PathParam("id") int id, Anfrage anfrage)
    {
        Auftrag old = map.get(id);
        if(old == null) {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        else {
            old.setId(1);
            old.setAnfrage(anfrage);
            old.setStatus("Geaendert", "Auftragdetails wurden geandert.");
            map.put(id, old);
            return Response.noContent().status(Response.Status.OK).build();
        }
    }
    */

    @GET
    @Path("manager")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllManager() throws ClassNotFoundException {
        DAL dal = new DAL();
        List<Manager> managerList;
        try {
            managerList = dal.getAllManager();
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(managerList).build();
    }

    @GET
    @Path("{id}/manager")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManager(@PathParam("id") int id) throws ClassNotFoundException {
        DAL dal = new DAL();
        Manager manager;
        try {
            manager = dal.getManager(id);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(manager).build();
    }

    @PUT
    @Path("{id}/manager/{managerId}")
    public Response updateManager(@PathParam("id") int id, @PathParam("managerId") int managerId) throws ClassNotFoundException {
        DAL dal = new DAL();
        try {
            dal.changeManager(id, managerId);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().status(Response.Status.OK).build();
    }

    @PUT
    @Path("{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStatus(@PathParam("id") int id, Status status) throws ClassNotFoundException {
        DAL dal = new DAL();
        try {
            dal.newStatus(id, status);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().status(Response.Status.OK).build();
    }

    @GET
    @Path("{id}/aenderungen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAenderungen(@PathParam("id") int id) throws ClassNotFoundException {
        DAL dal = new DAL();
        List<Aenderung> aenderungList;
        try {
            aenderungList = dal.getAenderungen(id);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(aenderungList).build();
    }

    @GET
    @Path("{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@PathParam("id") int id) throws ClassNotFoundException {
        DAL dal = new DAL();
        Aenderung status;
        try {
            status = dal.getStatus(id);
        } catch (SQLException e) {
            System.out.println(e);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(status).build();
    }
}