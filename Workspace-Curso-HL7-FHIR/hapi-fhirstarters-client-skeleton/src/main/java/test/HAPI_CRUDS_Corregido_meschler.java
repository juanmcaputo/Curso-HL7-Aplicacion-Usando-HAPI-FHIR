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

public class HAPI_CRUDS_Corregido_meschler {

   private static final FhirContext ctx = FhirContext.forR4();
   private static final IGenericClient client = ctx.newRestfulGenericClient("https://server.fire.ly/r4/");
   private static final String patientId = "83abae35-ee18-499d-93e9-d343acf136cc";
   private static final String observationId = "c007d39c-1e5c-4040-8a70-deb325d9e813";

   public static void main(String args[]) {

      // Punto 1 - A
      //System.out.println("Obteniendo paciente por ID:");
      //getPatientById(patientId);

      // Punto 1 - B
      //System.out.println("Obteniendo observación por ID:");
      //Observation observationById = getObservationById(observationId);

      // Punto 1 - C
      //System.out.println("Obteniendo pacientes por apellido:");
      //getPatientsByLastName("Perez");
      //List<Patient> patientsByLastName = getPatientsByLastName("Perez");

      // Punto 1 - D
      //System.out.println("Obteniendo observaciones por estado y apellido del paciente:");
      //List<Observation> observationsByStatusAndPatientLastName = getObservationsByStatusAndPatientLastName("final", "Perez");

      //---------------------------------------------------//

      // Punto 2 - A
      // Crear un nuevo paciente
      System.out.println("Creando un nuevo paciente:");
      Patient nuevoPacienteGenerado = createPatient();

      // Punto 2 - B
      // Modificar la dirección del paciente creado
      System.out.println("Modificando la dirección del paciente:");
      updatePatientAddress();

      // Punto 2 - C
      // Copiar una observación y modificar el ID del paciente
      System.out.println("Copiando una observación y modificando el ID del paciente:");
      copyObservationWithNewPatientId();

      // Punto 2 - D
      // Modificar el estado de una observación existente
       //updateObservationStatus();

      //---------------------------------------------------//

      // Punto 3 - A
      //System.out.println("Eliminando un paciente por ID:");
      //deletePatientById();

      // Punto 3 - B
      //System.out.println("Eliminando múltiples pacientes por IDs:");
      //deletePatientsByIds(new String[]{"6b1a83ba-4f10-4241-9f76-2af722807a54","0948c770-8c98-40fc-aba7-ff832c3d65be"});

      // Punto 3 - C
      //System.out.println("Eliminando una observación por ID:");
      //deleteObservationById();

      // Punto 3 - D
      //System.out.println("Eliminando múltiples observaciones por IDs:");
      //deleteObservationsByIds(new String[]{"c93eb500-6890-4c8c-a99d-1b287007d606","f9c9fc4f-b5c7-4f4f-b3fe-42155123e86d"});

      // Punto 3 - E
      //System.out.println("Eliminando observaciones asociadas a un paciente por ID:");
      deleteObservationsByPatientId();
   }


   private static void getPatientById(String id) {
      Patient patient = client.read()
         .resource(Patient.class)
         .withId(id)
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

   private static List<Patient> getPatientsByLastName(String lastName) {
      Bundle patientsByFamilyName = client.search().forResource(Patient.class)
         //.where(Patient.FAMILY.matchesExactly().value(lastName))
    	.where(Patient.FAMILY.matches().value(lastName))	      		  
         .returnBundle(Bundle.class)
         .execute();

      List<Patient> patientsByLastName = patientsByFamilyName.getEntry().stream().map(
         e -> (Patient) e.getResource()).collect(Collectors.toList());

      /*for (Patient p : patientsByLastName) {
         System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p));
      }*/
      
      //Esto es lo que tenía que hacer
      String string1 = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patientsByFamilyName);
      System.out.println(string1);

      return patientsByLastName;
   }

