package package_00;
import java.util.HashMap;

/**
 * Simula una sede de votacion, cuenta con un identificador unico de tipo entero, una capacidad
 * maxima para los votantes asignados a dicha sede, un entero que corresponde a los cupos disponibles de 
 * la sede para regular la asignacion de votantes, una ubicacion en coordenadas para poder
 * asignar los votantes en base a la cercania que tengan en relacion a la sede, y un HashMap<Integer, Mesa>
 * que cuenta con cada una de las mesas de votacion de la sede
 * 
 */

public class Sede {
	
	private int id;
	private int capMax;
	private int cuposDisponibles;
	private Coordenadas ubicacion;
	private HashMap<Integer, Mesa> mapaMesas;
	public Sede(int id, int capMax, Coordenadas ubicacion) {
		setId(id);
		setCapMax(capMax);
		cuposDisponibles = this.capMax;
		setUbicacion(ubicacion);
		mapaMesas = new HashMap<Integer, Mesa>();
	}
	
	public void setId(int id) {
		if (id <= 0) {
			System.out.println("ID no valido"); // Despues manejar con excepciones
		} else {
			this.id = id;
		}
	}
	
	public void setCapMax(int capMax) {
		if (capMax <= 0) {
			System.out.println("Capacidad no valida");
		} else {
			this.capMax = capMax;
		}
	}
	
	public void setCuposDisponibles(int cuposDisponibles) {
		if (cuposDisponibles < 0 || cuposDisponibles > capMax) {
			System.out.println("Cantidad ingresada no valida");
		} else {
			this.cuposDisponibles = cuposDisponibles;
		}
	}
	
	public void setUbicacion(Coordenadas ubicacion) {
		if (ubicacion == null) {
			System.out.println("Ubicacion no valida");
		} else {
			this.ubicacion = ubicacion;
		}
	}
	
	/**
	 * Sobrecarga el metodo setUbicacion para que el usuario tenga la posibilidad
	 * de ingresar las coordenadas de la sede en sus dos componentes x e y por separado
	 * en lugar de pasar por parametro un objeto de la clase Coordenadas
	 */
	
	public void setUbicacion(double x, double y) {
		if (ubicacion == null) {
			ubicacion = new Coordenadas(x, y);
		} else {
			ubicacion.setX(x);
			ubicacion.setY(y);
		}
	}
	
	public void setMapaMesas(HashMap<Integer, Mesa> mapaMesas) {
		if (mapaMesas == null) {
			System.out.println("Coleccion no valida");
		} else {
			this.mapaMesas = mapaMesas;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public int getCapMax() {
		return capMax;
	}
	
	public int getCuposDisponibles() {
		return cuposDisponibles;
	}
	
	public Coordenadas getUbicacion() {
		return ubicacion;
	}
	
	public HashMap<Integer, Mesa> getMapaMesas() {
		return mapaMesas;
	}
	
	/**
	 * Decrementa los cupos disponibles de la sede en caso de que un votante haya sido
	 * asignado con exito a una mesa de la sede
	 * 
	 */
	
	public void decrementarCuposDisponibles() {
		cuposDisponibles--;
	}
	
	/**
	 * 
	 * Suma las capacidades de cada mesa de la sede para asegurarse de
	 * que dicha suma no exceda la capacidad maxima de la sede y evitar incosistencias
	 * al momento de agregar mas mesas a la sede. El metodo recorre los valores del HashMap<Integer, Mesa>
	 * que contiene las mesas de la sede, validando que la mesa que se esta analizando no sea null
	 * para acceder a su atributo que almacena su capacidad maxima y agregandoselo a la 
	 * variable de retorno suma
	 * 
	 * @return Retorna la suma de las capacidades de la sede, esta se inicializa en 0,
	 * 		   si el HashMap<Integer, Mesa> que contiene las mesas de la sede es null
	 *         se retorna la variable suma sin haberle sumado ningun valor, de lo contrario
	 *         se retorna la variable almacenando la suma de las capacidades maximas de cada mesa 
	 *         
	 */
	
	public int sumarCapacidadMesa() {
		int suma = 0;
		if (mapaMesas == null) {
			return suma;
		} else {
			for (Mesa mesa : mapaMesas.values()) {
				if (mesa != null) {
					suma += mesa.getCapMax();
				}
			}
			return suma;
		}
	}
	
	/**
	 * El metodo agrega un objeto de la clase mesa, el cual es recibido por parametro, al HashMap<Integer, Mesa>
	 * que contiene las mesas de la sede, validando antes, que el parametro recibido no sea null, y si no lo es, que el
	 * agregar la nueva mesa no supere la capacidad maxima de la sede a causa de la capacidad
	 * de votantes de la mesa nueva, ademas, se valida si ya existe una mesa con el mismo 
	 * numero de mesa que el que tiene la mesa que se quiere agregar, en ese caso se reemplaza la mesa original por
	 * la nueva, validando que no se exceda la capacidad maxima de la sede en el proceso
	 * 
	 * @param mesa Se pasa por parametro el objeto de la clase mesa que se quiere agregar al
	 *        HashMap<Integer, Mesa> de mesas de la sede
	 * @throws El metodo lanza una excepcion de tipo ExcedeCapacidadException en caso de que
	 *         agregar la nueva mesa implique superar la capacidad maxima de la sede, o bien 
	 *         si reemplazar una mesa ya existente con una nueva implica exceder el limite de
	 *         votantes de la sede
	 *          
	 */
	
	
	public void agregarMesa(Mesa mesa) throws ExcedeCapacidadException {
		if (mesa == null) {
			return;
		} else {
			if (mapaMesas == null) {
				mapaMesas = new HashMap<Integer, Mesa>();
			} else {
				Mesa mesaAReemplazar = (Mesa)mapaMesas.get(mesa.getNumeroMesa());
				if (mesaAReemplazar != null) {
					if (sumarCapacidadMesa() - mesaAReemplazar.getCapMax() + mesa.getCapMax() > capMax) {
						throw new ExcedeCapacidadException("La mesa no puede ser reemplazada por una de mayor capacidad por que excederia la capacidad de votantes la sede");
					}
				} else {
					if (sumarCapacidadMesa() + mesa.getCapMax() > capMax) {
						throw new ExcedeCapacidadException("La mesa no puede ser agregada porque se excede la capacidad de votantes de la sede");
					}
				}
			}
			mapaMesas.put(mesa.getNumeroMesa(), mesa);
			return;
		}
	}
	
	public Mesa buscarMesa(int numMesa) throws BusquedaFallidaException {
		
		if (mapaMesas == null) {
			throw new BusquedaFallidaException("La mesa no ha sido encontrada debido a que la sede no tiene mesas");
		} else {
			Mesa encontrada = mapaMesas.get(numMesa);
			if (encontrada == null) {
				throw new BusquedaFallidaException("La mesa no ha sido encontrada en la sede");
			} else {
				return encontrada;
			}
		}
	}
	
	public Mesa retirarMesa(int numMesa) throws BusquedaFallidaException {
		
		Mesa quitada = buscarMesa(numMesa);
		mapaMesas.remove(numMesa, quitada);
		return quitada;
		
	}
}
