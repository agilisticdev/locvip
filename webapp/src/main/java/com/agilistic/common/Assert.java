package com.agilistic.common;

import org.apache.commons.lang.StringUtils;

public class Assert {

	public static <O> O notNull(O toCheck, String errorMessage) {
		if (toCheck == null) {
			throw new IllegalArgumentException(errorMessage);
		}
		return toCheck;
	}

	public static String isNotEmpty(String toCheck, String errorMessage) {
		if (StringUtils.isBlank(toCheck)) {
			throw new IllegalArgumentException(errorMessage);
		}
		return toCheck;
	}

}