   private static List<Observation> getObservationsByStatusAndPatientLastName(String status, String family) {
      List<Observation> observations = new ArrayList<>();

      //Se complica demasiado, hace una busqueda de pacientes, después de observaciones, después imprime una por una
      //Esto se tiene que resolver en una sola linea de codigo :
      //client.search().forResource(Observation.class).where(#condicion observation).and(#Condiciones observation->apellido).returnBundel....
      // Las condiciones son :
      //	* Condiciones de la observacion = Observation.STATUS.exactly().code(status)
      //	* Condiciones observation->apellid =   Observation.SUBJECT.hasChainedProperty(Patient.FAMILY.matches().value(family))
      // Eso lo devuelve en un Bundle resultados o como quiere llamar y lo imprimí y listo	 
     //ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(BundleResultados);
     //	System.out.println(string1); <-- Vea el código con la corrección
      //
      
      // Primero busco pacientes con un apellido dado. Uso la lógica que armé para el método anterior.
      Bundle patientsByFamilyName = client.search().forResource(Patient.class)
         .where(Patient.FAMILY.matchesExactly().value(family))
         .returnBundle(Bundle.class)
         .execute();

      List<String> patientIds = patientsByFamilyName.getEntry().stream()
         .map(entry -> ((Patient) entry.getResource()).getId())
         .collect(Collectors.toList());

      for (String pid : patientIds) {
         System.out.println("pid: " + pid);


         Bundle obsBundle = client.search()
            .forResource(Observation.class)
            .where(Observation.STATUS.exactly().codes(status))
            .where(Observation.SUBJECT.hasId(new IdType("Patient", pid)))
            .returnBundle(Bundle.class)
            .execute();

         // Procesar obsBundle como antes
         for (Bundle.BundleEntryComponent entry : obsBundle.getEntry()) {
            Resource res = entry.getResource();
            if (res instanceof Observation) {
               Observation obs = (Observation) res;
               System.out.println("ID de Observation: " + obs.getIdBase());
               observations.add((Observation) res);
            } else {
               // Recursos de otro tipo, opcionalmente loguea o ignora
               System.out.println("Recurso no es Observation: " + res.getResourceType());
            }
         }
      }

      return observations;
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

   private static void updatePatientAddress() {
      Patient patient = new Patient();
      patient.setId("0948c770-8c98-40fc-aba7-ff832c3d65be");

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
         .execute();

      Patient pacienteActualizado = client.read()
         .resource(Patient.class)
         .withId((IdType) outcome.getId())
         .execute();

      System.out.println("Paciente actualizado:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteActualizado));
   }

   private static void copyObservationWithNewPatientId() {
      System.out.println("Se buscará la obs. con el sig. id: ["
         + observationId + "] y se usará el sig. id de paciente: [" + patientId + "].");


      Observation observacionExistente = client.read().resource(Observation.class).withId(observationId).execute();
      System.out.println("Se obtuvo la observación: " + observacionExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(observacionExistente));

      Patient pacienteExistente = client.read().resource(Patient.class).withId(patientId).execute();
      System.out.print("Se obtuvo el paciente creado en el punto 2A: " + pacienteExistente.getId());
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pacienteExistente));

      Observation copiaDeLaObservacion = observacionExistente.copy();
      copiaDeLaObservacion.setSubject(new Reference(pacienteExistente.getId()));

      MethodOutcome outcome = client.create().resource(copiaDeLaObservacion).execute();
      Observation copiaDeLaObsGuardada = (Observation) outcome.getResource();

      System.out.println("La observación cambiándole el paciente ahora tiene:");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(copiaDeLaObsGuardada));
   }

   private static void updateObservationStatus() {
      Observation obsExistente = client.read().resource(Observation.class).withId(observationId).execute();
      Observation.ObservationStatus observationStatus = Observation.ObservationStatus.CANCELLED;
      System.out.println("Cambiando el estado a la observación anterior. Esa observación tenía el estado ["
         + obsExistente.getStatus() + "].");

      obsExistente.setStatus(observationStatus);

      MethodOutcome outcome = client.update()
         .resource(obsExistente)
         .withId(new IdType("Observation", observationId))
         .execute();

      Observation obsExistenteConNuevoEstado = (Observation) outcome.getResource();

      System.out.println("Ahora tiene: ");
      System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obsExistenteConNuevoEstado));
   }

   private static void deletePatientById() {
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

   private static void deleteObservationById() {
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

   private static void deleteObservationsByPatientId() {
      System.out.println("Primero busco las observaciones de ese paciente...");
      Bundle observationsByPatientId = client.search().forResource(Observation.class)
         .where(Observation.SUBJECT.hasId(patientId))
         .returnBundle(Bundle.class).execute();

      for (Bundle.BundleEntryComponent entry : observationsByPatientId.getEntry()) {
         Resource res = entry.getResource();
         if (res instanceof Observation) {
            //Observation obs = (Observation) res;
            System.out.println("ID de Observation: " + res.getId());

            String[] parts = res.getId().split("/");
            String obsId = parts[parts.length - 3];

            client.delete().resourceById(new IdType("Observation", obsId)).execute();
            System.out.println("Eliminada Observation ID: " + obsId);
         } else {
            // Recursos de otro tipo, opcionalmente loguea o ignora
            System.out.println("Recurso no es Observation: " + res.getResourceType());
         }
      }
   }


}
