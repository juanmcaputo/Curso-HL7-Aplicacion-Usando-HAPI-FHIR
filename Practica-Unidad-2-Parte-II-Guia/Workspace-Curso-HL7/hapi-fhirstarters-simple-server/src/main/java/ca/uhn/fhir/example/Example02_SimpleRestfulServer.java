package ca.uhn.fhir.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

@WebServlet("/*")
public class Example02_SimpleRestfulServer extends RestfulServer {

	@Override
	protected void initialize() throws ServletException {
		
	}
}
