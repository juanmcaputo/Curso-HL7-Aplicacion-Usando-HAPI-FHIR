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

public class HAPI_CRUDS_Meschler_06_noviembre {

   private static final FhirContext ctx = FhirContext.forR4();
   private static final IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");

   public static void main(String args[]) {

      // Punto 1 - A
      //System.out.println("Obteniendo paciente por ID:");
      //getPatientById("6d7ece9b-6a37-4b53-a6b1-1cc71c1ca44e");

      // Punto 1 - B
      //System.out.println("Obteniendo observación por ID:");
      //Observation observationById = getObservationById("cb12e213-bf97-4696-a99e-74ac3727083a");

      // Punto 1 - C
      // System.out.println("Obteniendo pacientes por apellido:");
      // Bundle patientsByLastName = getPatientsByLastName("Perez");

      // Punto 1 - D
      //System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
      //Bundle observationsByStatusAndPatientLastName = getObservationsByStatusAndPatientLastName("final", "Perez");

      //---------------------------------------------------//

      // Punto 2 - A
      // Crear un nuevo paciente
      //System.out.println("Creando un nuevo paciente:");
      // Patient nuevoPacienteGenerado = createPatient();

      // Punto 2 - B
      // Modificar la dirección del paciente creado
      //System.out.println("Modificando la dirección del paciente:");
      //updatePatientAddress("16353d06-399a-470c-a586-c8f2f7638fe3");

      // Punto 2 - C
      // Copiar una observación y modificar el ID del paciente
      //System.out.println("Copiando una observación y modificando el ID del paciente:");
      //copyObservationWithNewPatientId("16353d06-399a-470c-a586-c8f2f7638fe3", "7ff8a383-a92b-4600-adbe-d9c5acb763bd");

      // Punto 2 - D
      //Modificar el estado de una observación existente
      //updateObservationStatus("962a6c7d-fa3a-4d66-b88f-627319c0202e");

      //---------------------------------------------------//

      // Punto 3 - A
      //System.out.println("Eliminando un paciente por ID:");
      // deletePatientById("d190305b-9cf7-4843-83b0-44e1de3a2971");

      // Punto 3 - B
      //System.out.println("Eliminando múltiples pacientes por IDs:");
      //deletePatientsByIds(new String[]{"9ee454d6-df5b-4ca6-9c96-21e605bcab7e"});

      // Punto 3 - C
      //System.out.println("Eliminando una observación por ID:");
      //deleteObservationById("962a6c7d-fa3a-4d66-b88f-627319c0202e");

      // Punto 3 - D
      //System.out.println("Eliminando múltiples observaciones por IDs:");
      //deleteObservationsByIds(new String[]{"6e6ac123-e9c3-4073-b5fb-e06d0b05d9f6"});

      // Punto 3 - E
      System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
      deleteObservationsByPatientId("16353d06-399a-470c-a586-c8f2f7638fe3");
   }

   private static void getPatientById(String patientId) {
      Patient patient = client.read()
         .resource(Patient.class)
         .withId(patientId)
         .execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
   }

