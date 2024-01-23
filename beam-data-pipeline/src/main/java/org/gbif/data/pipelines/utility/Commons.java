package org.gbif.data.pipelines.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Commons {

  public static final String URL = "http://api.gbif.org/v1/geocode/reverse?lat={lat}&lng={lng}";
  public static final ObjectMapper mapper = new ObjectMapper();
  public static final String COUNTRY_CODE_KEY = "isoCountryCode2Digit";
  public static final String POLITICAL_TYPE_KEY = "Political";
  public static final String TYPE_KEY = "type";
  public static final String LATITUDE = "{lat}";
  public static final String LONGITUDE = "{lng}";
  public static final String AVRO_DIR = "./avro/output.avro";

}
