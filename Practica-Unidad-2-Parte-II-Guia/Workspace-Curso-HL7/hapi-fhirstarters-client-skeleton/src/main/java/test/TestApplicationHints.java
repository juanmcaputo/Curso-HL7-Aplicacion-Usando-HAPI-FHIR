package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;

public class TestApplicationHints {

	/*
	 * This class contains hints for the tasks outlined in TestApplication
	 */
	
	public static void main(String[] args) {
		//esto deberia ser una clase estatica para que se llame una sola vez por el consumo de recursos
		//FhirContext ctx = FhirContext.forR4();
	    	//IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");
		
		//System.out.println("Listando IDs de observaciones:");
		//listObservationIds();

		// Listar IDs de pacientes
	   	//System.out.println("Listando IDs de pacientes:");
	   	//listPatientIds();
		
		// Punto 1 - A  ok
		//System.out.println("Obteniendo paciente por ID:");
		//getPatientById("patient-id");

		// Punto 1 - B  ok
		//System.out.println("Obteniendo observación por ID:");
		//getObservationById("observation-id");

		// Punto 1 - C  ok
		//System.out.println("Obteniendo pacientes por apellido:");
		//getPatientsByLastName("Perez");

		// Punto 1 - D  ok
		//System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
		//getObservationsByStatusAndPatientLastName("final", "Perez");
		
		//---------------------------------------------------//
		
		// Punto 2 - A  ok
		// Crear un nuevo paciente 
		//System.out.println("Creando un nuevo paciente:");
		//createPatient("John", "Doe", "1980-01-01", "male", "123 Main St", "Anytown", "Anystate", "USA");

		// Punto 2 - B  ok 
		// Modificar la dirección del paciente creado  
		//System.out.println("Modificando la dirección del paciente:");
		//updatePatientAddress("patient-id", "456 Elm St", "Othertown", "Otherstate", "USA");

		// Punto 2 - C  ok
		// Copiar una observación y modificar el ID del paciente
		//System.out.println("Copiando una observación y modificando el ID del paciente:");
		//copyObservationWithNewPatientId("observation-id", "new-patient-id");

		// Punto 2 - D  --->>> DA error
		// Modificar el estado de una observación existente
		//System.out.println("Modificando el estado de una observación existente:");
		//updateObservationStatus("observation-id", "registered");
		//listObservationStatuses();
		//---------------------------------------------------//
		
		// Punto 3 - A  ok 
	    	//System.out.println("Eliminando un paciente por ID:");
	    	//deletePatientById("patient-id1");

	    	// Punto 3 - B  ok
	    	//System.out.println("Eliminando múltiples pacientes por IDs:");
	    	//deletePatientsByIds(new String[]{"patient-id1", "patient-id2"});

	    	// Punto 3 - C ok
	    	//System.out.println("Eliminando una observación por ID:");
	    	//deleteObservationById("observation-id1");

	    	// Punto 3 - D  ok
	    	//System.out.println("Eliminando múltiples observaciones por IDs:");
	    	//deleteObservationsByIds(new String[]{"observation-id1", "observation-id2"});

		
	    	// Punto 3 - E -->> Se pidio que era eliminar la observacion, pero ya se repite, igual este codigo funciona okk  <<<-----
	    	//System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
	    	//deleteObservationsByPatientId("patient-id");
	}

	public static void listObservationIds() {
		
	}

	public static void listPatientIds() {

	}

	//---------------------------------------------------//

	// Punto 1 - A
	public static void getPatientById(String patientId) {

	}

	// Punto 1 - B
	public static void getObservationById(String observationId) {

	}
	
	// Punto 1 - C
	public static void getPatientsByLastName(String lastName) {
	
	}
	
	// Punto 1 - D
	public static void getObservationsByStatusAndPatientLastName(String status, String lastName) {

	}

	//---------------------------------------------------//
	
	// Punto 2 - A
	public static void createPatient(String firstName, String lastName, String birthDate, String gender, String street, String city, String state, String country) {
	
	}

	// Punto 2 - B
	public static void updatePatientAddress(String patientId, String newStreet, String newCity, String newState, String newCountry) {

	}

	// Punto 2 - C
	public static void copyObservationWithNewPatientId(String observationId, String newPatientId) {

	}

	// Punto 2 - D
	public static void updateObservationStatus(String observationId, String newStatus) {

	}

	//---------------------------------------------------//

	// Punto 3 - A
	public static void deletePatientById(String patientId) {

	}
	
	// Punto 3 - B
	public static void deletePatientsByIds(String[] patientIds) {

	}

	// Punto 3 - C
	public static void deleteObservationById(String observationId) {

	}
	
	// Punto 3 - D
	public static void deleteObservationsByIds(String[] observationIds) {

	}

	// Punto 3 - E
	public static void deleteObservationsByPatientId(String patientId) {

	}
}
//---------------------------------------------------//
