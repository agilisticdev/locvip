package com.agilistic.locvip.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.AccessLevel;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agilistic.common.Assert;
import com.agilistic.locvip.Apartment;
import com.agilistic.locvip.service.ApartmentService;

@Component
@Path("/owner/{owner-id}")
public class OwnerApartmentResource {

	@Autowired
	@Setter(AccessLevel.PACKAGE)
	private ApartmentService apartmentService;

	@PostConstruct
	public void init() {
		Assert.notNull(apartmentService, "The apartment service canoot be null.");
	}

	@POST
	@Path("/appartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addApartments(@PathParam("owner-id") long ownerId, List<Apartment> apartments) {
		return Response.ok().build();
	}

	@GET
	@Path("/appartments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getApartments(@PathParam("owner-id") long ownerId) {
		List<Apartment> apartments = apartmentService.getApartmentsFor(ownerId);
		return Response.ok(apartments).build();
	}

}
