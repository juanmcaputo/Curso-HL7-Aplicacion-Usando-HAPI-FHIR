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
		ctx = FhirContext.forR4();
		client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");
		
		// Listar IDs de observaciones
		//System.out.println("Listando IDs de observaciones:");
		//listObservationIds();

		// Listar IDs de pacientes
	    //System.out.println("Listando IDs de pacientes:");
	    //listPatientIds();
		
		// Punto 1 - A 
		//System.out.println("Obteniendo paciente por ID:");
		//getPatientById("patient-id");

		// Punto 1 - B 
		//System.out.println("Obteniendo observación por ID:");
		//getObservationById("observation-id");

		// Punto 1 - C  
		//System.out.println("Obteniendo pacientes por apellido:");
		//getPatientsByLastName("Perez");

		// Punto 1 - D  
		//System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
		//getObservationsByStatusAndPatientLastName("final", "Perez");
		
		//---------------------------------------------------//
		
		// Punto 2 - A  
		// Crear un nuevo paciente 
		//System.out.println("Creando un nuevo paciente:");
		//createPatient("John", "Doe", "1980-01-01", "male", "123 Main St", "Anytown", "Anystate", "USA");

		// Punto 2 - B   
		// Modificar la dirección del paciente creado  
		//System.out.println("Modificando la dirección del paciente:");
		//updatePatientAddress("patient-id", "456 Elm St", "Othertown", "Otherstate", "USA");

		// Punto 2 - C  
		// Copiar una observación y modificar el ID del paciente
		//System.out.println("Copiando una observación y modificando el ID del paciente:");
		//copyObservationWithNewPatientId("observation-id", "new-patient-id");

		// Punto 2 - D  
		// Modificar el estado de una observación existente
		//System.out.println("Modificando el estado de una observación existente:");
		//updateObservationStatus("observation-id", "registered");

		//---------------------------------------------------//
		
		// Punto 3 - A  
	    //System.out.println("Eliminando un paciente por ID:");
	    //deletePatientById("patient-id");

	    // Punto 3 - B  
	    //System.out.println("Eliminando múltiples pacientes por IDs:");
	    //deletePatientsByIds(new String[]{"patient-id1", "patient-id2"});

	    // Punto 3 - C 
	    //System.out.println("Eliminando una observación por ID:");
	    //deleteObservationById("observation-id1");

	    // Punto 3 - D 
	    //System.out.println("Eliminando múltiples observaciones por IDs:");
	    //deleteObservationsByIds(new String[]{"observation-id1", "observation-id2"});

		
	    // Punto 3 - E 
	    //System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
	    //deleteObservationsByPatientId("patient-id");
		
	}

	//---------------------------------------------------//
	
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

		client.update().resource(observation).execute();
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
	