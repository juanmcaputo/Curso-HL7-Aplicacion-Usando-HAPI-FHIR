package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Bundle;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import java.util.Date;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import ca.uhn.fhir.rest.api.MethodOutcome;
import java.text.SimpleDateFormat;
import java.text.ParseException;



public class HAPI_CRUDS_SPERI {

   /**
    * This is the Java main method, which gets executed
    */
	// Create a context
    FhirContext ctx = FhirContext.forR4();

    // Create a client
    IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4/");	
	
	
   public static void main(String[] args) {

      

      //System.out.println("Listando IDs de observaciones:");
		//listObservationIds();

		// Listar IDs de pacientes
	   // System.out.println("Listando IDs de pacientes:");
	   // listPatientIds();
		
	   HAPI_CRUDS_SPERI service = new HAPI_CRUDS_SPERI();  // crear instancia
		// Punto 1 - A  
		//System.out.println("Obteniendo paciente por ID:");
		//service.getPatientById("7058334");	    

		// Punto 1 - B  
		//System.out.println("Obteniendo observación por ID:");
		//service.getObservationById("d6c03b4c-a421-4395-a127-334d5021b686");

		// Punto 1 - C  
		//System.out.println("Obteniendo pacientes por apellido:");
		//service.getPatientsByLastName("Perez");

		// Punto 1 - D  --> No funciona el _has, queda solo buscando por estado
		//System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
		//service.getObservationsByStatusAndPatientLastName("final", "Perez");
		
		//---------------------------------------------------//
		
		// Punto 2 - A  
		// Crear un nuevo paciente 
		//System.out.println("Creando un nuevo paciente:");
		//String id_nuevo = service.createPatient("John", "Peponcho2", "1980-01-01", "male", "123 Main St", "Anytown", "Anystate", "USA");

		// Punto 2 - B   
		// Modificar la dirección del paciente creado  
		//System.out.println("Modificando la dirección del paciente:");
		//service.updatePatientAddress("50730173", "Calle falsa 123", "Othertown", "Otherstate", "USA");

		// Punto 2 - C  
		// Copiar una observación y modificar el ID del paciente
		//System.out.println("Copiando una observación y modificando el ID del paciente:");
		//service.copyObservationWithNewPatientId("148", "50730173");

		// Punto 2 - D   --> Da error cuando la referencia del subject es urn:uuid:...
		// Modificar el estado de una observación existente
		//System.out.println("Modificando el estado de una observación existente:");
		//service.updateObservationStatus("148", "preliminary");
		
		//listObservationStatuses();
		//---------------------------------------------------//
		
		// Punto 3 - A   
	   // System.out.println("Eliminando un paciente por ID:");
	    //service.deletePatientById("46460233");

	    // Punto 3 - B  
	    //System.out.println("Eliminando múltiples pacientes por IDs:");
	    //service.deletePatientsByIds(new String[]{"46460235", "46460237"});

	    // Punto 3 - C 
	    //System.out.println("Eliminando una observación por ID:");
	    //service.deleteObservationById("6704886");

	    // Punto 3 - D  
	   // System.out.println("Eliminando múltiples observaciones por IDs:");
	   // service.deleteObservationsByIds(new String[]{"6229803", "6460124"});

		
	    // Punto 3 - E 
	    System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
	    service.deleteObservationsByPatientId("1168785");

   }
   public void getPatientById(String pID) {
	   Patient patient = client.read().resource(Patient.class).withId(pID).execute();
	   // Print the output
	      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
	      System.out.println(string);
	}

   public void getObservationById(String oID){
	   Observation obs = client.read().resource(Observation.class).withId(oID).execute();
	   // Print the output
	      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
	      System.out.println(string);
	}
   
   public void getPatientsByLastName(String last_name){
	   Bundle result = client.search()
			   .forResource(Patient.class)
			   .where(Patient.FAMILY.matches().values(last_name))
			   .returnBundle(Bundle.class)
			   .execute();

	// Print the output
	      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result);
	      System.out.println(string);
	}
   
   public void getObservationsByStatusAndPatientLastName(String estado, String last_name) {
	     
	   Bundle result = client.search()
		        .forResource(Observation.class)
		        .where(Observation.STATUS.exactly().code(estado))
		        // .and(Observation._has:Patient:subject:family.matches().value(last_name)))
		        //.and(new StringClientParam("_has:Patient:subject:family").matches().value(last_name))
		        .returnBundle(Bundle.class)
		        .execute();

	// Print the output
	      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result);
	      System.out.println(string);
   		}

