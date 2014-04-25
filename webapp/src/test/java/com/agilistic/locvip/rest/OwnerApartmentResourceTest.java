package com.agilistic.locvip.rest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.agilistic.locvip.Apartment;
import com.agilistic.locvip.service.ApartmentService;

public class OwnerApartmentResourceTest {

	private OwnerApartmentResource resource;
	private ApartmentService apartmentService;

	private Apartment apartment1;

	@Before
	public void init() {
		apartmentService = mock(ApartmentService.class);

		resource = new OwnerApartmentResource();
		resource.setApartmentService(apartmentService);
		resource.init();

		apartment1 = new Apartment();
		apartment1.setAddress("address");
		apartment1.setAreaInSquareFeet(750.0);
		apartment1.setAvailabilityDate("NOW");
		apartment1.setBathroomCount(1);
		apartment1.setBedroomCount(2);
		apartment1.setDescription("description");
		apartment1.setFloorCount(1);
		apartment1.setFloorLocation("2");
		apartment1.setMonthlyRent(700);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getApartmentForKnownOwner() throws Exception {
		// given
		when(apartmentService.getApartmentsFor(anyLong())).thenReturn(Arrays.asList(apartment1));

		// test
		Response response = resource.getApartments(1);

		// verify
		Assert.assertNotNull(response);

		Object entity = response.getEntity();
		Assert.assertTrue(entity instanceof Collection);
		Collection<Apartment> apartments = (Collection) entity;
		Assert.assertEquals(1, apartments.size());
		Assert.assertEquals(apartment1, apartments.iterator().next());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getApartmentForUnknownOwner() throws Exception {
		// given
		when(apartmentService.getApartmentsFor(anyLong())).thenReturn(new ArrayList<Apartment>());

		// test
		Response response = resource.getApartments(2);

		// verify
		Assert.assertNotNull(response);

		Object entity = response.getEntity();
		Assert.assertTrue(entity instanceof Collection);
		Collection<Apartment> apartments = (Collection) entity;
		Assert.assertEquals(0, apartments.size());
	}

}
