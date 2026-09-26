package package_00;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        
    	// Se crea una plantilla para que cada mesa lleve un conteo de votos
    	
        Vector<Sede> sedesIniciales = new Vector<>();
        ArrayList<Candidato> candidatosIniciales = new ArrayList<Candidato>();
        
        candidatosIniciales.add(new Candidato("11111111-1", "Romario Lincocheo", "Frente Amplio (FA)"));
        candidatosIniciales.add(new Candidato("22222222-2", "Franco Naudon", "Partido Socialista (PS)"));
        
        HashMap<String, Integer> plantillaConteoVotos = GestorDeColecciones.construirPlantillaVotos(candidatosIniciales);
        // Se prueba cargar el CSV
        GestorDeColecciones gestor = ControlPersistenciaDeDatos.cargar(sedesIniciales, plantillaConteoVotos);
        
        // Si no hay archivo previo usamos los datos inicializados por defecto
        if (gestor == null) {
            System.out.println("Sistema inicializado con un gestor vacio.");     
            try {
                
            	/* Se crean datos por defecto para probar el sistema
            	 * automatico de asignacion de sedes y mesas
            	 */
            	
                Sede sedePrueba2 = new Sede(2, 2000, new Coordenadas(10000, 10000));
                Sede sedePrueba3 = new Sede(3, 2000, new Coordenadas(-10000, -10000));
                
                Mesa mesaSede2 = new Mesa(1, 200, new HashMap<>(plantillaConteoVotos));
                Mesa mesaSede3 = new Mesa(1, 200, new HashMap<>(plantillaConteoVotos));
                
                sedePrueba2.agregarMesa(mesaSede2);
                sedePrueba3.agregarMesa(mesaSede3);
                
                sedesIniciales.add(sedePrueba2);
                sedesIniciales.add(sedePrueba3);

                SistemaDeAsignacion asignadorDeSedesYMesas = new SistemaDeAsignacion();

                Votante votante_prueba = new Votante("333333-3", "Votante sede 3", new Coordenadas(-9000, -8000));
                Votante votante_prueba_2 = new Votante("2222222-2", "Votante sede 2", new Coordenadas(9000, 8000));

                asignadorDeSedesYMesas.asignarSede(votante_prueba, sedesIniciales);
                asignadorDeSedesYMesas.asignarSede(votante_prueba_2, sedesIniciales);

                gestor = new GestorDeColecciones(sedesIniciales, candidatosIniciales);
                System.out.println("Datos iniciales de prueba y asignación por distancia cargados exitosamente.");
                
            } catch (Exception e) {
                System.err.println("Advertencia al cargar datos iniciales: " + e.getMessage());
            }
        }
        
        if (gestor.getCandidatos() == null || gestor.getCandidatos().isEmpty()) {
            for (Candidato c : candidatosIniciales) {
                gestor.agregarCandidato(c);
            }
        }
        
        plantillaConteoVotos = gestor.construirPlantillaVotos();
        
        Vector<Sede> sedes = gestor.getSedes(); 
        
        MenuInteractivo menu = new MenuInteractivo(sedes, plantillaConteoVotos, gestor.getCandidatos());
        try {
            menu.leerEntradaUsuario();
        } catch (IOException e) {
            System.err.println("Error en el menu: " + e.getMessage());
        }
        
        ControlPersistenciaDeDatos.guardar(gestor);
        System.out.println("Programa finalizado correctamente.");
    }
}
