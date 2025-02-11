package ca.uhn.fhir.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;


@WebServlet("/*")
public class Servidor extends RestfulServer {

	//el truco de esto es que java copia objetos, pero los atributos de ese objeto es por referencia
	public static globals global; 
	@Override
	protected void initialize() throws ServletException {
		// Create a context for the appropriate version
		setFhirContext(FhirContext.forR4());
		
		// Register resource providers
		//registerProvider(new Example01_PatientResourceProvider());
	
		registerProvider(new patientResourceProviders(global));
		registerProvider(new observationResourceProvider(global));
		// Format the responses in nice HTML
		//registerInterceptor(new ResponseHighlighterInterceptor());
	}
}
