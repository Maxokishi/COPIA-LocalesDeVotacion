package package_00;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class GestorDeColecciones {
	
	private Vector<Sede> sedes;
	
	public GestorDeColecciones() {
		sedes = new Vector<Sede>();
	}
	
	public Vector<Sede> getSedes() {
	    return sedes;
	}
	
	public GestorDeColecciones(Vector<Sede> sedes) {
		this.sedes = sedes;
	}
	
	public void agregarVotanteAMesa(Votante votante, Mesa mesa) {
		if (votante == null || mesa == null)	{
			System.out.println("La mesa y/o el votante ingresados no existen");
			return;
		} else {
			try {
				mesa.agregarVotante(votante);
				System.out.println("El votante ha sido agregado con exito");
			} catch (ExcedeCapacidadException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void agregarMesaASede(Mesa mesa, Sede sede) {
		if (sede == null) {
			System.out.println("La sede no existe");
			return;
		} else {
			try {
				sede.agregarMesa(mesa);
			    System.out.println("La mesa ha sido agregada con exito");
			} catch (ExcedeCapacidadException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void listarVotantesMesa(Mesa mesa) {
		if (mesa == null) {
			System.out.println("Esta mesa no existe");
		} else {
			ArrayList<Votante> listaVotantes = new ArrayList<Votante>();
			for(int i = 0; i < mesa.cantidadVotantes(); i++) {
				listaVotantes.add(mesa.getVotante(i));
			}
			
			if (mesa.cantidadVotantes() == 0) {
				System.out.println("Esta mesa no tiene votantes");
			} else {
				for (Votante votante : listaVotantes) {
					System.out.println(votante.obtenerDatos());
				}
			}
		}
	}
	
	public void listarMesasSede(Sede sede) {
		if (sede == null) {
			System.out.println("La sede no existe");
		} else {
			Collection<Mesa> coleccionMesas = sede.getMapaMesas();
			if (coleccionMesas == null) {
				System.out.println("La sede no cuenta con mesas disponibles");
			} else {
				for (Mesa mesa : coleccionMesas) {
					System.out.println("Numero de mesa: " + mesa.getNumeroMesa() + " Capacidad maxima: " + mesa.getCapMax());
				}
			}
		}
	}
	
	public Votante buscarVotanteEnMesa(String rut, Mesa mesa) {
		
		if (mesa == null) {
			System.out.println("La mesa ingresada no existe");
			return null;
		} else {
			try {
				Votante v = mesa.buscarVotante(rut);
				System.out.println("Votante hallado con exito");
				return v;
			} catch (BusquedaFallidaException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
	}
	
	public Mesa buscarMesaEnSede(int numeroMesa, Sede sede) {
		if (sede == null) {
			System.out.println("La sede ingresada no existe");
			return null;
		} else {
			try { 
				Mesa encontrada = sede.buscarMesa(numeroMesa);
				System.out.println("La mesa ha sido encontrada con exito");
				return encontrada;
			} catch (BusquedaFallidaException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
	}
	
	public Votante eliminarVotanteDeMesa(String rut, Mesa mesa) {
		
		if (mesa == null) {
			System.out.println("La mesa ingresada no existe");
			return null;
		} else {
			try {
				Votante eliminado = mesa.eliminarVotante(rut);
				System.out.println("Votante eliminado con exito");
				return eliminado;
			} catch (BusquedaFallidaException e) {
				
				System.out.println(e.getMessage());
				return null;
			}
		}
	}
	
	public Mesa eliminarMesaDeSede(int numeroMesa, Sede sede) {
		if (sede == null) {
			System.out.println("La sede ingresada no existe");
			return null;
		} else {
			try {
				Mesa eliminada = sede.retirarMesa(numeroMesa);
				System.out.println("Mesa eliminada con exito");
				return eliminada;
			} catch (BusquedaFallidaException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
	}
	
	public void modificarNombreVotante(String rut, Mesa mesa, String nuevoNombre) {
		if (mesa == null || mesa.cantidadVotantes() == 0 || rut == null || nuevoNombre == null) {
			System.out.println("La mesa ingresada no existe o bien no tiene votantes asignados, el rut ingresado no es valido, o el nuevo nombre tampoco");
		} else {
			Votante votanteAModificarNombre = buscarVotanteEnMesa(rut, mesa);
			if (votanteAModificarNombre == null) {
				System.out.println("El rut ingresado no corresponde a un votante asignado a esta mesa");
				return;
			} else {
				votanteAModificarNombre.setNombre(nuevoNombre);
				System.out.println("Nombre actualizado con exito");
				return;
			}
		}
	}
	
	public void modificarNombreVotante(Votante votante, String nuevoNombre) {
		if (votante == null || nuevoNombre == null) {
			System.out.println("El votante no existe o bien el nuevo nombre no es valido");
			return;
		} else {
			votante.setNombre(nuevoNombre);
			System.out.println("Nombre actualizado con exito");
			return;
		}
	}
	
	public void modificarResidenciaVotante(String rut, Mesa mesa, Coordenadas nuevoDomicilio) {
		if (mesa == null || mesa.cantidadVotantes() == 0 || rut == null || nuevoDomicilio == null) {
			System.out.println("La mesa no existe o no cuenta con votantes asignados, el rut ingresado no es valido, o el domicilio ingresado no existe");
			return;
		} else {
			Votante votanteAModificarDomicilio = buscarVotanteEnMesa(rut, mesa);
			if (votanteAModificarDomicilio == null) {
				System.out.println("El rut ingresado no corresponde a un votante asignado a esta mesa");
				return;
			} else {
				votanteAModificarDomicilio.setResidencia(nuevoDomicilio);
				return;
			}
		}
	}
	
	public void modificarResidenciaVotante(String rut, Mesa mesa, double xNuevoDomicilio, double yNuevoDomicilio) {
		if (mesa == null || mesa.cantidadVotantes() == 0 || rut == null) {
			System.out.println("La mesa no existe o no cuenta con votantes asignados, o bien el rut ingresado no es valido");
			return;
		} else {
			Votante votanteAModificarDomicilio = buscarVotanteEnMesa(rut, mesa);
			if (votanteAModificarDomicilio == null) {
				System.out.println("El rut ingresado no corresponde a un votante asignado a esta mesa");
				return;
			} else {
				votanteAModificarDomicilio.setResidencia(xNuevoDomicilio, yNuevoDomicilio);
				return;
			}
		}
	}
	
	public void modificarResidenciaVotante(Votante votante, Coordenadas nuevoDomicilio) {
		if (votante == null || nuevoDomicilio == null) {
			System.out.println("El votante o el nuevo domicilio ingresados no existen");
			return;
		} else {
			votante.setResidencia(nuevoDomicilio);
			return;
		}
	}
	
	public void modificarResidenciaVotante(Votante votante, double xNuevoDomicilio, double yNuevoDomicilio) {
		if (votante == null) {
			System.out.println("El votante ingresado no existe");
			return;
		} else {
			votante.setResidencia(xNuevoDomicilio, yNuevoDomicilio);
			return;
		}
	}
	
	public void modificarCapMaxMesa(int numeroMesa, Sede sede, int nuevaCapMax) {
		
		if (sede == null) {
			System.out.println("La sede ingresada no existe");
			return;
		} else {
			
			Mesa mesaAModificarCapMax = buscarMesaEnSede(numeroMesa, sede);
			if (mesaAModificarCapMax == null) {
				return;
			}
			if (mesaAModificarCapMax.cantidadVotantes() == 0) {
				mesaAModificarCapMax.setListaVotantes(new ArrayList<Votante>());
			}
				
			int aux = mesaAModificarCapMax.getCapMax();
			if (!mesaAModificarCapMax.setCapMax(nuevaCapMax)) {
				return;	
			} else {
				try {
					sede.agregarMesa(mesaAModificarCapMax);
					System.out.println("La mesa ha sido agregada con exito");
					return;
				} catch (ExcedeCapacidadException e) {
					mesaAModificarCapMax.setCapMax(aux);
					System.out.println(e.getMessage());
					return;
				}
			}
		}
	}
	public Sede buscarSede(int id) {
		if (sedes == null) {
			return null;
		} else {
			for (Sede sede : sedes) {
				if (sede != null && sede.getId() == id) {
					return sede;
				}
			}
			return null;
		}
	}
	
	public void listarSedes(Vector<Sede> sedes) {
		if (sedes == null || sedes.isEmpty()) {
			System.out.println("No hay sedes disponibles");
			return;
		} else {
			for (Sede sede : sedes) {
				if (sede != null) {
					System.out.println("ID de la sede: " + sede.getId());
					if (sede.getUbicacion() != null)
						System.out.println("Ubicacion: " + sede.getUbicacion().getX() + ", " + sede.getUbicacion().getY());
				}
			}
		}
	}
}
