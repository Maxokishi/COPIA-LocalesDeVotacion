package package_00;

import java.util.HashMap;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MenuInteractivo {
	
	private Vector<Sede> sedes;
	private HashMap<String, Integer> conteoVotos;
	private ArrayList<Candidato> candidatos;
	
	public MenuInteractivo(Vector<Sede> sedes,  HashMap<String, Integer> conteoVotos, ArrayList<Candidato> candidatos) {
		this.sedes = sedes;
		this.conteoVotos = conteoVotos;
		this.candidatos = candidatos;
	}
	
    public void leerEntradaUsuario() throws IOException { //IOExpection debido a la entrada de los usuarios en distintas opciones
    	
        GestorDeColecciones gestor = new GestorDeColecciones(sedes, candidatos);
    	BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    	int opcion = 0;
    	while(true) {
    		
    		System.out.println("Seleccione un numero");
        	System.out.println("1 - Entrar en modo de consola");
        	System.out.println("2 - Entrar en modo de ventana");
        	System.out.println("3 - Salir");
        	
    		try {
    			opcion = Integer.parseInt(lector.readLine());
    		} catch (IllegalArgumentException e) {
    			System.out.println("Debe ingresar un numero entero!");
    			continue;
    		}
    		
    		switch(opcion) {
    			case 1: //Entra modo consola
    				boolean modoConsolaActivado = true;
    				while (modoConsolaActivado) {
    					System.out.println("Seleccione un numero");
    					System.out.println("1 - Entrar como gestor");
    					System.out.println("2 - Entrar como votante");
    					System.out.println("3 - Volver");
    					int opcionConsola = 0;
    					try {
    						opcionConsola = Integer.parseInt(lector.readLine());
    					} catch (IllegalArgumentException e) {
    						System.out.println("Debe ingresar un numero entero!");
    						continue;
    					}
    				
    					switch(opcionConsola) {
    						case 1:
    							boolean modoGestorActivado = true;
    							while (modoGestorActivado) {
    								System.out.println("Seleccione un numero");
    								System.out.println("1 - Gestionar votantes");
    								System.out.println("2 - Gestionar mesas");
    								System.out.println("3 - Gestionar sedes");
    								System.out.println("4 - Volver");
    								int opcionGestion = 0;
    								try {
    									opcionGestion = Integer.parseInt(lector.readLine());
    								} catch (IllegalArgumentException e) {
    									System.out.println("Debe ingresar un numero entero!");
    									continue;
    								}
    								
    								switch(opcionGestion) {
    									case 1: //Gestionar votantes
    										boolean modoGestorVotantes = true;
    										while (modoGestorVotantes) {
    											System.out.println("Seleccione un numero");
    											System.out.println("1 - Agregar votante a mesa");
    											System.out.println("2 - Listar votantes asignados a una mesa");
    											System.out.println("3 - Buscar un votante en una mesa");
    											System.out.println("4 - Eliminar la asignacion de un votante a una mesa");
    											System.out.println("5 - Modificar el nombre de un votante");
    											System.out.println("6 - Modificar el domicilio de un votante");
    											System.out.println("7 - Volver");
    											int opcionGestionVotantes = 0;
    											try {
    												opcionGestionVotantes = Integer.parseInt(lector.readLine()); //Transforma lo que ingreso el usuario a int.
    											} catch (IllegalArgumentException e) {
    												System.out.println("Debe ingresar un numero entero!");
    												continue;
    											}
    											switch(opcionGestionVotantes) {
    												case 1:
    													
    													boolean esValidoID = false;
    													int idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa a la que se le desea asginar un votante");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sede = gestor.buscarSede(idSede); // Busca la sede de acuerdo a su id
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa a la que desea agregar un votante");
    														int numMesa = -1; // Luego de ingresar la sede, pide la mesa.
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaAgregarVotante = gestor.buscarMesaEnSede(numMesa, sede); //Busca la mesa donde se tiene que agregar el votante
    														if (mesaAgregarVotante == null) {
    															System.out.println("El numero de mesa ingresado no se halla registrado dentro de la sede seleccionada");
    															break;
    														} else {
    															System.out.println("Ingrese los datos del votante que desea agregar a la mesa");
    															System.out.println("Ingrese el rut del votante");
    															String rut = null;
    															try {
    																rut = lector.readLine();
    															} catch (IOException e) {
    																System.out.println("Error al leer el rut, ingrese un String");
    																continue;
    															}
    															System.out.println("Ingrese el nombre del votante");
    															String nombre = null;
    															try {
    																nombre = lector.readLine();
    															} catch (IOException e) {
    																System.out.println("Error al leer el nombre, ingrese un String");
    																continue;
    															}
    															System.out.println("Ingrese la componenete x de la coordenada del votante");
    															double xComponent = 0.0;
    															try {
    																xComponent = Double.parseDouble(lector.readLine());
    															} catch (IllegalArgumentException e) {
    																System.out.println("Error al leer la componente x, ingrese un double");
    																continue;
    															}
    															System.out.println("Ingrese la componenete y de la coordenada del votante");
    															double yComponent = 0.0;
    															try {
    																yComponent = Double.parseDouble(lector.readLine());
    															} catch (IllegalArgumentException e) {
    																System.out.println("Error al leer la componente y, ingrese un double");
    																continue;
    															}
    															Coordenadas residenciaVotanteAgregado = new Coordenadas(xComponent, yComponent);
    															Votante votanteAgregado = new Votante(rut, nombre, residenciaVotanteAgregado); //Crea al votante con las especificaciones
    															try {
    																mesaAgregarVotante.agregarVotante(votanteAgregado); // Agrega el votante
    															} catch (ExcedeCapacidadException e) {
    																System.out.println(e.getMessage());
    																break;
    															}
    														}
    													}
    													break;
    												case 2:
    													esValidoID = false;
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa de la cual desea observar la lista de votantes");
    														try {
    															idSede = Integer.parseInt(lector.readLine()); //Lee el id de la sede deseada 
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													sede = gestor.buscarSede(idSede); // Busca la sede
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa que desea visualizar su lista de votantes");
    														int numMesa = -1;
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaAListarVotantes = gestor.buscarMesaEnSede(numMesa, sede);
    														gestor.listarVotantesMesa(mesaAListarVotantes); //Lista los votantes luego de comprobar que la mesa existia
    													}
    														
    													break;
    												case 3:
    													esValidoID = false;
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa en la que desea buscar un votante");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true; // Valida la id de la sede
    													}
    													sede = gestor.buscarSede(idSede); // Busca la sede
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa en la que desea buscar un votante");
    														int numMesa = -1;
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaBuscarVotante = gestor.buscarMesaEnSede(numMesa, sede); // Busca la mesa
    														System.out.println("Ingrese el RUT del votante que desea buscar en la mesa");
    														String rut = null;
    														try {
    															rut = lector.readLine();
    														} catch (IOException e) {
    															System.out.println("Error al leer el rut, ingrese un String");
    															continue;
    														}
    														Votante buscado = gestor.buscarVotanteEnMesa(rut, mesaBuscarVotante);
    														if (buscado == null) {
    															System.out.println("No se ha hallado el votante en la mesa solicitada");
    														} else {
    															System.out.println(buscado.obtenerDatos());
    														}
    													}
    													break;
    												case 4:
    													esValidoID = false;
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa de la que desea eliminar un votante");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													sede = gestor.buscarSede(idSede);
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa de la que desea eliminar un votante");
    														int numMesa = -1;
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaQuitarVotante = gestor.buscarMesaEnSede(numMesa, sede);
    														System.out.println("Ingrese el RUT del votante que desea eliminar");
    														String rut = null;
    														try {
    															rut = lector.readLine();
    														} catch (IOException e) {
    															System.out.println("Error al leer el rut, ingrese un String");
    															continue;
    														}
    														gestor.eliminarVotanteDeMesa(rut, mesaQuitarVotante);
    													}
    													break;
    												case 5:
    													esValidoID = false;
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa del votante que desea modificar su nombre");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													sede = gestor.buscarSede(idSede);
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa del votante que desea modificar su nombre");
    														int numMesa = -1;
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaModificarVotante = gestor.buscarMesaEnSede(numMesa, sede);
    														System.out.println("Ingrese el RUT del votante que desea modificar su nombre");
    														String rut = null;
    														try {
    															rut = lector.readLine();
    														} catch (IOException e) {
    															System.out.println("Error al leer el rut, ingrese un String");
    															continue;
    														}
    														System.out.println("Ingrese el nuevo nombre del votante");
    														String nuevo = null;
    														try {
    															nuevo = lector.readLine();
    														} catch (IOException e) {
    															System.out.println("Error al leer el nuevo nombre, ingrese un String");
    															continue;
    														}
    														boolean opcionValida = false;
    														int opcionModificarNombreVotante = 0;
    														while (!opcionValida) {
    															System.out.println("Ingrese un numero");
    															System.out.println("1 - Modificar el nombre del votante por medio de su rut y numero de mesa");
    															System.out.println("2 - Modificar el nombre del votante desde el mismo votante");
    															System.out.println("3 - Volver");
    															try {
    																opcionModificarNombreVotante = Integer.parseInt(lector.readLine());
    															} catch (IllegalArgumentException e) {
    																System.out.println("Error al leer la opcion, ingrese un numero entero");
    																continue;
    															}
    															opcionValida = true;
    														}
    														switch (opcionModificarNombreVotante) {
    															case 1:
    																gestor.modificarNombreVotante(rut, mesaModificarVotante, nuevo);
    																break;
    															case 2:
    																Votante votanteModificarNombre = gestor.buscarVotanteEnMesa(rut, mesaModificarVotante);
    																if (votanteModificarNombre == null) {
    																	System.out.println("El votante no se encuentra en la mesa solicitada");
    																} else {
    																	gestor.modificarNombreVotante(votanteModificarNombre, nuevo);
    																}
    																break;
    															case 3:
    																break;
    														}
    													}
    													
    													break;
    												case 6:
    													esValidoID = false;
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la que pertenece la mesa del votante que desea modificar su domicilio");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													sede = gestor.buscarSede(idSede);
    													if (sede == null) {
    														System.out.println("El ID ingresado no corresponde una sede existente");
    														break;
    													} else {
    														System.out.println("Ingrese el numero de la mesa del votante que desea modificar su domicilio");
    														int numMesa = -1;
    														try {
    															numMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														Mesa mesaModificarVotante = gestor.buscarMesaEnSede(numMesa, sede);
    														System.out.println("Ingrese el RUT del votante que desea modificar su domicilio");
    														String rut = null;
    														try {
    															rut = lector.readLine();
    														} catch (IOException e) {
    															System.out.println("Error al leer el rut, ingrese un String");
    															continue;
    														}
    														boolean modoCambiarDomicilio = true;
    														while (modoCambiarDomicilio) {
    															System.out.println("Ingrese un numero");
    															System.out.println("1 - Modificar el domicilio a traves del mismo votante");
    															System.out.println("2 - Modificar el domicilio usando el rut y la mesa del votante");
    															System.out.println("3 - Volver");
    															int opcionModificarDomicilio = 0;
    															try {
    																opcionModificarDomicilio = Integer.parseInt(lector.readLine());
    															} catch (IllegalArgumentException e) {
    																System.out.println("Error al procesar la opcion, ingrese un numero entero!");
    																continue;
    															}
    															switch (opcionModificarDomicilio) {
    																case 1:
    																	Votante v = gestor.buscarVotanteEnMesa(rut, mesaModificarVotante);
    																	boolean modoSeleccionCoordenadas = true;
    																	double xComponent = 0.0;
    																	double yComponent = 0.0;
    																	System.out.println("Ingrese la componente x de la nueva ubicacion");
        																try {
        																	xComponent = Double.parseDouble(lector.readLine());
        																} catch (IllegalArgumentException e) {
        																	System.out.println("Error al leer la componente x, ingrese un double!");
        																	continue;
        																}
        																System.out.println("Ingrese la componente y de la nueva ubicacion");
        																yComponent = 0.0;
        																try {
        																	yComponent = Double.parseDouble(lector.readLine());
        																} catch (IllegalArgumentException e) {
        																	System.out.println("Error al leer la componente y, ingrese un double!");
        																	continue;
        																}
    																	
    																	while (modoSeleccionCoordenadas) {
    																		System.out.println("Ingrese un numero");
    																		System.out.println("1 - Modificar el domicilio usando la ubicacion concreta");
    																		System.out.println("2 - Modificar el domicilio usando las compenentes x e y de forma independiente");
    																		System.out.println("3 - Volver");
    																		int opcionSeleccionCoordenadas = 0;
    																		try {
    																			opcionSeleccionCoordenadas = Integer.parseInt(lector.readLine());
    																		} catch (IllegalArgumentException e) {
    																			System.out.println("Error al leer la opcion, ingrese un numero entero!");
    																			continue;
    																		}
    																		switch (opcionSeleccionCoordenadas) {
    																			case 1:
    																				Coordenadas nuevaUbicacion = new Coordenadas(xComponent, yComponent);
    																				gestor.modificarResidenciaVotante(v,nuevaUbicacion);
    																				System.out.println("Domicilio actualizado con exito");
    																				modoSeleccionCoordenadas = false;
    																				break;
    																			case 2:
    																				gestor.modificarResidenciaVotante(v, xComponent, yComponent);
    																				System.out.println("Domicilio actualizado con exito");
    																				modoSeleccionCoordenadas = false;
    																				break;
    																			case 3:
    																				modoSeleccionCoordenadas = false;
    																				break;
    																			default:
    																				continue;
    																		}
    																	}
    																	break;
    																case 2:
    																	modoSeleccionCoordenadas = true;
    																	xComponent = 0.0;
    																	yComponent = 0.0;
    																	System.out.println("Ingrese la componente x de la nueva ubicacion");
        																try {
        																	xComponent = Double.parseDouble(lector.readLine());
        																} catch (IllegalArgumentException e) {
        																	System.out.println("Error al leer la componente x, ingrese un double!");
        																	continue;
        																}
        																System.out.println("Ingrese la componente y de la nueva ubicacion");
        																yComponent = 0.0;
        																try {
        																	yComponent = Double.parseDouble(lector.readLine());
        																} catch (IllegalArgumentException e) {
        																	System.out.println("Error al leer la componente y, ingrese un double!");
        																	continue;
        																}
        																while (modoSeleccionCoordenadas) {
        																	System.out.println("Ingrese un numero");
        																	System.out.println("1 - Modificar el domicilio usando la ubicacion concreta");
        																	System.out.println("2 - Modificar el domicilio usando las compenentes x e y de forma independiente");
        																	System.out.println("3 - Volver");
        																	int opcionSeleccionCoordenadas = 0;
        																	try {
        																		opcionSeleccionCoordenadas = Integer.parseInt(lector.readLine());
        																	} catch (IllegalArgumentException e) {
        																		System.out.println("Error al leer la opcion, ingrese un numero entero!");
        																		continue;
        																	}
        																	switch (opcionSeleccionCoordenadas) {
        																		case 1:
        																			Coordenadas nuevaUbicacion = new Coordenadas(xComponent, yComponent);
        																			gestor.modificarResidenciaVotante(rut, mesaModificarVotante,nuevaUbicacion);
        																			System.out.println("Domicilio actualizado con exito");
        																			modoSeleccionCoordenadas = false;
        																			break;
        																		case 2:
        																			gestor.modificarResidenciaVotante(rut, mesaModificarVotante, xComponent, yComponent);
        																			System.out.println("Domicilio actualizado con exito");
        																			modoSeleccionCoordenadas = false;
        																			break;
        																		case 3:
        																			modoSeleccionCoordenadas = false;
        																			break;
        																		default:
        																			continue;
        																	}
    																	}
    														
    																	break;
 
    																case 3:
    																	modoCambiarDomicilio = false;
    																	continue;
    																default:
    																	continue;	
    															}
    															
    														}
    													}
    													break;
    												case 7:
    													modoGestorVotantes = false;
    													break;
    												default: 
    													System.out.println("La opcion ingresada no es valida");	
    											}
    										}
    										break;
    									case 2:
    										boolean modoGestionMesas = true;
    										while (modoGestionMesas) {
    											System.out.println("Ingrese un numero");
    											System.out.println("1 - Agregar una mesa a sede");
    											System.out.println("2 - Listar las mesas pertenecientes a una sede");
    											System.out.println("3 - Buscar una mesa");
    											System.out.println("4 - Retirar una mesa de una sede");
    											System.out.println("5 - Modificar la capacidad maxima de una mesa");
    											System.out.println("6 - Volver");
    											int opcionGestionMesas = 0;
    											try {
    												opcionGestionMesas = Integer.parseInt(lector.readLine());
    											} catch (IllegalArgumentException e) {
    												System.out.println("Error al procesar la opcion, ingrese un numero entero!");
    												continue;
    											}
    											switch (opcionGestionMesas) {
    												case 1:
    													boolean esValidoID = false;
    													boolean esValidoNumMesa = false;
    													boolean esValidaCapMax = false;
    													int idSede = -1;
    													int numMesaNueva = 0;
    													int capMaxMesa = 0;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede a la desea agregar una mesa");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sedeAgregarMesa = gestor.buscarSede(idSede);
    													if (sedeAgregarMesa == null) {
    														System.out.println("La sede ingresada no existe");
    														break;
    													}
    													while (!esValidoNumMesa) {
    														System.out.println("Ingrese un numero de mesa");
    														try {
    															numMesaNueva = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Error al leer el numero de mesa, ingrese un valor entero");
    															continue;
    														}
    														esValidoNumMesa = true;
    													}
    													while (!esValidaCapMax) {
    														System.out.println("Ingrese la capacidad maxima de la mesa que desea agregar");
    														try {
    															capMaxMesa = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Error al leer la capacidad maxima, ingrese un valor entero");
    															continue;
    														}
    														esValidaCapMax = true;
    													}
    													Mesa nueva = new Mesa(numMesaNueva, capMaxMesa, gestor.construirPlantillaVotos());
    													gestor.agregarMesaASede(nueva, sedeAgregarMesa);
    													break;
    												case 2:
    													esValidoID = false;	
    													idSede = -1;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede donde se encuentran las mesas que desea listar");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sedeListarMesas = gestor.buscarSede(idSede);
    													if (sedeListarMesas == null) {
    														System.out.println("La sede ingresada no existe");
    														break;
    													}
    													gestor.listarMesasSede(sedeListarMesas);
    													break;
    												case 3:
    													esValidoID = false;
    													esValidoNumMesa = false;
    													idSede = -1;
    													int numMesaBuscada = 0;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede en la que desea buscar una mesa");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sedeBuscarMesa = gestor.buscarSede(idSede);
    													if (sedeBuscarMesa == null) {
    														System.out.println("La sede ingresada no existe");
    														break;
    													}
    													while (!esValidoNumMesa) {
    														System.out.println("Ingrese un numero de mesa");
    														try {
    															numMesaBuscada = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Error al leer el numero de mesa, ingrese un valor entero");
    															continue;
    														}
    														esValidoNumMesa = true;
    													}
    													Mesa buscada = gestor.buscarMesaEnSede(numMesaBuscada, sedeBuscarMesa);
    													if (buscada != null) {
    														System.out.println("Mesa numero " + buscada.getNumeroMesa() + " encontrada con exito");
    														System.out.println("Capacidad maxima de la mesa: " + buscada.getCapMax());
    														if (buscada.cantidadVotantes() != 0) {
    															System.out.println("Lista de votantes asignados a esta mesa: ");
    															gestor.listarVotantesMesa(buscada);
    														}
    													} else {
    														System.out.println("La mesa no ha sido encontrada");
    													}
    													break;
    												case 4:
    													esValidoID = false;
    													esValidoNumMesa = false;
    													idSede = -1;
    													int numMesaAQuitar = 0;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede en la que se encuentra la mesa que desea eliminar");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sedeQuitarMesa = gestor.buscarSede(idSede);
    													if (sedeQuitarMesa == null) {
    														System.out.println("La sede ingresada no existe");
    														break;
    													}
    													while (!esValidoNumMesa) {
    														System.out.println("Ingrese un numero de mesa");
    														try {
    															numMesaAQuitar = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Error al leer el numero de mesa, ingrese un valor entero");
    															continue;
    														}
    														esValidoNumMesa = true;
    													}
    													Mesa eliminada = gestor.eliminarMesaDeSede(numMesaAQuitar, sedeQuitarMesa);
    													if (eliminada == null) {
    														System.out.println("La mesa no pudo ser eliminada porque no se encuentra en esta sede");
    													} else {
    														System.out.println("La mesa ha sido eliminada con exito");
    													}
    													break;
    												case 5:
    													esValidoID = false;
    													esValidoNumMesa = false;
    													idSede = -1;
    													int numMesaAModificar = 0;
    													while(!esValidoID) {
    														System.out.println("Ingrese el ID de la sede en la que se encuentra la mesa cuya capacidad maxima desea modificar");
    														try {
    															idSede = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Valor invalido, ingrese un numero entero!");
    															continue;
    														}
    														esValidoID = true;
    													}
    													Sede sedeModificarMesa = gestor.buscarSede(idSede);
    													if (sedeModificarMesa == null) {
    														System.out.println("La sede ingresada no existe");
    														break;
    													}
    													while (!esValidoNumMesa) {
    														System.out.println("Ingrese un numero de mesa");
    														try {
    															numMesaAModificar = Integer.parseInt(lector.readLine());
    														} catch (IllegalArgumentException e) {
    															System.out.println("Error al leer el numero de mesa, ingrese un valor entero");
    															continue;
    														}
    														esValidoNumMesa = true;
    													}
    													Mesa modificada = gestor.buscarMesaEnSede(numMesaAModificar, sedeModificarMesa);
    													if (modificada == null) {
    														System.out.println("La mesa no pudo ser encontrada en esta sede");
    													} else {
    														esValidaCapMax = false;
    														int nuevaCapMax = 0;
    														while (!esValidaCapMax) {
    															System.out.println("Ingrese la nueva capacidad maxima de la mesa");
    															try {
    																nuevaCapMax = Integer.parseInt(lector.readLine());
    															} catch (IllegalArgumentException e) {
    																System.out.println("Error al leer la capacidad maxima, intente ingresando un entero");
    																continue;
    															}
    															esValidaCapMax = true;
    														}
    														gestor.modificarCapMaxMesa(numMesaAModificar, sedeModificarMesa, nuevaCapMax);
    													}
    													break;
    												case 6:
    													modoGestionMesas = false;
    													break;
    												default:
    													System.out.println("El numero ingresado no corresponde a una opcion valida");
    													continue;
    											}
    										}
    										break;
    									case 3:
    										boolean modoGestionSedes = true;
    										while (modoGestionSedes) {
    											System.out.println("Ingrese un numero");
    											System.out.println("1 - Buscar una sede por ID");
    											System.out.println("2 - Listar sedes");
    											System.out.println("3 - Volver");
    											int opcionGestionSedes = 0;
    											try {
    												opcionGestionSedes = Integer.parseInt(lector.readLine());
    											} catch (IllegalArgumentException e) {
    												System.out.println("Error al procesar la opcion, ingrese un numero entero!");
    												continue;
    											}
    											switch (opcionGestionSedes) {
    												case 1:
    													int idSedeBuscada = 0;
    													System.out.println("Ingrese el ID de la sede que busca");
    													try {
    														idSedeBuscada = Integer.parseInt(lector.readLine());
    													} catch (IllegalArgumentException e) {
    														System.out.println("Error al procesar la opcion, ingrese un numero entero!");
    														continue;
    													}
    													Sede buscada = gestor.buscarSede(idSedeBuscada);
    													if (buscada != null) {
    														System.out.println("Sede encontrada con exito");
    														if (buscada.getUbicacion() != null) {
    															System.out.println("Ubicacion de la sede: " + buscada.getUbicacion().getX() + ", " + buscada.getUbicacion().getY());
    														}
    													} else {
    														System.out.println("No se ha encontrado la sede solicitada");
    													}
    													break;
    												case 2:
    													gestor.listarSedes(sedes);
    													break;
    												case 3:
    													modoGestionSedes = false;
    													break;
    												default:
    													System.out.println("La opcion ingresada no es valida");
    													continue;
    											}
    										}
    										break;
    									case 4:
    										modoGestorActivado = false;
    										break;
    									default:
    										System.out.println("La opcion ingresada no es valida");	
    								}
    							}
    							
    							break;
    						case 2:
    							System.out.println("1.- Mostrar candidatos");
    							int opcionVotante = 0;
    							try {
    	    						opcionVotante = Integer.parseInt(lector.readLine());
    	    					} catch (IllegalArgumentException e) {
    	    						System.out.println("Debe ingresar un numero entero!");
    	    						continue;
    	    					}
    							
    							switch(opcionVotante) {
    								case 1:
    									gestor.listarCandidatos(candidatos);
    									break;
    								case 2:
    									break;
    								default:
    									System.out.println("La opcion ingresada no es valida");
    										
    							}
    						case 3:
    							modoConsolaActivado = false;
    							break;
    						default:
    							System.out.println("La opcion ingresada no es valida");	
    				
    					}
    				}
    				
    				break;
    			case 2:
    				javax.swing.SwingUtilities.invokeLater(() -> { //Llama a la ventana en el caso de que se presione la opcion 2.
    					Ventana ventanaPrincipal = new Ventana(gestor, sedes, conteoVotos);
    					ventanaPrincipal.setLocationRelativeTo(null);
    					ventanaPrincipal.setVisible(true);
    				});
    				break;
    			case 3: //Acaba el sistema
    				return;
    			default: //Error por si coloca un numero que no es una opcion
    				System.out.println("La opción ingresada no es valida, intente ingresando 1 o 2");
    				continue;
    		}
    	}
    }
}
