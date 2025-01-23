package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import java.util.Collections;

import org.hl7.fhir.instance.model.api.IIdType;

public class TestApplicationHints {

	/*
	 * This class contains hints for the tasks outlined in TestApplication
	 */
	
	private static FhirContext ctx;
	private static  IGenericClient client; 
	
	public static void main(String[] args) {
		//esto deberia ser una clase estatica para que se llame una sola vez por el consumo de recursos
		ctx = FhirContext.forR4();
	    client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");
		
		
		//System.out.println("Listando IDs de observaciones:");
		//listObservationIds();

		// Listar IDs de pacientes
	    //System.out.println("Listando IDs de pacientes:");
	    //listPatientIds();
		
		// Punto 1 - A  ok
		//System.out.println("Obteniendo paciente por ID:");
		//getPatientById("patient-id");	    

		// Punto 1 - B  ok
		//System.out.println("Obteniendo observaci�n por ID:");
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
		// Modificar la direcci�n del paciente creado  
		//System.out.println("Modificando la direcci�n del paciente:");
		//updatePatientAddress("patient-id", "456 Elm St", "Othertown", "Otherstate", "USA");

		// Punto 2 - C  ok
		// Copiar una observaci�n y modificar el ID del paciente
		//System.out.println("Copiando una observaci�n y modificando el ID del paciente:");
		//copyObservationWithNewPatientId("a2179bca-9c7e-4f1c-9125-c7401c253c14", "new-patient-id");

		// Punto 2 - D  --->>> DA error
		// Modificar el estado de una observaci�n existente
		//System.out.println("Modificando el estado de una observaci�n existente:");
		//updateObservationStatus("a2179bca-9c7e-4f1c-9125-c7401c253c14", "final");
		//listObservationStatuses();
		//---------------------------------------------------//
		
		// Punto 3 - A  ok 
	    //System.out.println("Eliminando un paciente por ID:");
	    //deletePatientById("patient-id1");

	    // Punto 3 - B  ok
	    //System.out.println("Eliminando m�ltiples pacientes por IDs:");
	    //deletePatientsByIds(new String[]{"patient-id1", "patient-id2"});

	    // Punto 3 - C ok
	    //System.out.println("Eliminando una observaci�n por ID:");
	    //deleteObservationById("observation-id1");

	    // Punto 3 - D  ok
	    //System.out.println("Eliminando m�ltiples observaciones por IDs:");
	    //deleteObservationsByIds(new String[]{"observation-id1", "observation-id2"});

		
	    // Punto 3 - E -->> Se pidio que era eliminar la observacion, pero ya se repite, igual este codigo funciona okk  <<<-----
	    //System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
	    //deleteObservationsByPatientId("patient-id");
		
	}

	//---------------------------------------------------//
	
	// esta funcion hay que eliminarla porque es de prueba del punto 2 - D
	public static void listObservationStatuses() {
	    
	    Bundle results = client
	        .search()
	        .forResource(Observation.class)
	        .returnBundle(Bundle.class)
	        .execute();

	    for (Bundle.BundleEntryComponent entry : results.getEntry()) {
	        Observation observation = (Observation) entry.getResource();
	        String status = observation.getStatus().toCode(); 
	        System.out.println("Observation ID: " + observation.getIdElement().getIdPart() + ", Status: " + status);
	    }
	}
	/////////////

	public static void listObservationIds() {
		org.hl7.fhir.r4.model.Bundle results = client
			.search()
			.forResource(Observation.class)
			.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
			.execute();

		for (Bundle.BundleEntryComponent entry : results.getEntry()) {
			Observation observation = (Observation) entry.getResource();
			System.out.println("Observation ID: " + observation.getIdElement().getIdPart());
		}
	}

	public static void listPatientIds() {
	    org.hl7.fhir.r4.model.Bundle results = client
	        .search()
	        .forResource(Patient.class)
	        .returnBundle(org.hl7.fhir.r4.model.Bundle.class)
	        .execute();

	    for (Bundle.BundleEntryComponent entry : results.getEntry()) {
	        Patient patient = (Patient) entry.getResource();
	        System.out.println("Patient ID: " + patient.getIdElement().getIdPart());
	    }
	}


	// Punto 1 - A
	public static void getPatientById(String patientId) {
		Patient patient;
		try {
			patient = client.read().resource(Patient.class).withId(patientId).execute();
		} catch (ResourceNotFoundException e) {
			System.out.println("Resource not found!");
			return;
		}

		String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		System.out.println(string);
	}

