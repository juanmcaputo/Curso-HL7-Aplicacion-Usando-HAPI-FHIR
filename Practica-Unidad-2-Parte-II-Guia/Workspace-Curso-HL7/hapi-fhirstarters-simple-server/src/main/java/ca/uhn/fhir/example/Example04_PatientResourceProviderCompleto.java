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
	   
	  //It creat some of the things needed after 
      Patient pat1 = new Patient();
      pat1.setId("1");
      pat1.addIdentifier().setSystem("http://acme.com/MRNs").setValue("7000135");
      pat1.addName().setFamily("Simpson").addGiven("Homer").addGiven("J");
      myPatients.put("1", pat1);
      Contador = 1;
   }

   @Override
   public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
   }

   
   @Create()
   public MethodOutcome createPatient(@ResourceParam Patient thePatient) {



      // Save this patient to the database...
      Contador++;
      String StringContador = String.valueOf(Contador);
      thePatient.setId(String.valueOf(Contador));
      myPatients.put(StringContador,thePatient);
      //savePatientToDatabase(thePatient);

      // This method returns a MethodOutcome object which contains
      // the ID (composed of the type Patient, the logical ID 3746, and the
      // version ID 1)
      MethodOutcome retVal = new MethodOutcome();
      retVal.setResource(thePatient);
      //retVal.setId(new IdType("Patient", "3746", "1"));

      // You can also add an OperationOutcome resource to return

      return retVal;
   }
   
   /**
    * Simple implementation of the "read" method
    */
   @Read(version = false)
   public Patient ReadPatientByID(@IdParam IdType theId) {
      
	  Patient retVal = myPatients.get(theId.getIdPart());
      if (retVal == null) {
         throw new ResourceNotFoundException(theId);
      }
	   
	   
	   /*Patient pat1 = new Patient();
	   pat1.setId("1");
	   pat1.addIdentifier().setSystem("http://acme.com/MRNs").setValue("7000135");
	   pat1.addName().setFamily("Simpson").addGiven("Homer").addGiven("J");*/
	   //return pat1;
	   //   myPatients.put("1", pat1);
      return retVal;
	   
   }
   
  @Update()
   public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient) {
      String resourceId = theId.getIdPart();                        
      MethodOutcome retVal = new MethodOutcome();
      // ... I delete the old patient      
      myPatients.remove(resourceId);  
      myPatients.put(resourceId,thePatient);
      
      
      retVal.setResource(thePatient);
      
      return retVal;
      
   }
@Delete()
public void deletePatient(@IdParam IdType theId) {
	   // .. Delete the patient ..
	String resourceId = theId.getIdPart(); 
    MethodOutcome retVal = new MethodOutcome();
    // ... I delete the old patient      
    myPatients.remove(resourceId); 
	   // otherwise, delete was successful
	   return; // can also return MethodOutcome
	}

}
