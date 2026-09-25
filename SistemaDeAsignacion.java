package package_00;
import java.util.HashMap;
import java.util.Vector;

/**
 * Simula un sistema para poder asignar una sede y una mesa a un votante
 * 
 */

public class SistemaDeAsignacion {
	
	/**
	 * Asigna un votante a una mesa que cuente con la capacidad disponible en una sede en particular
	 * @param votante Hace referencia al votante al cual se le quiere asignar una mesa dentro de la sede
	 * @param sede Corresponde a la sede a la que se quiere asignar un votante
	 * @return El metodo retorna true si encuentra una mesa no null que cuenta con capacidad
	 *         disponible para poder asignarle el votante, en caso de que la sede ingresada no
	 *         cuente con mesas, o de que no se encuentre una mesa no null o con capacidad disponible
	 *         para poder asignarle el votante, el metodo retornara false  
	 *         
	 */
	
	private boolean asignarMesa(Votante votante, Sede sede) {
		
		HashMap<Integer, Mesa> mapaMesas = sede.getMapaMesas();
		if (mapaMesas == null) {
			return false;
		}
		
		for (Mesa mesa : mapaMesas.values()) {
			if (mesa != null) {
				try {
					mesa.agregarVotante(votante);
					votante.setTieneMesa(true);
					System.out.println("El votante ha sido agregado con exito");
					return true;
				} catch (ExcedeCapacidadException e) {
					
				}
			}
		}
		return false;
	}
	
	/**
	 * Recorre el vector con las sedes, validando que estas cuenten con la capacidad para
	 * asignar un votante, verificando que ni el vector que almacena las sedes, ni el votante ingresado
	 * sean null, y comprobando que el votante no tenga ya una mesa asignada. Se llama al metodo
	 * calcularDistanciaASede del votante en cada sede, validando antes que la sede tenga cupos disponibles,
	 * para hallar la sede disponible mas cercana al votante, se declara una variable
	 * auxiliar para verificar si se esta en la primera iteracion de la busqueda de sedes para establecer
	 * un criterio de comparacion de distancias para determinar la sede disponible mas cercana. Finalmente
	 * se comprueba si efectivamente se encontro una sede disponible para asignar al votante y se llama
	 * al metodo asignarMesa para asignar una mesa al votante, y si este retorna true, confirmando que el
	 * votante fue asignado con exito, se decrementa la cantidad de cupos disponibles de la sede
	 * @param votante Hace referencia al votante al que se le quiere asignar una sede
	 * @param vectorSedes Corresponde al vector que almacena las sedes que almacena el programa completo
	 * 
	 */
	
	public void asignarSede(Votante votante, Vector<Sede> vectorSedes) {
		if (votante == null || votante.getTieneMesa() == true || vectorSedes == null) {
			System.out.println("El votante ya tiene una mesa asignada o el votante no existe");
			return;
		}
		double menorDistancia = 0;
		Sede sedeMenorDistancia = null;
		int aux = 0;
		for (Sede sede : vectorSedes) {
			if (sede != null && sede.getCuposDisponibles() > 0) {
				if (aux == 0) {
					menorDistancia = votante.calcularDistanciaASede(sede);
					sedeMenorDistancia = sede;
					aux = 1;
				} else {
					double distanciaActual = votante.calcularDistanciaASede(sede);
					if (distanciaActual < menorDistancia) {
						menorDistancia = distanciaActual;
						sedeMenorDistancia = sede;
					}
				}
			}
		}
		if (sedeMenorDistancia != null) {
			if (asignarMesa(votante, sedeMenorDistancia) == true) {
				sedeMenorDistancia.decrementarCuposDisponibles();
			}
		}
	}
}
