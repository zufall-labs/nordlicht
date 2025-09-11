package ai.zufall.nordlicht.calc;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An example service class.
 */
@Path("/api/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final List<Item> items = new ArrayList<>();

    @GET
    public List<Item> listAll() {
        return items;
    }

    /**
     * Get item by ID.
     *
     * @param id The ID
     * @return The item
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return items.stream()
            .filter(item -> item.id().equals(id))
            .findFirst()
            .map(Response::ok)
            .orElse(Response.status(Response.Status.NOT_FOUND))
            .build();
    }

    @POST
    public Response create(Item item) {
        items.add(item);
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    /**
     * Delete item by ID.
     *
     * @param id The ID
     * @return Success or error
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        final Optional<Item> item = items.stream()
            .filter(i -> i.id().equals(id))
            .findFirst();

        if (item.isPresent()) {
            items.remove(item.get());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
