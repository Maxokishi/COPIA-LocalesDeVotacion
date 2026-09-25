package package_00;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class ControlPersistenciaDeDatos {

    private static final String ARCHIVO_CSV = "sistema_votacion.csv";

    // Metodo guardar (Sistema batch: graba al salir)
    public static void guardar(GestorDeColecciones gestor) {
        // Obtenemos las sedes almacenadas en el gestor
        Vector<Sede> sedes = gestor.getSedes();
        if (sedes == null) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CSV))) {
            // Escribimos la cabecera del archivo CSV
            pw.println("ID_Sede,Numero_Mesa,Rut_Votante,Nombre_Votante,X_Coord,Y_Coord");

            // Recorremos siguiendo el siguiente orden: Sede -> Mesa -> Votante
            for (Sede sede : sedes) {
                if (sede.getMapaMesas() != null) {
                    for (Mesa mesa : sede.getMapaMesas().values()) {
                        if (mesa.cantidadVotantes() > 0) {
                        	ArrayList<Votante> listaVotantes = new ArrayList<Votante>();
                        	for(int i = 0; i < mesa.cantidadVotantes(); i++) {
                        		listaVotantes.add(mesa.getVotante(i));
                        	}
                        	
                            for (Votante v : listaVotantes) {
                                String linea = sede.getId() + "," +
                                               mesa.getNumeroMesa() + "," +
                                               v.getRut() + "," +
                                               v.getNombre() + "," +
                                               v.getResidencia().getX() + "," +
                                               v.getResidencia().getY();
                                pw.println(linea);
                            }
                        }
                    }
                }
            }
            System.out.println("Datos guardados correctamente en el archivo CSV!");
        } catch (IOException e) {
            System.err.println("Error al guardar los datos en CSV: " + e.getMessage());
        }
    }

    // Metodo cargar (Sistema batch: carga datos al iniciar)
    public static GestorDeColecciones cargar(Vector<Sede> sedesIniciales, HashMap<String, Integer> conteoVotosPlantilla) {
        File archivo = new File(ARCHIVO_CSV);
        
        // Si no existe el archivo previo, retornamos null para que el Main cargue los datos de prueba
        if (!archivo.exists()) {
            System.out.println("No se encontro archivo CSV previo. Se iniciara con datos nuevos.");
            return null; 
        }

        // Creamos un gestor nuevo
        GestorDeColecciones gestorCargado = new GestorDeColecciones(sedesIniciales);

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))) {
            String linea = br.readLine(); // Leer y saltar la cabecera
            
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 6) {
                    try {
                        int idSede = Integer.parseInt(partes[0].trim());
                        int numMesa = Integer.parseInt(partes[1].trim());
                        String rut = partes[2].trim();
                        String nombre = partes[3].trim();
                        double x = Double.parseDouble(partes[4].trim());
                        double y = Double.parseDouble(partes[5].trim());

                        // Buscar la sede, si no existe en dentro de la coleccion, se crea
                        Sede sedeEncontrada = gestorCargado.buscarSede(idSede);
                        if (sedeEncontrada == null) {
                            Coordenadas coordSede = new Coordenadas(0.0, 0.0); // Coordenadas por defecto
                            sedeEncontrada = new Sede(idSede, 100, coordSede);
                            gestorCargado.getSedes().add(sedeEncontrada);
                        }

                        // Buscar la mesa en la sede, si no existe se crea
                        Mesa mesaEncontrada = gestorCargado.buscarMesaEnSede(numMesa, sedeEncontrada);
                        if (mesaEncontrada == null) {
                            mesaEncontrada = new Mesa(numMesa, 50, new HashMap<>(conteoVotosPlantilla));
                            sedeEncontrada.agregarMesa(mesaEncontrada);
                        }

                        // Agregar el votante a la mesa (evitando duplicados si ya estuviera)
                        boolean existeVotante = false;
                        ArrayList <Votante> listaVotantes = new ArrayList <Votante>();
                        
                        for(int i = 0; i < mesaEncontrada.cantidadVotantes(); i++) {
                        	listaVotantes.add(mesaEncontrada.getVotante(i));
                        }
                        
                        for (Votante vExistente : listaVotantes) {
                            if (vExistente.getRut().equals(rut)) {
                                existeVotante = true;
                                break;
                            }
                        }

                        if (!existeVotante) {
                            Coordenadas coordVotante = new Coordenadas(x, y);
                            Votante votante = new Votante(rut, nombre, coordVotante);
                            mesaEncontrada.agregarVotante(votante);
                        }

                    } catch (Exception ex) {
                        System.err.println("Error procesando línea del CSV: " + linea);
                    }
                }
            }
            System.out.println("Datos cargados exitosamente desde el archivo CSV!");
            return gestorCargado;
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            return null;
        }
    }
}
