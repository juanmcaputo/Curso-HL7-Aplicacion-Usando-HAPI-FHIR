package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import org.hl7.fhir.r4.model.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HAPI_CRUDS_Meschler {

   private static final FhirContext ctx = FhirContext.forR4();
   private static final IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");

   public static void main(String args[]) {

	// // Punto 1 - A
	//System.out.println("Obteniendo paciente por ID:");
	//Patient patientById = getPatientById("61c1f724-f26b-438d-9025-4e2a62982b1f");

	// // Punto 1 - B
	//System.out.println("Obteniendo observación por ID:");
	//Observation observationById = getObservationById("ec124412-54e0-4082-add9-9c0d75990d7d");

	// // Punto 1 - C
	//System.out.println("Obteniendo pacientes por apellido:");
	//List<Patient> patientsByLastName = getPatientsByLastName("Perez");

	// // Punto 1 - D
	System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
	List<Observation> observationsByStatusAndPatientLastName = getObservationsByStatusAndPatientLastName("final", "Perez");

	// //---------------------------------------------------//

	// // Punto 2 - A
	// // Crear un nuevo paciente
	 //System.out.println("Creando un nuevo paciente:");

	 //Patient nuevoPacienteGenerado = createPatient("Carlos Jose Alejandro", "Paiz Meschler",
	  //  Date.valueOf("1983-11-23"), Enumerations.AdministrativeGender.MALE,
	  //  "Esmeralda 779 piso 9 depto A", "Ciudad Autonoma de Buenos Aires", "Buenos Aires", "Argentina");

	// // Punto 2 - B
	// // Modificar la dirección del paciente creado
	 //System.out.println("Modificando la dirección del paciente:");
	 //updatePatientAddress(nuevoPacienteGenerado.getId(), "Ciudad de la furia 810 piso 8 depto 49", "Otra Ciudad", "Otra Provincia", "Argentina");

	// // Punto 2 - C
	// // Copiar una observación y modificar el ID del paciente
	// System.out.println("Copiando una observación y modificando el ID del paciente:");
	// copyObservationWithNewPatientId(observationById.getId(), nuevoPacienteGenerado.getId());
	 //copyObservationWithNewPatientId("e6afccd7-0ffe-4f21-aecd-ee2675742532", "79765f67-2842-46c7-aaff-eb22effae49");

	// // Punto 2 - D
	// // Modificar el estado de una observación existente
	// updateObservationStatus(observationById.getId(), Observation.ObservationStatus.CANCELLED);

	// //---------------------------------------------------//

	// // Punto 3 - A
	//System.out.println("Eliminando un paciente por ID:");
	// deletePatientById(nuevoPacienteGenerado.getId());
	//deletePatientById("d50ed7e1-a8e8-42d1-9361-ee8618d06614");

	// // Punto 3 - B
	// System.out.println("Eliminando múltiples pacientes por IDs:");
	// deletePatientsByIds(new String[]{nuevoPacienteGenerado.getId()});
	//deletePatientsByIds(new String[]{"d50ed7e1-a8e8-42d1-9361-ee8618d06614","f6a4bab5-d328-4ee0-a934-f79849a7cbe1"});

	// // Punto 3 - C
	// System.out.println("Eliminando una observación por ID:");
	// deleteObservationById(observationById.getId());
	//deleteObservationById("e6afccd7-0ffe-4f21-aecd-ee2675742532");

	// // Punto 3 - D
	// System.out.println("Eliminando múltiples observaciones por IDs:");
	// deleteObservationsByIds(new String[]{observationById.getId()});
	//deleteObservationsByIds(new String[]{"e6afccd7-0ffe-4f21-aecd-ee2675742532","observation-pulserate-example-01"});

	// // Punto 3 - E
    //System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
    //deleteObservationsByPatientId(nuevoPacienteGenerado.getId());
    deleteObservationsByPatientId("patient-example-01");

   }


   private static Patient getPatientById(String id) {
      Patient patientById = client.read()
         .resource(Patient.class)
         .withId(id)
         .execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patientById));
      return patientById;
   }

   private static Observation getObservationById(String id) {
      Observation observationById = client.read()
         .resource(Observation.class)
         .withId(id)
         .execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observationById));
      return observationById;
   }

   private static List<Patient> getPatientsByLastName(String lastName) {
      Bundle patientsByFamilyName = client.search().forResource(Patient.class)
         .where(Patient.FAMILY.matchesExactly().value(lastName))
         .returnBundle(Bundle.class)
         .execute();

      List<Patient> patientsByLastName = patientsByFamilyName.getEntry().stream().map(
         e -> (Patient) e.getResource()).collect(Collectors.toList());

      for (Patient p : patientsByLastName) {
         System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));
      }

      return patientsByLastName;
   }

   private static List<Observation> getObservationsByStatusAndPatientLastName(String status, String family) {
      List<Observation> observations = new ArrayList<>();
      Bundle observationsByStatusAndPatientLastName = client.search().forResource(Observation.class)
         .where(Observation.STATUS.exactly().code(status))
       //Otra opción era .and(Observation.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value(lastName)))
         .and(new ReferenceClientParam("subject").hasId("Patient?family=" + family))          
         .returnBundle(Bundle.class).execute();
      
      //Hubiera sido mucho mejor y más simple, imprimir directamente el bundle y listo... ;)
      //System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observationsByStatusAndPatientLastName));

      for (Bundle.BundleEntryComponent entry : observationsByStatusAndPatientLastName.getEntry()) {
         Resource res = entry.getResource();
         if (res instanceof Observation) {
            Observation obs = (Observation) res;
            System.out.println("ID de Observation: " + obs.getId());
            observations.add((Observation) res);
         } else {
            // Recursos de otro tipo, opcionalmente loguea o ignora
            System.out.println("Recurso no es Observation: " + res.getResourceType());
         }
      }

      return observations;
   }

   private static Patient createPatient(String given, String family, Date birthDate,
                                        Enumerations.AdministrativeGender gender,
                                        String street, String city, String state, String country) {
      Patient nuevoPaciente = new Patient();
      nuevoPaciente.addIdentifier().setSystem("urn:system").setValue("12345");

      nuevoPaciente.addName().setFamily(family).addGiven(given);
      nuevoPaciente.setBirthDate(birthDate);
      nuevoPaciente.setGender(Enumerations.AdministrativeGender.MALE);

      Address direccion = new Address();
      StringType calle = new StringType();
      calle.setValue(street);
      direccion.setLine(java.util.Arrays.asList(calle));
      direccion.setCity(city);
      direccion.setState(state);
      direccion.setCountry(country);
      nuevoPaciente.setAddress(Arrays.asList(direccion));

      MethodOutcome outcome = client.create().resource(nuevoPaciente)
         .prettyPrint().encodedJson().execute();

      System.out.println("El ID del nuevo paciente es: [" + nuevoPaciente.getId() + "].");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(nuevoPaciente));

      return (Patient) outcome.getResource();
   }

   private static void updatePatientAddress(String id, String s, String otraCiudad, String otraProvincia, String argentina) {
      Patient patient = new Patient();
      String[] idPacienteEnPartes = id.split("/");
      String idPacienteBase = idPacienteEnPartes[1];
      System.out.println("[0] : [" + idPacienteEnPartes[0] + "] - [1]: [" + idPacienteEnPartes[1] + "].");
      patient.setId(idPacienteBase);

      Address otraDireccion = new Address();
      StringType otraCalle = new StringType();
      otraCalle.setValue("Tucumán 810 piso 8 depto 49");
      otraDireccion.setLine(java.util.Arrays.asList(otraCalle));
      otraDireccion.setCity("Otra Ciudad");
      otraDireccion.setState("Otra Provincia");
      otraDireccion.setCountry("Argentina");
      patient.setAddress(Arrays.asList(otraDireccion));

      MethodOutcome outcome1 = client.update()
         .resource(patient)
         //.withId(idPacienteBase)
         .execute();

      Patient pacienteActualizado = (Patient) outcome1.getResource();
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteActualizado));
   }

   private static void copyObservationWithNewPatientId(String obsId, String patientId) {

      String[] parts = obsId.split("/");
      String idBaseObs = parts[parts.length - 3]; // última parte del ID

      System.out.println(obsId);
      System.out.println("Se buscará la obs. con el sig. id: [" + idBaseObs + "].");


      Observation observacionExistente = client.read().resource(Observation.class).withId(idBaseObs).execute();
      System.out.println("Se obtuvo la observación obtenida previamente en el punto 1A: " + observacionExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observacionExistente));

      String[] idPacienteEnPartes = patientId.split("/");

      Patient pacienteExistente = client.read().resource(Patient.class).withId(idPacienteEnPartes[1]).execute();
      System.out.print("Se obtuvo el paciente creado en el punto 2A: " + pacienteExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteExistente));

      Observation copiaDeLaObservacion = observacionExistente.copy();
      copiaDeLaObservacion.setSubject(new Reference(idPacienteEnPartes[1]));

      MethodOutcome outcome = client.create().resource(copiaDeLaObservacion).execute();
      Observation copiaDeLaObsGuardada = (Observation) outcome.getResource();

      System.out.println("La observación cambiándole el paciente ahora tiene:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(copiaDeLaObsGuardada));
   }

   private static void updateObservationStatus(String obsId, Observation.ObservationStatus observationStatus) {
      String[] parts = obsId.split("/");
      String idBaseObs = parts[parts.length - 3];
      Observation obsExistente = client.read().resource(Observation.class).withId(obsId).execute();

      System.out.println("Cambiando el estado a la observación anterior. Esa observación tenía el estado ["
         + obsExistente.getStatus() + "].");

      obsExistente.setStatus(observationStatus);

      System.out.println("[0] : [" + parts[parts.length -1] + "] - [1]: [" + parts[parts.length -3] + "].");

      MethodOutcome outcome = client.update()
         .resource(obsExistente)
         .withId(idBaseObs)
         .execute();
      Observation obsExistenteConNuevoEstado = (Observation) outcome.getResource();

      System.out.println("Ahora tiene: ");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obsExistenteConNuevoEstado));
   }

   private static void deletePatientById(String patientId) {
      String[] idPacienteEnPartes = patientId.split("/");
      String idPacienteBase = idPacienteEnPartes[1];
      try {
         System.out.println("Eliminando paciente con id ¨[" + patientId + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Patient", idPacienteBase))
            .execute();
      } catch (Exception e) {
         System.out.println("Error al eliminar el paciente con ID ["
            + idPacienteBase + "]. " + Arrays.toString(e.getStackTrace()));
      }
   }

   private static void deletePatientsByIds(String[] ids) {
      List<String> idsPacientes = Arrays.asList(ids);

      for (String id : idsPacientes) {
         String[] idEnPartes = id.split("/");
         String idBase = idEnPartes[1];
         System.out.println("Eliminando paciente [" + idBase + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Patient", idBase))
            .execute();
      }
   }

   private static void deleteObservationById(String obsId) {
      String[] parts = obsId.split("/");
      String idBaseObs = parts[parts.length - 3]; // última parte del ID
      try {
         System.out.println("Borrando observacion [" + idBaseObs + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Observation", idBaseObs))
            .execute();
      } catch (Exception e) {
         System.out.println("Error al eliminar la observación con ID ["
            + idBaseObs + "]. " + Arrays.toString(e.getStackTrace()));
      }
   }

   private static void deleteObservationsByIds(String[] ids) {
      List<String> idsObservaciones = Arrays.asList(ids);

      for (String id : idsObservaciones) {
         String[] parts = id.split("/");
         String idBase = parts[parts.length - 3];
         System.out.println("Borrando observacion [" + idBase + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Observation", idBase))
            .execute();
      }
   }

   private static void deleteObservationsByPatientId(String patientId) {
      String[] idPacienteEnPartes = patientId.split("/");
      System.out.println("Primero busco las observaciones de ese paciente...");
      Bundle observationsByPatientId = client.search().forResource(Observation.class)
         .where(Observation.SUBJECT.hasId(idPacienteEnPartes[1]))
         .returnBundle(Bundle.class).execute();

      for (Bundle.BundleEntryComponent entry : observationsByPatientId.getEntry()) {
         Resource res = entry.getResource();
         if (res instanceof Observation) {
            Observation obs = (Observation) res;
            System.out.println("ID de Observation: " + obs.getId());

            String obsId = obs.getId();
            // Extraer solo el ID base
            String[] parts = obsId.split("/");
            String idBase = parts[parts.length - 1]; // última parte del ID
            client.delete().resourceById("Observation", idBase).execute();
            System.out.println("Eliminada Observation ID: " + idBase);
         } else {
            // Recursos de otro tipo, opcionalmente loguea o ignora
            System.out.println("Recurso no es Observation: " + res.getResourceType());
         }
      }
   }


}
