package package_00;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;

/** 
 * Simula una mesa de votacion de una sede especifica, la clase
 * cuenta con un numero de mesa, el cual es unico en cada sede y sirve como identificador,
 * una capacidad maxima que indica cuantos votantes como maximo puede tener asignados esa mesa,
 * un ArrayList<> de objetos de la Votante que almacena los votantes asignados a la mesa, y
 * un HashMap<String, Integer> que sirve para que cada mesa lleve su propio conteo de los votos
 * siendo la clave el nombre del candidato y el valor un entero que corresponde a la cantidad de votos
 * que lleva dicho candidato
 */

public class Mesa {
	
	private int numeroMesa;
	private int capMax;
	private ArrayList<Votante> listaVotantes;
	private HashMap<String, Integer> conteoVotos;
	
	public Mesa(int numeroMesa, int capMax, HashMap<String, Integer> conteoVotos) {
		setNumeroMesa(numeroMesa);
		this.listaVotantes = new ArrayList<Votante>(); 
		setCapMax(capMax);
		setConteoVotos(conteoVotos);
	}
	
	public void setNumeroMesa(int numeroMesa) {
		if (numeroMesa <= 0) {
			System.out.println("Valor invalido"); // Despues manejar con excepciones cuando se repase ese contenido
		} else {
			this.numeroMesa = numeroMesa;
		}
	}
	
	public boolean setCapMax(int capMax) {
		if (capMax <= 0) {
			System.out.println("Valor invalido");
			return false;
		} else {
			if (listaVotantes != null && listaVotantes.size() > capMax) {
				System.out.println("La nueva capacidad maxima de votantes es inferior a la cantidad de votantes ya asignados");
				return false;
			} else {
				this.capMax = capMax;
				return true;
			}
		}
	}
	
	public void setListaVotantes(ArrayList<Votante> listaVotantes) {
		if (listaVotantes == null) {
			System.out.println("Valor invalido");
		} else {
			this.listaVotantes = listaVotantes;
		}
	}
	
	public void setConteoVotos(HashMap<String, Integer> conteoVotos) {
		if (conteoVotos == null) {
			System.out.println("Valor invalido");
		} else {
			this.conteoVotos = conteoVotos;
		}
	}
	
	public int getNumeroMesa() {
		return numeroMesa;
	}
	
	public int getCapMax() {
		return capMax;
	}
	
	public Votante getVotante(int indice) {
		return listaVotantes.get(indice);
	}
	
	public int cantidadVotantes() {
		return listaVotantes.size();
	}
	
	public Map<String, Integer> getConteoVotos(){
		return Collections.unmodifiableMap(conteoVotos);
	}
	
	/** 
	 *  El metodo verifica la capacidad maxima de la mesa y la compara con
	 *  el tamaño del ArrayLista<> de votantes que esta posee con su capacidad maxima
	 *  para comprobar si la mesa cuenta con capacidad para seguir agregando votantes, ademas
	 *  se valida que el votante ingresado como parametro no sea null antes de agregarlo
	 *  al ArrayList<> de votantes
	 * 
	 * @param votante Corresponde al objeto de la clase Votante que se quiere agregar
	 *        al ArrayList<> de votantes de la mesa
	 * @throws El metodo lanza una excepcion de tipo ExcedeCapacidadException para indicar
	 *         que la mesa no cuenta con capacidad para agregar mas votantes a su
	 *         ArrayList<>
	 * */
	
	public void agregarVotante(Votante votante) throws ExcedeCapacidadException {
		if (votante == null) {
			return;
		} else if (listaVotantes != null && listaVotantes.size() >= capMax) {
			throw new ExcedeCapacidadException("Se excede la capacidad maxima de la mesa");
		} else {
			if (listaVotantes == null) {
				listaVotantes = new ArrayList<Votante>();
			}
			listaVotantes.add(votante);
		}
	}
	
	public Votante buscarVotante(String RUT) throws BusquedaFallidaException {
		
		if (listaVotantes == null) {
			throw new BusquedaFallidaException("No se puedo encontrar al votante por que la lista esta vacia");
		}
		for (Votante v : listaVotantes) {
			if (v != null && v.getRut() != null && v.getRut().equals(RUT)) {
				return v;
			}
		}	
		throw new BusquedaFallidaException("Votante no encontrado");
	}
	
	public Votante eliminarVotante(String rut) throws BusquedaFallidaException {
		
		Votante v = buscarVotante(rut);
		listaVotantes.remove(v);
	    return v;
	}
}
