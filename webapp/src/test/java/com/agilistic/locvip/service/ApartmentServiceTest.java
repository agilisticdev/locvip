package com.agilistic.locvip.service;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.agilistic.locvip.Apartment;
import com.google.common.io.Files;

public class ApartmentServiceTest {

	private ApartmentService service;

	@Before
	public void init() {
		service = new ApartmentService();
	}

	@Test(expected = IllegalArgumentException.class)
	public void apartmentFileParentDirNull() throws Exception {
		service.setApartmentBasePath(null);
		service.init();
	}

	@Test(expected = IllegalArgumentException.class)
	public void apartmentFileParentDirEmpty() throws Exception {
		service.setApartmentBasePath("");
		service.init();
	}

	@Test(expected = IllegalArgumentException.class)
	public void apartmentFileParentDirBlank() throws Exception {
		service.setApartmentBasePath("     			 ");
		service.init();
	}

	@Test(expected = IllegalArgumentException.class)
	public void apartmentFileParentDirPointsToNothing() throws Exception {
		service.setApartmentBasePath("notadir");
		service.init();
	}

	@Test(expected = IllegalArgumentException.class)
	public void apartmentFileParentDirNotADir() throws Exception {
		File f = new File(getClass().getResource("apartments-owner-305.csv").toURI());
		service.setApartmentBasePath(f.getAbsolutePath());
		service.init();
	}

	@Test
	public void emptyApartmentFileParentDir() throws Exception {
		File tempDir = Files.createTempDir();
		tempDir.deleteOnExit();
		service.setApartmentBasePath(tempDir.getAbsolutePath());
		service.init();
		Assert.assertEquals(0, service.getOwnerCount());
	}

	@Test
	public void noValidFile() throws Exception {
		File f = new File(getClass().getResource("apartments-owner-305.csv").toURI());
		String parentDir = f.getParentFile().getParentFile().getAbsolutePath();
		service.setApartmentBasePath(parentDir);
		service.init();
		Assert.assertEquals(0, service.getOwnerCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void malformedApartmentFileContent() throws Exception {
		// given
		File baseDir = new File(getClass().getResource("apartments-owner-305.csv").toURI()).getParentFile();
		String parentDir = baseDir.getAbsolutePath() + "/invalid";
		service.setApartmentBasePath(parentDir);

		// test
		service.init();
	}

	@Test
	public void getApartmentsForUnknownOwner() throws Exception {
		// given
		successfullInit();

		// test
		List<Apartment> apartmentsForOwnerUknown = service.getApartmentsFor(7777777);

		// verify
		Assert.assertNotNull(apartmentsForOwnerUknown);
		Assert.assertTrue(CollectionUtils.isEmpty(apartmentsForOwnerUknown));
	}

	@Test
	public void successfullInit() throws Exception {
		// given
		File f = new File(getClass().getResource("apartments-owner-305.csv").toURI());
		String parentDir = f.getParentFile().getAbsolutePath();
		service.setApartmentBasePath(parentDir);

		// test
		service.init();

		// verify
		Assert.assertEquals(2, service.getOwnerCount());
	}

	@Test
	public void getApartmentsForKnownOwner() throws Exception {
		// given
		successfullInit();

		// test
		List<Apartment> apartmentsForOwner305 = service.getApartmentsFor(305);
		List<Apartment> apartmentsForOwner159904 = service.getApartmentsFor(159904);

		// verify
		Assert.assertEquals(2, apartmentsForOwner305.size());
		Assert.assertEquals(1, apartmentsForOwner159904.size());
	}

}
