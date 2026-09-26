package package_00;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class ControlPersistenciaDeDatos {

    private static final String ARCHIVO_CSV = "sistema_votacion.csv";
    private static final String ARCHIVO_CANDIDATOS_CSV = "candidatos.csv";
    
    /*
     * Metodo guardar
     *
     * Se encarga de guardar el ID de las diferentes sedes, los diferentes numeros
     * identificadores de mesa, las diferentes capacidades maximas de las mesas,
     * los diferentes rut de cada votante, el nombre de cada votante, y las
     * coordenadas del domicilio de cada votante.
     * Una mesa con varios votantes genera varias filas mientras que una mesa 
     * sin votantes genera una unica fila.
     * 
     */
    
    public static void guardar(GestorDeColecciones gestor) {
        if (gestor == null || gestor.getSedes() == null) {
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
        	
            // Cabecera del CSV
        	
            pw.println(
                    "ID_Sede,Numero_Mesa,Capacidad_Mesa," +
                    "Rut_Votante,Nombre_Votante,X_Coord,Y_Coord"
            );


            // Se recorren Sede -> Mesa -> Votante
            
            for (Sede sede : gestor.getSedes()) {
                if (sede == null ||
                    sede.getMapaMesas() == null) {
                    continue;
                }


                for (Mesa mesa : sede.getMapaMesas()) {
                    if (mesa == null) {
                        continue;
                    }

                    // Caso para mesas sin votantes asignados

                    if (mesa.cantidadVotantes() == 0) {
                        String linea =
                                sede.getId() + "," +
                                mesa.getNumeroMesa() + "," +
                                mesa.getCapMax() + ",,,,,";
                        pw.println(linea);
                    }

                    // Caso para mesas que si tienen una cierta cantidad de votantes asignados
                  
                    else {
                        for (int i = 0; i < mesa.cantidadVotantes(); i++) {
                            
                        	Votante votante = mesa.getVotante(i);
                            if (votante == null) {
                                continue;
                            }

                            String linea =
                                    sede.getId() + "," +
                                    mesa.getNumeroMesa() + "," +
                                    mesa.getCapMax() + "," +
                                    votante.getRut() + "," +
                                    votante.getNombre() + "," +
                                    votante.getResidencia().getX() + "," +
                                    votante.getResidencia().getY();

                            pw.println(linea);
                        }
                    }
                }
            }

            guardarCandidatos(gestor);
            System.out.println("Datos guardados correctamente en el archivo CSV!");
            
        } catch (IOException e) {
            System.err.println("Error al guardar los datos en CSV: " + e.getMessage());
        }
    }

    /*
     * Metodo cargar
     *
     * Si no existe archivo previo este retornara null y Main se encargara
     * de crear los datos predeterminados, en caso de existir el csv
     * se reconstruye el gestor a partir del archivo.
     * 
     */

    public static GestorDeColecciones cargar(Vector<Sede> sedesIniciales, HashMap<String, Integer> conteoVotosPlantilla) {

        File archivo = new File(ARCHIVO_CSV);


        /* Se comprueba si existe o no archivo previo, en caso
         * de no existir se inicializa el programa con datos
         * predeterminados */
        
        if (!archivo.exists()) {
            System.out.println("No se encontro archivo CSV previo.");
            System.out.println("Se iniciara con los datos predeterminados.");
            return null;
        }

        /*
         * Si existe el CSV, creamos un gestor vacío.
         *
         * NO utilizamos sedesIniciales aquí.
         *
         * El estado persistido será el que está en el CSV.
         */
        GestorDeColecciones gestorCargado = new GestorDeColecciones(new Vector<Sede>());
        

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))) {

            // Se salta la cabacera
        
            String linea = br.readLine();

            // Se leen todas las filas
            
            while ((linea = br.readLine()) != null) {
            	
                String[] partes = linea.split(",", -1); // -1 permite conservar las columnas vacias

                /* Las primeras tres columnas son obligatorias y corresponde
                 * al ID de la sede, el numero de mesa y la capacidad maxima de la mesa */
                
                if (partes.length < 3) {
                    continue;
                }

                try {
                	
                    int idSede = Integer.parseInt(partes[0].trim());
                    int numeroMesa = Integer.parseInt(partes[1].trim());
                    int capacidadMesa = Integer.parseInt(partes[2].trim());

                    // Instrucciones para buscar o crear sede

                    Sede sedeEncontrada = gestorCargado.buscarSede(idSede);
                    if (sedeEncontrada == null) {

                        /* Se usan valores definidos por el sistema en las
                         * sedes */
                    	
                        Coordenadas coordenadasSede = new Coordenadas(0.0, 0.0);
                        sedeEncontrada = new Sede(idSede, 2000, coordenadasSede);
        
                        gestorCargado.getSedes().add(sedeEncontrada);
                    }

                    // Instrucciones para buscar o crear mesa

                    Mesa mesaEncontrada = gestorCargado.buscarMesaEnSede(numeroMesa, sedeEncontrada);
                    if (mesaEncontrada == null) {

                        /* Si la mesa no existe se crea utilizando la capacidad almacenada
                         * en el csv */
                    	
                        mesaEncontrada = new Mesa(numeroMesa, capacidadMesa, new HashMap<>(conteoVotosPlantilla));
                        sedeEncontrada.agregarMesa(mesaEncontrada);
                        
                    }


                    /*
                     * Instrucciones para cargar el votante 
                     * 
                     * 0 -> ID de la sede
                     * 1 -> Numero de mesa
                     * 2 -> Capacidad maxima
                     * 3 -> Rut
                     * 4 -> Nombre
                     * 5 -> Coordenada X 
                     * 6 -> Coordenada Y
                     * 
                     */
                    
                    if (partes.length >= 7 && !partes[3].trim().isEmpty()) {
                    	
                        String rut = partes[3].trim();
                        String nombre = partes[4].trim();
                        double x = Double.parseDouble(partes[5].trim());
                        double y = Double.parseDouble(partes[6].trim());

                        // Se comprueba que el votante no este ya asignado a la mesa
                        
                        boolean existeVotante = false;
                        for (int i = 0; i < mesaEncontrada.cantidadVotantes(); i++) {
                            Votante votanteExistente = mesaEncontrada.getVotante(i);
                            if (votanteExistente != null && votanteExistente.getRut() != null && votanteExistente.getRut().equals(rut)) {
                                existeVotante = true;
                                break;
                            }
                        }
                        
                        // En caso de que no exista se crea y se agrega
                        
                        if (!existeVotante) {	
                            Coordenadas coordenadasVotante = new Coordenadas(x, y);
                            Votante votante = new Votante(rut, nombre, coordenadasVotante);
                            mesaEncontrada.agregarVotante(votante);
                            
                        }
                    }

                } catch (Exception e) {
                	
                    System.err.println("Error procesando línea del CSV: " + linea);
                    System.err.println("Informacion adicional: " + e.getMessage());
                }
            }

            System.out.println("Datos cargados exitosamente desde el archivo CSV!");
            
            ArrayList<Candidato> candidatosCargados = cargarCandidatos();
            for (Candidato c : candidatosCargados) {
                gestorCargado.agregarCandidato(c);
            }
            
            return gestorCargado;

            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            return null;
        }
    }
    
    public static void guardarCandidatos(GestorDeColecciones gestor) {
        if (gestor == null || gestor.getCandidatos() == null) {
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CANDIDATOS_CSV))) {
            pw.println("Rut,Nombre,Partido");
            for (Candidato c : gestor.getCandidatos()) {
                if (c == null) continue;
                pw.println(
                    c.getRut() + "," +
                    c.getNombre() + "," +
                    c.getPartido()
                );
            }
        } catch (IOException e) {
            System.err.println("Error al guardar candidatos: " + e.getMessage());
        }
    }
    
    public static ArrayList<Candidato> cargarCandidatos() {
        ArrayList<Candidato> candidatos = new ArrayList<>();
        File archivo = new File(ARCHIVO_CANDIDATOS_CSV);
        if (!archivo.exists()) {
            return candidatos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                if (partes.length < 3) continue;
                String rut = partes[0].trim();
                String nombre = partes[1].trim();
                String partido = partes[2].trim();
                if (rut.isEmpty()) continue;
                candidatos.add(new Candidato(rut, nombre, partido));
            }
        } catch (IOException e) {
            System.err.println("Error al cargar candidatos: " + e.getMessage());
        }
        return candidatos;
    }
}