//---------------------------------------------------//

	public String createPatient(String nombre, String apellido, String birthdate, String sexo, String calle, String ciudad, String provincia, String pais) {
	
		Patient nuevo_paciente = new Patient();
		
		// Asigna nombre, apellido y fecha de nacimiento
		nuevo_paciente.addName().setFamily(apellido).addGiven(nombre);
		
		// Parsear fecha desde String
	    try {
	        Date bd = new SimpleDateFormat("yyyy-MM-dd").parse(birthdate);
	        nuevo_paciente.setBirthDate(bd);
	    } catch (ParseException e) {
	        System.out.println("Formato de fecha inválido. Formato esperado: yyyy-MM-dd");
	        e.printStackTrace();
	    }
		
		// Asigna sexo
		switch (sexo) {
		
		case "male": 
			nuevo_paciente.setGender(AdministrativeGender.MALE);
			break;
		case "female": 
			nuevo_paciente.setGender(AdministrativeGender.FEMALE);
			break;
		default:
			nuevo_paciente.setGender(AdministrativeGender.UNKNOWN);
            break;
		
		}
		
		// Asigna dirección
		Address direccion = new Address();
	    direccion.addLine(calle);
	    direccion.setCity(ciudad);
	    direccion.setState(provincia);
	    direccion.setCountry(pais);
	    nuevo_paciente.addAddress(direccion);
		
	    // Creación
	    MethodOutcome outcome = client.create()
                .resource(nuevo_paciente)
                .prettyPrint()
                .encodedJson()
                .execute();
	
	 // Obtener ID del recurso creado
	    String id = outcome.getId().getIdPart();
	    
	 // Imprimir en consola
	    System.out.println("Recurso completo:");
	    System.out.println(client.getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(nuevo_paciente));
	    
	    return id;
		
	}

	public void updatePatientAddress(String pID, String calle, String ciudad, String provincia, String pais) {
	
		 Patient patient = client.read().resource(Patient.class).withId(pID).execute();
		 
		// Asigna dirección
			Address direccion = new Address();
		    direccion.addLine(calle);
		    direccion.setCity(ciudad);
		    direccion.setState(provincia);
		    direccion.setCountry(pais);
		 
		 patient.addAddress(direccion);
		 
		 MethodOutcome outcome = client.update()
		            .resource(patient)
		            .prettyPrint()
		            .encodedJson()
		            .execute();
		 
		// Resultado
		    System.out.println("Dirección actualizada");
		    System.out.println(client.getFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
	}
	
	public void copyObservationWithNewPatientId(String obs_id, String pat_id) {
		
		Observation obs = client.read().resource(Observation.class).withId(obs_id).execute();

	    // Si observation fue encontrado
		if (obs != null) {
	        
	    	// Nueva instancia copia la original
	    	Observation newObservation = obs.copy();
	    	newObservation.setId((String) null);
	    	
	    	// Asigna paciente
	    	newObservation.setSubject(new Reference("Patient/" + pat_id));
	    	
	    	MethodOutcome outcome = client
	    	        .create()
	    	        .resource(newObservation)
	    	        .execute();

	    	    System.out.println("Nueva observación creada con ID: " +
	    	        outcome.getId().getIdPart());
	    	
	    }
	}
	
	public void updateObservationStatus(String obs_id, String status) {
		
		Observation obs = client.read().resource(Observation.class).withId(obs_id).execute();
		
		if (obs != null) {
			obs.setStatus(Observation.ObservationStatus.fromCode(status));
			
			MethodOutcome outcome = client
			        .update()
			        .resource(obs)
			        .execute();
			

			// Print the output
			      String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
			      System.out.println(string);
			
		}
	}
	
	// ----------------------------------------------------------------- // 
    
	public void deletePatientById(String pID) {
	
		MethodOutcome outcome = client
	            .delete()
	            .resourceById(new IdType("Patient", pID))
	            .execute();

	        System.out.println("Paciente eliminado correctamente. ID: " + pID);
	}

	public void deletePatientsByIds(String[] ids_paciente){
		for (String pID : ids_paciente) {
	        try {
	        	MethodOutcome outcome = client
	    	            .delete()
	    	            .resourceById(new IdType("Patient", pID))
	    	            .execute();

	    	        System.out.println("Paciente eliminado correctamente. ID: " + pID);
	        } catch (Exception e) {
	            System.out.println("Error al eliminar paciente con ID " + pID + ": " + e.getMessage());
	        }
	    }
		
	}

	public void deleteObservationById(String oID) {
		
		MethodOutcome outcome = client
	            .delete()
	            .resourceById(new IdType("Observation", oID))
	            .execute();

	        System.out.println("Observacion eliminada correctamente. ID: " + oID);
	}

	public void deleteObservationsByIds(String[] ids_obs){
		for (String oID : ids_obs) {
	        try {
	        	MethodOutcome outcome = client
	    	            .delete()
	    	            .resourceById(new IdType("Observation", oID))
	    	            .execute();

	    	        System.out.println("Observacion eliminada correctamente. ID: " + oID);
	        } catch (Exception e) {
	            System.out.println("Error al eliminar observcion con ID " + oID + ": " + e.getMessage());
	        }
	    }
		
	}
	
	public void deleteObservationsByPatientId(String pID) {
	    // Buscar todas las Observations asociadas al paciente
	    Bundle observations = client
	        .search()
	        .forResource(Observation.class)
	        .where(Observation.SUBJECT.hasId(pID))
	        .returnBundle(Bundle.class)
	        .execute();

	    if (observations.getEntry().isEmpty()) {
	        System.out.println("No se encontraron Observations para el paciente con ID: " + pID);
	        return;
	    }

	    // Eliminar cada Observation encontrada
	    for (Bundle.BundleEntryComponent entry : observations.getEntry()) {
	        Observation obs = (Observation) entry.getResource();
	        String obsId = obs.getIdElement().getIdPart();

	        try {
	            client.delete().resourceById(new IdType("Observation", obsId)).execute();
	            System.out.println("Observation eliminada: " + obsId);
	        } catch (Exception e) {
	            System.out.println("Error al eliminar Observation " + obsId + ": " + e.getMessage());
	        }
	    }
	}
}


