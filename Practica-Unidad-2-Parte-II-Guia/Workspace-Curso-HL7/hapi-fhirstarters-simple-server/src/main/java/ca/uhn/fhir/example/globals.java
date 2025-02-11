package ca.uhn.fhir.example;

import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.r4.model.Patient;

//El truco es que si bien java copia objetos, los atributos los pasa por referncia..... 

public class globals {
	public static Map<String, Patient> myPatients = new HashMap<String, Patient>();
	public static int myNextPatientID = 2;

}
