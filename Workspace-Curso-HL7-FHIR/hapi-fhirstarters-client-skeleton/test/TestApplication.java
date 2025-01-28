package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Patient;

public class TestApplication {

   /**
    * This is the Java main method, which gets executed
    */
   public static void main(String[] args) {

      // Create a context
      FhirContext ctx = FhirContext.forR4();

      // Create a client
      IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");

      // Read a patient with the given ID
      Patient patient = client.read().resource(Patient.class).withId("033369db-bd0a-43f9-92a3-f5d34f98e540").execute();

      // Print the output
      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
      System.out.println(string);

   }

}
