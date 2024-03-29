package org.gbif.data.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.gbif.data.service.DataTransformerService;
import org.gbif.data.utility.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/data")
@Slf4j
public class DataTransformerController {

  private final DataTransformerService dataTransformerService;

  @Autowired
  public DataTransformerController(DataTransformerService dataTransformerService) {
    this.dataTransformerService = dataTransformerService;
  }

  @PostMapping(value = "/transform", produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> transformDataZip(@RequestParam("file") MultipartFile file) {
    dataTransformerService.generateTransformedDataFiles(file);
    try (var zip = new ZipFile(Commons.ZIP_AVRO_FILE)) {
      if (!zip.getFile().exists()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(
          new ByteArrayResource(Files.readAllBytes(Paths.get(zip.getFile().toURI())))
              .getByteArray());
    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