   private static Observation getObservationById(String id) {
      Observation observationById = client.read()
         .resource(Observation.class)
         .withId(id)
         .execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observationById));
      return observationById;
   }

   private static Bundle getPatientsByLastName(String lastName) {
      Bundle patientsByFamilyName = client.search().forResource(Patient.class)
         .where(Patient.FAMILY.matchesExactly().value(lastName))
         .returnBundle(Bundle.class)
         .execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patientsByFamilyName));

      return patientsByFamilyName;
   }

   private static Bundle getObservationsByStatusAndPatientLastName(String status, String family) {
      // String url = "/Observation?status=" + status + "&subject.family=" + family;

      Bundle bundleObservaciones = client.search().forResource(Observation.class).where(Observation.STATUS.exactly().code(status))
            .and(Observation.SUBJECT.hasChainedProperty(Patient.FAMILY.matchesExactly().value(family)))
            .returnBundle(Bundle.class).execute();

      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundleObservaciones));

      return bundleObservaciones;
   }

   private static Patient createPatient() {
      String given = "Carlos Jose Alejandro";
      String family = "Paiz Meschler";
      Date birthDate = Date.valueOf("1983-11-23");
      Enumerations.AdministrativeGender gender = Enumerations.AdministrativeGender.MALE;
      String street = "Esmeralda 779 piso 9 depto A";
      String city = "Ciudad Autonoma de Buenos Aires";
      String state = "Buenos Aires";
      String country = "Argentina";

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

      IdType id = (IdType) outcome.getId();

      System.out.println("El ID del nuevo paciente es: [" + id + "].");
      // Uso el ID obtenido al crear el paciente para buscarlo

      Patient patient = client.read()
         .resource(Patient.class)
         .withId(id)
         .execute();

      System.out.println("El paciente traido desde el servidor es:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

      return (Patient) outcome.getResource();
   }

   private static void updatePatientAddress(String patientId) {

      Patient patient = client.read()
         .resource(Patient.class)
         .withId(patientId)
         .execute();

      System.out.println("Paciente obtenido del servidor:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

      Address otraDireccion = new Address();
      StringType otraCalle = new StringType();
      otraCalle.setValue("Tucumán 820 piso 8 depto 49");
      otraDireccion.setLine(java.util.Arrays.asList(otraCalle));
      otraDireccion.setCity("Otra Ciudad");
      otraDireccion.setState("Otra Provincia");
      otraDireccion.setCountry("Argentina");
      patient.setAddress(Arrays.asList(otraDireccion));

      MethodOutcome outcome = client.update()
         .resource(patient)
         .withId(patientId)
         .execute();

      Patient pacienteActualizado = client.read()
         .resource(Patient.class)
         .withId((IdType) outcome.getId())
         .execute();

      System.out.println("Paciente actualizado:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteActualizado));
   }

   private static void copyObservationWithNewPatientId(String patientId, String observationId) {
      System.out.println("Se buscará la obs. con el sig. id: ["
         + observationId + "] y se usará el sig. id de paciente: [" + patientId + "].");


      Observation observacionExistente = client.read().resource(Observation.class).withId(observationId).execute();
      System.out.println("Se obtuvo la observación: " + observacionExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observacionExistente));

      Patient pacienteExistente = client.read().resource(Patient.class).withId(patientId).execute();
      System.out.print("Se obtuvo el paciente: " + pacienteExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteExistente));

      Observation copiaDeLaObservacion = observacionExistente.copy();
      copiaDeLaObservacion.setSubject(new Reference(pacienteExistente.getId()));

      MethodOutcome outcome = client.create().resource(copiaDeLaObservacion).execute();
      Observation copiaDeLaObsGuardada = (Observation) outcome.getResource();

      System.out.println("La observación cambiándole el paciente ahora tiene:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(copiaDeLaObsGuardada));
   }

   private static void updateObservationStatus(String observationId) {
      Observation obsExistente = client.read().resource(Observation.class).withId(observationId).execute();

      System.out.println("Cambiando el estado a la observación anterior. Esa observación tenía el estado ["
         + obsExistente.getStatus() + "].");

      obsExistente.setStatus(Observation.ObservationStatus.AMENDED);

      MethodOutcome outcome = client.update()
         .resource(obsExistente)
         .withId(new IdType("Observation", observationId))
         .execute();

      Observation obsExistenteConNuevoEstado = (Observation) outcome.getResource();

      System.out.println("Ahora tiene: ");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obsExistenteConNuevoEstado));
   }

   private static void deletePatientById(String patientId) {
      try {
         System.out.println("Eliminando paciente con id ¨[" + patientId + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Patient", patientId))
            .execute();
      } catch (Exception e) {
         System.out.println("Error al eliminar el paciente con ID ["
            + patientId + "]. " + Arrays.toString(e.getStackTrace()));
      }
   }

   private static void deletePatientsByIds(String[] ids) {
      for (String id : ids) {
         System.out.println("Eliminando paciente [" + id + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Patient", id))
            .execute();
      }
   }

   private static void deleteObservationById(String observationId) {
      try {
         System.out.println("Borrando observacion [" + observationId + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Observation", observationId))
            .execute();
      } catch (Exception e) {
         System.out.println("Error al eliminar la observación con ID ["
            + observationId + "]. " + Arrays.toString(e.getStackTrace()));
      }
   }

   private static void deleteObservationsByIds(String[] ids) {
      for (String id : ids) {
         System.out.println("Borrando observacion [" + id + "].");
         MethodOutcome response = client
            .delete()
            .resourceById(new IdType("Observation", id))
            .execute();
      }
   }

   private static void deleteObservationsByPatientId(String patientId) {
      String url = "/Observation?subject=Patient/" + patientId;
      Bundle observationsByPatientId = client.search().byUrl(url).returnBundle(Bundle.class).execute();

      for (Bundle.BundleEntryComponent entry : observationsByPatientId.getEntry()) {
         Resource res = entry.getResource();
         String id = entry.getResource().getMeta().getVersionId();
         if (res instanceof Observation) {

            System.out.println("Eliminando observation: " + id);

            client.delete().resourceById(new IdType("Observation", id)).execute();
         } else {
            System.out.println("Recurso no es Observation: " + res.getResourceType());
         }
      }
   }


}
