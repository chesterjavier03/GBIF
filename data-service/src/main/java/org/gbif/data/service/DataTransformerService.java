package org.gbif.data.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.gbif.data.pipelines.service.CSV2AvroPipelineService;
import org.gbif.data.utility.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DataTransformerService {

  protected final CSV2AvroPipelineService csv2AvroPipelineService;

  @Autowired
  public DataTransformerService(CSV2AvroPipelineService csv2AvroPipelineService) {
    this.csv2AvroPipelineService = csv2AvroPipelineService;
  }

  public void generateTransformedDataFiles(MultipartFile inputFile) {
    try {
      File convertedFile =  convertMultipartFile(inputFile);
      if(convertedFile.exists()) {
        CSV2AvroPipelineService.generateAvroFiles(convertMultipartFile(inputFile));
        new ZipFile(Commons.ZIP_AVRO_FILE).addFolder(new File(Commons.AVRO_DIR));
      }
    } catch (IOException e) {
      log.error("ERROR [{}]", e.getLocalizedMessage());
    }
  }

  private File convertMultipartFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write( file.getBytes());
    fos.close();
    return convFile;
  }
}
