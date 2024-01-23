package org.gbif.data.pipelines.utility;

import static org.gbif.data.pipelines.utility.Commons.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CountryCodeExtractorUtility {

  public static String fetchCountryCode(String lat, String lng) {
    AtomicReference<String> countryCode = new AtomicReference<>("");
    RestTemplate restTemplate = new RestTemplate();

    var updatedUrl = URL.replace(LATITUDE, lat).replace(LONGITUDE, lng);
    var response = restTemplate.getForEntity(updatedUrl,  String.class);

    if(response.hasBody()) {
      try {
        var result = mapper.readValue(response.getBody(), ArrayList.class);
        if(!result.isEmpty())
          extractCountryCode(countryCode, result);
      } catch (JsonProcessingException e) {
        log.error("ERROR [{}]", e.getLocalizedMessage());
      }
    }
    return countryCode.get();
  }

  private static void extractCountryCode(AtomicReference<String> countryCode, ArrayList result) {
    result.parallelStream().forEachOrdered(item -> {
      var itemData = mapper.convertValue(item, new TypeReference<LinkedHashMap<String, String>>() {});
      if(itemData.get(TYPE_KEY).equals(POLITICAL_TYPE_KEY))
        countryCode.set(itemData.get(COUNTRY_CODE_KEY));
    });
  }


}
