package com.agilistic.locvip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "pictureFileNames")
public class Apartment {

	private String address;

	private String floorLocation;

	private double areaInSquareFeet;

	private int bedroomCount;

	private int bathroomCount;

	private int floorCount = 1;

	private String description = "None";

	private String availabilityDate;

	private String pictureFileNames;

	private double monthlyRent;

	public void validate() throws IllegalArgumentException {
		if (StringUtils.isBlank(address)) {
			throw new IllegalArgumentException("Invalid address '" + address + "'");
		}
		if (StringUtils.isBlank(floorLocation)) {
			throw new IllegalArgumentException("Invalid floor location '" + floorLocation + "'");
		}
		if (StringUtils.isBlank(availabilityDate)) {
			throw new IllegalArgumentException("Invalid availability date '" + availabilityDate + "'");
		}
		if (floorCount < 1) {
			throw new IllegalArgumentException("Invalid floor count '" + floorCount + "'");
		}
		if (areaInSquareFeet < 0.0) {
			throw new IllegalArgumentException("Invalid area (in square feet) '" + areaInSquareFeet + "'");
		}
		if (bedroomCount < 1) {
			throw new IllegalArgumentException("Invalid bedroom count '" + bedroomCount + "'");
		}
		if (bathroomCount < 1) {
			throw new IllegalArgumentException("Invalid bathroom count '" + bathroomCount + "'");
		}
		if (monthlyRent < 0.0) {
			throw new IllegalArgumentException("Invalid monthly rent '" + monthlyRent + "'");
		}
	}

}
