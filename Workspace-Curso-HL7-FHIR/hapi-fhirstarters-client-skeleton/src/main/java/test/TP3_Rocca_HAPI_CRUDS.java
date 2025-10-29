package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;

public class TP3_Rocca_HAPI_CRUDS {
    
   static FhirContext ctx = FhirContext.forR4();
   static IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4/");
   public static void main(String[] args) {
	   //Búsqueda de IDs disponibles
	   //System.out.println("Listando IDs de Pacientes:");	  
	   //listPatientIds();
	   //System.out.println("Listando IDs de Observaciones:");
	   //listObservationIds();	  
	  
	   //Punto 1 - A
	   //System.out.println("Obteniendo paciente por ID:");
	   //getPatientById("48206945"); //Luis Riquelme	    
      
	   //Punto 1 - B 
	   //System.out.println("Obteniendo observación por ID:");
	   //getObservationById("10005263"); //Heart rate status final
      
   		// Punto 1 - C  
   		//System.out.println("Obteniendo pacientes por apellido:");
   		//getPatientsByLastName("Perez");
   		
   		// Punto 1 - D  
   		//System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
   		//getObservationsByStatusAndPatientLastName("final", "Perez");
   
	   // Punto 2 - A  
	   // Crear un nuevo paciente 
	   //System.out.println("Creando un nuevo paciente:");
	   //createPatient("John Doe", "Doe", "1980-01-01", "female", "123 Main St", "Anytown", "Anystate", "USA");
	   
	   // Punto 2 - B   
	   //Modificar la dirección del paciente creado  
	   //System.out.println("Modificando la dirección del paciente:");
	   //updatePatientAddress("50717518", "456 Elm St", "Othertown", "Otherstate", "USA");

		// Punto 2 - C  
		// Copiar una observación y modificar el ID del paciente
	   	//System.out.println("Copiando una observación y modificando el ID del paciente:");
		//copyObservationWithNewPatientId("140", "50717518");

		// Punto 2 - D  --->>> DA error
		// Modificar el estado de una observación existente
		//System.out.println("Modificando el estado de una observación existente:");
		//updateObservationStatus("50717876", "preliminary");
	   
		// Punto 3 - A   
	    //System.out.println("Eliminando un paciente por ID:");
	    //deletePatientById("46460231");
	   
	   // Punto 3 - B  
	    //System.out.println("Eliminando múltiples pacientes por IDs:");
	    //deletePatientsByIds(new String[]{"49529634", "49646812"});

	    // Punto 3 - C 
	    //System.out.println("Eliminando una observación por ID:");
	    //deleteObservationById("49649467");

	    // Punto 3 - D  
	    //System.out.println("Eliminando múltiples observaciones por IDs:");
	    //deleteObservationsByIds(new String[]{"6229744", "6229772"});

		
	    // Punto 3 - E -->> Se pidio que era eliminar la observacion, pero ya se repite, igual este codigo funciona k  <<<-----
	    //System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
	    //deleteObservationsByPatientId("126");
   }


   private static void deleteObservationsByPatientId(String id) {
	   Bundle query = client.search().forResource(Observation.class).where(Observation.SUBJECT.hasId(id)).returnBundle(Bundle.class).execute();
	   if(query.getEntry().isEmpty())
		   System.out.println("Paciente " + id + " no enocntrado");
	   else
	   {
		   Resource observation = query.getEntry().get(0).getResource();
		   String obsId= observation.getIdElement().getIdPart();
		   System.out.println("Observation ID" + obsId);
		   deleteObservationById(obsId);
	   }
}


   private static void deleteObservationsByIds(String[] ids) {
	   for(int i = 0; i<ids.length;i++)
		   deleteObservationById(ids[i]);
}


   private static void deleteObservationById(String id) {
	   MethodOutcome response  = client.delete().resourceById("Observation", id).execute();
	   OperationOutcome outcome = (OperationOutcome) response.getOperationOutcome();
	   if (outcome != null) {
		   System.out.println(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getDisplay());
	   }
}


   private static void deletePatientsByIds(String[] ids) {
	   for(int i = 0; i<ids.length;i++)
		   deletePatientById(ids[i]);
}


   private static void deletePatientById(String id) {
	   MethodOutcome response  = client.delete().resourceById("Patient", id).execute();
	   OperationOutcome outcome = (OperationOutcome) response.getOperationOutcome();
	   if (outcome != null) {
		   System.out.println(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getDisplay());
	   }
}
   
   private static void updateObservationStatus(String id, String status) {
	   Observation observation = client.read().resource(Observation.class).withId(id).execute();
	   observation.setStatus(ObservationStatus.fromCode(status));
	   MethodOutcome response  = client.update().resource(observation).execute();
	   OperationOutcome outcome = (OperationOutcome) response.getOperationOutcome();
	   if (outcome == null) {
		   getObservationById(id);
	   }
}
   
   private static void copyObservationWithNewPatientId(String observationId, String patientId) {
	Observation obs = client.read().resource(Observation.class).withId(observationId).execute();
	obs.setSubject(new Reference("Patient/"+patientId));
	MethodOutcome outcome= client.create().resource(obs).prettyPrint().encodedJson().execute();
	getObservationById(outcome.getId().toString());
}
   
   private static void updatePatientAddress(String id, String street, String city, String state,
		String country) {
	   Patient patient = client.read().resource(Patient.class).withId(id).execute();
	   patient.getAddress().clear();
	   patient.addAddress(new Address().addLine(street).setCity(city).setState(state).setCountry(country));
	   MethodOutcome response  = client.update().resource(patient).execute();
	   OperationOutcome outcome = (OperationOutcome) response.getOperationOutcome();
	   if (outcome == null) {
		   getPatientById(id);
	   }
}

   private static void createPatient(String name, String surname, String birthdate, String gender, String street,
		String city, String state, String country) {
	   Patient patient = new Patient();
	   patient.addName().setFamily(surname).addGiven(name);
	   patient.setBirthDateElement(new DateType(birthdate));
	   patient.setGender(Enumerations.AdministrativeGender.fromCode(gender));
	   patient.addAddress().addLine(street).setCity(city).setState(state).setCountry(country);
	   MethodOutcome outcome = client.create().resource(patient).prettyPrint().encodedJson().execute();
	   getPatientById(outcome.getId().toString());
}

   private static void getObservationsByStatusAndPatientLastName(String status, String surname) {
	   Bundle query = client.search().forResource(Observation.class).where(Observation.STATUS.exactly().code(status)).and(Observation.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value(surname))).count(5).returnBundle(Bundle.class).execute();
	   String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(query);
	   System.out.println(string1);	
}

   private static void getPatientsByLastName(String string) {
	Bundle query = client.search().forResource(Patient.class).where(Patient.FAMILY.matches().value(string)).count(5).returnBundle(Bundle.class).execute();
	String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(query);
	System.out.println(string1);
}

   private static void getObservationById(String string) {
	   Observation obs = client.read().resource(Observation.class).withId(string).execute();
	   String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
	   System.out.println(string1);
}

   private static void getPatientById(String string) {
	   Patient patient = client.read().resource(Patient.class).withId(string).execute();
	   String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
	   System.out.println(string1);
   }
 
   private static void listObservationIds() {
	   Bundle query = client.search().forResource(Observation.class).count(5).returnBundle(Bundle.class).execute();
	   String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(query);
	   System.out.println(string1);

}
   private static void listPatientIds() {
	   Bundle query = client.search().forResource(Patient.class).count(5).returnBundle(Bundle.class).execute();
	   String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(query);
	   System.out.println(string1);
}
   
}
