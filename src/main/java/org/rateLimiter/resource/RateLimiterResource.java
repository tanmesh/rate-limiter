package org.rateLimiter.resource;

import org.rateLimiter.service.IRateLimiterService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RateLimiterResource {
    private IRateLimiterService rateLimiterService;

    public RateLimiterResource(IRateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GET
    @Path("/limited")
    @Produces(MediaType.APPLICATION_JSON)
    public Response limited(@Context HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr() + "-" + request.getServerPort();
        String responseString;
        try {
            responseString = rateLimiterService.limited(ipAddress);
        } catch (Exception e) {
            return Response.status(429).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(responseString).build();
    }

    @GET
    @Path("/unlimited")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlimited() {
        String responseString;
        try {
            responseString = rateLimiterService.unlimited();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(responseString).build();
    }
}
