package ca.uhn.fhir.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class Hints implements IResourceProvider {
	private Map<String, Patient> myPatients = new HashMap<String, Patient>();
	private int myNextId = 2;

	/** Constructor */
	public Hints() {
		
	}

	/** Simple implementation of the "read" method */
	@Read(version = false)
	public Patient read(@IdParam IdType theId) {
		
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Patient.class;
	}

	@Create
	public MethodOutcome create(@ResourceParam Patient thePatient) {
		
	}

	@Search
	public List<Patient> search() {
		
	}

	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_FAMILY) StringParam theParam) {
		
	}

}
