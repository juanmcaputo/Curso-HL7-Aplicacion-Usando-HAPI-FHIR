package ca.uhn.fhir.example;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashMap;
import java.util.Map;


public class Example04_PatientResourceProviderCompleto implements IResourceProvider {

   private Map<String, Patient> myPatients = new HashMap<String, Patient>();
   private int Contador;

   /**
    * Constructor
    */
   public Example04_PatientResourceProviderCompleto() {
	  
   }

   @Override
   public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
   }

   
   @Create()
   public MethodOutcome createPatient(@ResourceParam Patient thePatient) {

   }
   
   /**
    * Simple implementation of the "read" method
    */
   @Read(version = false)
   public Patient ReadPatientByID(@IdParam IdType theId) {
	   
   }
   
   @Update()
   public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient) {
      
   }
@Delete()
public void deletePatient(@IdParam IdType theId) {

}