	// Punto 1 - B
	public static void getObservationById(String observationId) {
		Observation observation;
		try {
			observation = client.read().resource(Observation.class).withId(observationId).execute();
		} catch (ResourceNotFoundException e) {
			System.out.println("Resource not found!");
			return;
		}

		String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observation);
		System.out.println(string);
	}

	// Punto 1 - C
	public static void getPatientsByLastName(String lastName) {
		org.hl7.fhir.r4.model.Bundle results = client
			.search()
			.forResource(Patient.class)
			.where(Patient.FAMILY.matches().value(lastName))
			.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
			.execute();

		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(results));
	}
		
	    // Punto 1 - D
	    public static void getObservationsByStatusAndPatientLastName(String status, String lastName) {
		org.hl7.fhir.r4.model.Bundle results = client
			.search()
			.forResource(Observation.class)
			.where(Observation.STATUS.exactly().code(status))
			.and(Observation.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value(lastName)))
			.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
			.execute();

		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(results));
	}

	//---------------------------------------------------//

	// Punto 2 - A
	public static void createPatient(String firstName, String lastName, String birthDate, String gender, String street, String city, String state, String country) {
		Patient patient = new Patient();
		patient.addName().setFamily(lastName).addGiven(firstName);
		patient.setBirthDateElement(new DateType(birthDate));
		patient.setGender(Enumerations.AdministrativeGender.fromCode(gender));
		patient.addAddress().addLine(street).setCity(city).setState(state).setCountry(country);

		MethodOutcome outcome = client.create().resource(patient).execute();
		IdType id = (IdType) outcome.getId();

		Patient createdPatient = client.read().resource(Patient.class).withId(id).execute();
		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdPatient));
	}

	// Punto 2 - B
	public static void updatePatientAddress(String patientId, String newStreet, String newCity, String newState, String newCountry) {
		Patient patient = client.read().resource(Patient.class).withId(patientId).execute();
		patient.getAddressFirstRep().setLine(Collections.singletonList(new StringType(newStreet)))
				.setCity(newCity)
				.setState(newState)
				.setCountry(newCountry);

		client.update().resource(patient).execute();

		Patient updatedPatient = client.read().resource(Patient.class).withId(patientId).execute();
		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedPatient));
	}

	// Punto 2 - C
	public static void copyObservationWithNewPatientId(String observationId, String newPatientId) {
		Observation observation = client.read().resource(Observation.class).withId(observationId).execute();
		observation.setSubject(new Reference("Patient/" + newPatientId));
		observation.setId(IdType.newRandomUuid());

		MethodOutcome outcome = client.create().resource(observation).execute();
		IdType id = (IdType) outcome.getId();

		Observation createdObservation = client.read().resource(Observation.class).withId(id).execute();
		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObservation));
	}

	// Punto 2 - D
	public static void updateObservationStatus(String observationId, String newStatus) {
		Observation observation = client.read().resource(Observation.class).withId(observationId).execute();
		observation.setStatus(Observation.ObservationStatus.fromCode(newStatus));

		client.update().resource(observation).withId(observationId).execute();
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		Observation updatedObservation = client.read().resource(Observation.class).withId(observationId).execute();
		System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedObservation));
	}

	//---------------------------------------------------//

	// Punto 3 - A
	public static void deletePatientById(String patientId) {
		client.delete().resourceById(new IdType("Patient", patientId)).execute();
		System.out.println("Patient with ID " + patientId + " has been deleted.");
	}

	// Punto 3 - B
	public static void deletePatientsByIds(String[] patientIds) {
		for (String patientId : patientIds) {
			client.delete().resourceById(new IdType("Patient", patientId)).execute();
			System.out.println("Patient with ID " + patientId + " has been deleted.");
		}
	}

	// Punto 3 - C
	public static void deleteObservationById(String observationId) {
		client.delete().resourceById(new IdType("Observation", observationId)).execute();
		System.out.println("Observation with ID " + observationId + " has been deleted.");
	}

	// Punto 3 - D
	public static void deleteObservationsByIds(String[] observationIds) {

		for (String observationId : observationIds) {
			client.delete().resourceById(new IdType("Observation", observationId)).execute();
			System.out.println("Observation with ID " + observationId + " has been deleted.");
		}
	}

	// Punto 3 - E
	public static void deleteObservationsByPatientId(String patientId) {
		org.hl7.fhir.r4.model.Bundle results = client
			.search()
			.forResource(Observation.class)
			.where(Observation.SUBJECT.hasId(patientId))
			.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
			.execute();

		for (Bundle.BundleEntryComponent entry : results.getEntry()) {
			Observation observation = (Observation) entry.getResource();
			client.delete().resourceById(observation.getIdElement()).execute();
			System.out.println("Observation with ID " + observation.getIdElement().getIdPart() + " associated with Patient ID " + patientId + " has been deleted.");
		}
	}

	//---------------------------------------------------//













	}
	