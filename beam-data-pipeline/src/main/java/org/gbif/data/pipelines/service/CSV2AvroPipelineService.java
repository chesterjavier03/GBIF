package org.gbif.data.pipelines.service;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.avro.coders.AvroCoder;
import org.apache.beam.sdk.extensions.avro.io.AvroIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.gbif.data.pipelines.io.avro.Observation;
import org.gbif.data.pipelines.utility.Commons;
import org.gbif.data.pipelines.utility.CountryCodeExtractorUtility;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CSV2AvroPipelineService {

  public static void generateAvroFiles(File inputFile) {

    // Setup
    PipelineOptions options = PipelineOptionsFactory.create();
    options.setRunner(DirectRunner.class); // run locally
    Pipeline p = Pipeline.create(options);

    // Instruct Beam to use default Avro serialization instances of Observation
    p.getCoderRegistry().registerCoderForClass(Observation.class, AvroCoder.of(Observation.class));

    // Define the pipeline
    PCollection<String> lines = p.apply("Read CSV", TextIO.read().from(inputFile.getAbsolutePath()));
    PCollection<Observation> observations = lines.apply("Convert to Observations", ParDo.of(lineParser()));
    observations.apply("Write to Avro", AvroIO.write(Observation.class).to(Commons.AVRO_DIR));

    // Execute the pipeline
    p.run().waitUntilFinish();
  }

  private static DoFn<String, Observation> lineParser() {
    return new DoFn<String, Observation>() {
      @ProcessElement
      public void processElement(ProcessContext c) {
        String line = c.element();
        String[] fields = line.split(";"); // file delimitation

        // Please assume perfect data, and no need to check for NPE etc.
        c.output(Observation
            .newBuilder()
            .setOccurrenceID(fields[0])
            .setKingdom(fields[1])
            .setPhylum(fields[2])
            .setClass$(fields[3])
            .setOrder(fields[4])
            .setFamily(fields[5])
            .setGenus(fields[6])
            .setScientificName(fields[7])
            .setDecimalLatitude(Double.parseDouble(fields[8]))
            .setDecimalLongitude(Double.parseDouble(fields[9]))
            .setCountryCode(CountryCodeExtractorUtility.fetchCountryCode(fields[8], fields[9]))
            .build());
      }
    };
  }
}
