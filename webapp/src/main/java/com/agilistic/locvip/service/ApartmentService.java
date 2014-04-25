package com.agilistic.locvip.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import lombok.AccessLevel;
import lombok.Setter;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.MappingStrategy;

import com.agilistic.common.Assert;
import com.agilistic.locvip.Apartment;

@Component
public class ApartmentService {

	private static final List<Apartment> EMPTY_LIST = new ArrayList<Apartment>();

	@Value("${apartment.base.path}")
	@Setter(AccessLevel.PACKAGE)
	private String apartmentBasePath;

	private Map<Long, List<Apartment>> apartmentsByOwner = new HashMap<Long, List<Apartment>>();

	@PostConstruct
	public void init() throws Exception {

		Assert.isNotEmpty(apartmentBasePath, "The directory where all apartment CSV files are located cannot be null, empty of blank.");

		final Pattern filePattern = Pattern.compile("^apartments-owner-([0-9]+)\\.csv$");

		File basePath = new File(this.apartmentBasePath);
		if (basePath.exists() && basePath.isDirectory()) {
			File[] apartmentFiles = basePath.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return filePattern.matcher(pathname.getName()).matches();
				}
			});

			if (apartmentFiles != null && apartmentFiles.length > 0) {

				ColumnPositionMappingStrategy<Apartment> mappingStrategy = new ColumnPositionMappingStrategy<Apartment>();
				mappingStrategy.setType(Apartment.class);
				String[] columns =
				    new String[] { "address", "floorLocation", "areaInSquareFeet", "bedroomCount", "bathroomCount", "floorCount", "description", "availabilityDate", "pictureFileNames", "monthlyRent" };
				mappingStrategy.setColumnMapping(columns);

				for (File ownerFile : apartmentFiles) {
					readApartments(ownerFile, filePattern, mappingStrategy);
				}
			}
		} else {
			throw new IllegalArgumentException("The apartment base path '" + apartmentBasePath + "' is not a valid directory");
		}
	}

	void readApartments(File ownerFile, Pattern filePattern, MappingStrategy<Apartment> mappingStrategy) throws IOException {
		try {
			CSVReader reader = new CSVReader(new FileReader(ownerFile), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER, 1);
			List<Apartment> ownerApartments = (new CsvToBean<Apartment>()).parse(mappingStrategy, reader);
			validateApartments(ownerApartments);
			if (ownerApartments != null) {
				Matcher matcher = filePattern.matcher(ownerFile.getName());
				if (matcher.matches()) {
					String id = matcher.group(1);
					long ownerId = Long.parseLong(id);
					apartmentsByOwner.put(ownerId, ownerApartments);
				}
			}
		} catch (Exception ex) {
			throw new IllegalArgumentException("Cannot parse properly the CSV file '" + ownerFile.getName() + "'.", ex);
		}
	}

	private void validateApartments(List<Apartment> ownerApartments) throws IllegalArgumentException {
		if (CollectionUtils.isNotEmpty(ownerApartments)) {
			for (Apartment a : ownerApartments) {
				a.validate();
			}
		}
	}

	public List<Apartment> getApartmentsFor(long ownerId) {
		List<Apartment> list = this.apartmentsByOwner.get(ownerId);
		return (list != null ? list : EMPTY_LIST);
	}

	int getOwnerCount() {
		return this.apartmentsByOwner.size();
	}
}
