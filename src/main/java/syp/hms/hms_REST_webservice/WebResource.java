package syp.hms.hms_REST_webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/resource")
public class WebResource {
    private static Map<Integer, Auftrag> map;
    private static AtomicInteger counter = new AtomicInteger();

    static
    {
        map = new ConcurrentHashMap();
        map.put(1, new Auftrag(1,
                new Anfrage(
                    new Firma("Aldrian Stiegen GmbH", "Frau", "Carina", "Aldrian", "carina@stiegen.at", "0660 1234567"),
                    new Leistung(true, true, "Stadtmesse", "AT", "Graz", LocalDate.now(), LocalDate.now().plusDays(1), "Schiff"),
                    new Sendung(false, null, "Spezial LKW", 3, true, ""),
                    new Ladestelle("Austria", "Leibnitz", "8430", LocalDateTime.now()),
                    new Ladestelle("Austria", "Graz", "8530", LocalDateTime.now().plusDays(1)),
                    new Rueckverladung(false, null),
                    ""
                )

        ));
        counter.set(1);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Auftrag> getAllEnquiries()
    {
        List<Auftrag> list = map.values().stream().collect(Collectors.toList());

        return list;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnquirieById(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null)
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(auftrag).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEnquirie(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null)
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        map.remove(id);
        return Response.noContent().status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newCapture(@Context UriInfo uriInfo, Anfrage anfrage)
    {
        int id = counter.incrementAndGet();
        Auftrag auftrag = new Auftrag(id, anfrage);
        auftrag.setStatus("angefragt");
        map.put(id, auftrag);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.created(location).build();
    }

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
            old.setStatus("geaendert");
            map.put(id, old);
            return Response.noContent().status(Response.Status.OK).build();
        }
    }

    @PUT
    @Path("{id}/manager/{manager}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateManager(@PathParam("id") int id, @PathParam("manager") String manager)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag == null) {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        else {
            auftrag.setId(id);
            auftrag.setManager(manager);
            map.put(id, auftrag);
            return Response.noContent().status(Response.Status.OK).build();
        }
    }

    @GET
    @Path("{id}/manager")
    public Response getManager(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null || auftrag.getManager() == null || auftrag.getManager().equals(""))
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(auftrag.getManager()).build();
    }

    @PUT
    @Path("{id}/status/{status}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStatus(@PathParam("id") int id, @PathParam("status") String status)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag == null) {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        else {
            auftrag.setId(id);
            auftrag.setStatus(status);
            map.put(id, auftrag);
            return Response.noContent().status(Response.Status.OK).build();
        }
    }

    @GET
    @Path("{id}/status/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusList(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null || auftrag.getAenderungen() == null || auftrag.getAenderungen().equals(""))
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        List<Aenderung> list = auftrag.getAenderungen();
        return Response.ok(list).build();
    }

    @GET
    @Path("{id}/status/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusDetail(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null || auftrag.getAenderungen() == null || auftrag.getAenderungen().equals(""))
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        List<Aenderung> list = auftrag.getAenderungen();
        return Response.ok(list.get(list.size()-1)).build();
    }

    @GET
    @Path("{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@PathParam("id") int id)
    {
        Auftrag auftrag = map.get(id);
        if(auftrag ==null || auftrag.getStatus() == null || auftrag.getStatus().equals(""))
        {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(auftrag.getStatus()).build();
    }
}