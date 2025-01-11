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
//		step1_read_a_resource();
//		step2_search_for_patients_named_test();
//		step3_create_patient();
		
//		String PatientID = "1410a913-e8fc-4084-9cf4-7ec0a4a144c1";
		//String NewName = "Latina";
		//step4_update_patient(PatientID, NewName);
//		step5_delete_a_resource(PatientID);
	}

	public static void step1_read_a_resource() {

	}

	public static void step2_search_for_patients_named_test() {

	}
	
	public static void step3_create_patient() {
	
	}
	
	public static void step4_update_patient(String PatiendID,String AddedName) {		

	}
	
	public static void step5_delete_a_resource(String PatientID) {
	
	}
}
