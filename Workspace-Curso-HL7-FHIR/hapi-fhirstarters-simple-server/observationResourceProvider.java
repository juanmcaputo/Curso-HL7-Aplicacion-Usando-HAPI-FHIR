package ca.uhn.fhir.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
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
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;


public class observationResourceProvider implements IResourceProvider {
    private Map<String, Observation> myObservations;
    private Map<String, Patient> myPatients;// Referencia a los pacientes existentes
    private int myNextId;

    
    public observationResourceProvider() {
    	myObservations = new HashMap<String, Observation>();
        myPatients = new HashMap<String, Patient>();// Inicializar myPatients
    	myNextId = 1;  	  	
	}
    
    public observationResourceProvider(Map<String, Patient> patients) {
        this.myPatients = patients;
        myObservations = new HashMap<String, Observation>(); // Asegurarse de inicializar myObservations
        myNextId = 1;
    }
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Observation.class;
    }

    @Create
    public MethodOutcome create(@ResourceParam Observation theObservation) {
        // Verificar que el paciente asociado tenga un ID válido (Practica 4 parte 2 punto 5)
        String patientId = theObservation.getSubject().getReferenceElement().getIdPart();
        if (!myPatients.containsKey(patientId)) {
            throw new UnprocessableEntityException("El paciente con ID " + patientId + " no existe.");
        }
        //---//
        int id = myNextId++;
        theObservation.setId(new IdType(id));
        myObservations.put(Integer.toString(id), theObservation);

        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theObservation.getIdElement());
        outcome.setResource(theObservation); 

        return outcome;
    }
    
    @Read
    public Observation read(@IdParam IdType theId) {
        Observation observation = myObservations.get(theId.getIdPart());
        if (observation == null) {
            throw new ResourceNotFoundException("Observation with ID " + theId.getIdPart() + " not found.");
        }
        return observation;
    }
    
    
    @Search
    public List<Observation> search() {
        List<Observation> retVal = new ArrayList<Observation>();
        retVal.addAll(myObservations.values());
        return retVal;
    }

    @Search
    public List<Observation> search(@RequiredParam(name = Observation.SP_SUBJECT) ReferenceParam theParam) {
        List<Observation> retVal = new ArrayList<Observation>();
        for (Observation next : myObservations.values()) {
            String subject = next.getSubject().getReference().toLowerCase();
            if (!subject.contains(theParam.getValue().toLowerCase())) {
                continue;
            }
            retVal.add(next);
        }
        return retVal;
    }

 
    @Update
    public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theObservation) {
        String id = theId.getIdPart();
        if (!myObservations.containsKey(id)) {
            throw new ResourceNotFoundException(theId);
        }
        theObservation.setId(theId);
        myObservations.put(id, theObservation);
        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theObservation.getIdElement());
        outcome.setResource(theObservation);

        return outcome;
    }
    

    @Delete
    public MethodOutcome delete(@IdParam IdType theId) {
        String id = theId.getIdPart();
        if (!myObservations.containsKey(id)) {
            throw new ResourceNotFoundException(theId);
        }
        myObservations.remove(id);

        OperationOutcome operationOutcome = new OperationOutcome();
        operationOutcome.addIssue(
            new OperationOutcome.OperationOutcomeIssueComponent()
                .setSeverity(OperationOutcome.IssueSeverity.INFORMATION)
                .setDiagnostics("Observation with ID " + id + " has been deleted.")
        );

        MethodOutcome outcome = new MethodOutcome();
        outcome.setId(theId);
        outcome.setResource(operationOutcome);
        outcome.setCreated(true); 

        return outcome;
    }
}
