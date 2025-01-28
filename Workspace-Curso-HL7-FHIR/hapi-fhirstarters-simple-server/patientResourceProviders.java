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
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class patientResourceProviders implements IResourceProvider {
	private Map<String, Patient> myPatients = new HashMap<String, Patient>();
	private int myNextId = 2;

	/** Constructor */
	public patientResourceProviders() {
		Patient pat1 = new Patient();
		pat1.setId("1");
		pat1.addIdentifier().setSystem("http://acme.com/MRNs").setValue("7000135");
		pat1.addName().setFamily("Simpson").addGiven("Homer").addGiven("J");
		myPatients.put("1", pat1);
	}


	@Read(version = false)
	public Patient read(@IdParam IdType theId) {
		Patient retVal = myPatients.get(theId.getIdPart());
		if (retVal == null) {
			throw new ResourceNotFoundException(theId);
		}
		return retVal;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Patient.class;
	}

	@Create
	public MethodOutcome create(@ResourceParam Patient thePatient) {
		int id = myNextId++;
		thePatient.setId(new IdType(id));
		myPatients.put(Integer.toString(id), thePatient);

		return new MethodOutcome().setId(thePatient.getIdElement());
	}

	@Search
	public List<Patient> search() {
		List<Patient> retVal = new ArrayList<Patient>();
		retVal.addAll(myPatients.values());
		return retVal;
	}

	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_FAMILY) StringParam theParam) {
		List<Patient> retVal = new ArrayList<Patient>();

		for (Patient next : myPatients.values()) {
			String familyName = next.getNameFirstRep().getFamily().toLowerCase();
			if (familyName.contains(theParam.getValue().toLowerCase()) == false) {
				continue;
			}
			retVal.add(next);
		}

		return retVal;
	}

	// Practica 4 parte 2 punto 3 - a
 	@Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
        String id = theId.getIdPart();
        if (!myPatients.containsKey(id)) {
            throw new ResourceNotFoundException(theId);
        }
        thePatient.setId(theId);
        myPatients.put(id, thePatient);
        return new MethodOutcome().setId(thePatient.getIdElement());
    }
 	
 	// Practica 4 parte 2 punto 3 - b
    @Delete
    public void delete(@IdParam IdType theId) {
        String id = theId.getIdPart();
        if (!myPatients.containsKey(id)) {
            throw new ResourceNotFoundException(theId);
        }
        myPatients.remove(id);
    }
}
