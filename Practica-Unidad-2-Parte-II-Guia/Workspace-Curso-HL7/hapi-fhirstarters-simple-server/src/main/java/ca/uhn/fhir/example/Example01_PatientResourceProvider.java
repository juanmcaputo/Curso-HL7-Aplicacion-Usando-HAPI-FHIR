package ca.uhn.fhir.example;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashMap;
import java.util.Map;

public class Example01_PatientResourceProvider implements IResourceProvider {

   private Map<String, Patient> myPatients = new HashMap<String, Patient>();

   /**
    * Constructor
    */
   public Example01_PatientResourceProvider() {
     
   }

   @Override
   public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
   }

   /**
    * Simple implementation of the "read" method
    */
   @Read()
   public Patient read(@IdParam IdType theId) {
      
   }
}
