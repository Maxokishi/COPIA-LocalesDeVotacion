package package_00;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;

public class Ventana extends JFrame {

    private JTextArea areaTexto;
    private JPanel panelSubOpciones;
    private GestorDeColecciones gestor;
    private Vector<Sede> sedes;
    private HashMap<String, Integer> conteoVotos;
    /**
     * 
     * @param gestor se usa para el manejo de los datos
     * @param sedes se necesita para inicializar el gestor como corresponde
     * @param conteoVotos para calcular los votos totales y sacar un resultado.
     */
    public Ventana(GestorDeColecciones gestor, Vector<Sede> sedes, HashMap<String, Integer> conteoVotos) {
        this.gestor = gestor;
        this.sedes = sedes;
        this.conteoVotos = conteoVotos;
        // Funciones para que la ventana abra, definir tam, y que se cierre la ejecucion al momento de cerrar la ventana
        this.setSize(700, 600);
        this.setTitle("Ventana con Opciones");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        setLayout(new BorderLayout(10, 10));

        JPanel panelOpciones = crearPanelOpciones();
        add(panelOpciones, BorderLayout.NORTH);
        
        // Agrega las distintas opciones luego de elegir ya una opcion
        panelSubOpciones = new JPanel();
        panelSubOpciones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSubOpciones.setBorder(BorderFactory.createTitledBorder("Subopciones"));
        panelSubOpciones.setBackground(new Color(240, 248, 255));
        add(panelSubOpciones, BorderLayout.CENTER);
        // Para que cada boton tenga su texto designado
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setPreferredSize(new Dimension(680, 200));
        add(scroll, BorderLayout.SOUTH);

        areaTexto.append("Bienvenido! Selecciona una opción...\n");
    }

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        /**
        * Crea las opciones principales, cada gestion llama al metodo de sus subopciones correspondientes
        * Boton de guardar, es para ir actualizando los datos segun hacemos las acciones
        */
        JButton gestionVotantes = crearBoton("Gestion de Votantes", new Color(245, 196, 113));
        JButton gestionMesas = crearBoton("Gestion de Mesas", new Color(62, 96, 163));
        JButton gestionSedes = crearBoton("Gestion de Sedes", new Color(245, 196, 113));
        JButton btnGuardar = crearBoton("Guardar Datos", new Color(75, 128, 64));
        JButton btnLimpiar = crearBoton("Limpiar", Color.RED);

        gestionVotantes.addActionListener(e -> mostrarSubOpcionesVotantes());
        gestionMesas.addActionListener(e -> mostrarSubOpcionesMesas());
        gestionSedes.addActionListener(e -> mostrarSubOpcionesSedes());
        btnGuardar.addActionListener(e -> {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            System.setOut(printStream);

            ControlPersistenciaDeDatos.guardar(gestor);

            System.out.flush();
            System.setOut(originalOut);

            String salidaCapturada = baos.toString();
            if (!salidaCapturada.isEmpty()) {
                areaTexto.append(salidaCapturada);
            }
        });
        btnLimpiar.addActionListener(e -> {
            areaTexto.setText("");
            panelSubOpciones.removeAll();
            panelSubOpciones.revalidate();
            panelSubOpciones.repaint();
        });

        panel.add(gestionVotantes);
        panel.add(gestionMesas);
        panel.add(gestionSedes);
        panel.add(btnGuardar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void mostrarSubOpcionesVotantes() {
        panelSubOpciones.removeAll();
        panelSubOpciones.setBorder(BorderFactory.createTitledBorder("Subopciones - Opción 1"));

        JButton agregarVotante = crearBotonSub("Agregar votante", new Color(245, 196, 113));
        JButton listarVotantesAsigMesa = crearBotonSub("Listar votantes asignados a una mesa", new Color(245, 196, 113));
        JButton buscarVotanteMesa = crearBotonSub("Buscar un votante en una mesa", new Color(245, 196, 113));
        JButton eliminarVotanteAsigMesa = crearBotonSub("Eliminar la asignacion de un votante a una mesa", new Color(245, 196, 113));
        JButton modificarVotanteNombre = crearBotonSub("Modificar el nombre de un votante", new Color(245, 196, 113));
        JButton modificarVotanteDomicilio = crearBotonSub("Modificar el domicilio de un votante", new Color(245, 196, 113));
        JButton btnVolver = crearBotonSub("⬅ Volver", Color.GRAY);

        agregarVotante.addActionListener(e -> {
            /**
             * Hace las mismas acciones que el caso del menu interactivo, cambia algunos elementos de logica y especialmente
             * de lector del teclado del usuario, por opciones de la clase JFRAME, pasa lo mismo con todas las subopciones
             * dependiendo claramente del modo de gestion (votante, mesa, sede) y la subopcion elegida
             */
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(
                        this,
                        "Ingrese el ID de la sede a la que pertenece la mesa a la que se le desea asignar un votante"
                );
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sede = gestor.buscarSede(idSede);
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(
                        this,
                        "Ingrese el numero de la mesa a la que desea agregar un votante"
                );
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaAgregarVotante = gestor.buscarMesaEnSede(numMesa, sede);
                if (mesaAgregarVotante == null) {
                    areaTexto.append("El numero de mesa ingresado no se halla registrado dentro de la sede seleccionada\n");
                    return;
                } else {
                    areaTexto.append("Ingrese los datos del votante que desea agregar a la mesa\n");

                    String rut = JOptionPane.showInputDialog(this, "Ingrese el rut del votante");
                    if (rut == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }

                    String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del votante");
                    if (nombre == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }

                    String inputX = JOptionPane.showInputDialog(this, "Ingrese la componente x de la coordenada del votante");
                    if (inputX == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }
                    double xComponent = 0.0;
                    try {
                        xComponent = Double.parseDouble(inputX.trim());
                    } catch (NumberFormatException ex) {
                        areaTexto.append("Error al leer la componente x, ingrese un double\n");
                        return;
                    }

                    String inputY = JOptionPane.showInputDialog(this, "Ingrese la componente y de la coordenada del votante");
                    if (inputY == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }
                    double yComponent = 0.0;
                    try {
                        yComponent = Double.parseDouble(inputY.trim());
                    } catch (NumberFormatException ex) {
                        areaTexto.append("Error al leer la componente y, ingrese un double\n");
                        return;
                    }

                    Coordenadas residenciaVotanteAgregado = new Coordenadas(xComponent, yComponent);
                    Votante votanteAgregado = new Votante(rut, nombre, residenciaVotanteAgregado);
                    try {
                        mesaAgregarVotante.agregarVotante(votanteAgregado);
                        areaTexto.append("Votante agregado correctamente\n");
                    } catch (ExcedeCapacidadException ex) {
                        areaTexto.append(ex.getMessage() + "\n");
                        return;
                    }
                }
            }
        });

        listarVotantesAsigMesa.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this,"Ingrese el ID de la sede a la que pertenece la mesa de la cual desea observar la lista de votantes");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sede = gestor.buscarSede(idSede);
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(this, "Ingrese el numero de la mesa que desea visualizar su lista de votantes");
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaAListarVotantes = gestor.buscarMesaEnSede(numMesa, sede);

                PrintStream originalOut = System.out;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(baos);
                System.setOut(printStream);

                gestor.listarVotantesMesa(mesaAListarVotantes);

                System.out.flush();
                System.setOut(originalOut);

                String salidaCapturada = baos.toString();
                if (!salidaCapturada.isEmpty()) {
                    areaTexto.append(salidaCapturada);
                } else {
                    areaTexto.append("No hay votantes en esta mesa\n");
                }
            }
        });

        buscarVotanteMesa.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog( this,"Ingrese el ID de la sede a la que pertenece la mesa en la que desea buscar un votante");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true; // Valida la id de la sede
            }

            Sede sede = gestor.buscarSede(idSede); // Busca la sede
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(this, "Ingrese el numero de la mesa en la que desea buscar un votante");
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaBuscarVotante = gestor.buscarMesaEnSede(numMesa, sede); // Busca la mesa

                String rut = JOptionPane.showInputDialog(this, "Ingrese el RUT del votante que desea buscar en la mesa");
                if (rut == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }

                Votante buscado = gestor.buscarVotanteEnMesa(rut, mesaBuscarVotante);
                if (buscado == null) {
                    areaTexto.append("No se ha hallado el votante en la mesa solicitada\n");
                } else {
                    areaTexto.append(buscado.obtenerDatos() + "\n");
                }
            }
        });

        eliminarVotanteAsigMesa.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede a la que pertenece la mesa de la que desea eliminar un votante");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sede = gestor.buscarSede(idSede);
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(this, "Ingrese el numero de la mesa de la que desea eliminar un votante");
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaQuitarVotante = gestor.buscarMesaEnSede(numMesa, sede);

                String rut = JOptionPane.showInputDialog(this, "Ingrese el RUT del votante que desea eliminar");
                if (rut == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }

                gestor.eliminarVotanteDeMesa(rut, mesaQuitarVotante);
            }
        });

        modificarVotanteNombre.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede a la que pertenece la mesa del votante que desea modificar su nombre");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sede = gestor.buscarSede(idSede);
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(this, "Ingrese el numero de la mesa del votante que desea modificar su nombre");
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaModificarVotante = gestor.buscarMesaEnSede(numMesa, sede);

                String rut = JOptionPane.showInputDialog(this, "Ingrese el RUT del votante que desea modificar su nombre");
                if (rut == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }

                String nuevo = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre del votante");
                if (nuevo == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                boolean opcionValida = false;
                int opcionModificarNombreVotante = 0;
                while (!opcionValida) {
                    String[] opciones = {"1 - Modificar el nombre por RUT y mesa",
                        "2 - Modificar el nombre desde el votante",
                        "3 - Volver"};
                    String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione una opción:", "Modificar nombre de votante", JOptionPane.QUESTION_MESSAGE,
                            null, opciones, opciones[0]
                    );
                    if (seleccion == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }
                    try {
                        opcionModificarNombreVotante = Integer.parseInt(seleccion.substring(0, 1));
                    } catch (NumberFormatException ex) {
                        areaTexto.append("Error al leer la opcion, ingrese un numero entero\n");
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
                            areaTexto.append("El votante no se encuentra en la mesa solicitada\n");
                        } else {
                            gestor.modificarNombreVotante(votanteModificarNombre, nuevo);
                        }
                        break;
                    case 3:
                        areaTexto.append("Operación cancelada por el usuario\n");
                        return;
                    default:
                        areaTexto.append("Opción no válida\n");
                        break;
                }
            }
        });

        modificarVotanteDomicilio.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede a la que pertenece la mesa del votante que desea modificar su domicilio");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sede = gestor.buscarSede(idSede);
            if (sede == null) {
                areaTexto.append("El ID ingresado no corresponde una sede existente\n");
                return;
            } else {
                String inputMesa = JOptionPane.showInputDialog(this, "Ingrese el numero de la mesa del votante que desea modificar su domicilio");
                if (inputMesa == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                int numMesa = -1;
                try {
                    numMesa = Integer.parseInt(inputMesa.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    return;
                }

                Mesa mesaModificarVotante = gestor.buscarMesaEnSede(numMesa, sede);

                String rut = JOptionPane.showInputDialog(this, "Ingrese el RUT del votante que desea modificar su domicilio");
                if (rut == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }

                boolean modoCambiarDomicilio = true;
                while (modoCambiarDomicilio) {
                    String[] opcionesMenu = {
                        "1 - Modificar el domicilio a traves del mismo votante",
                        "2 - Modificar el domicilio usando el rut y la mesa del votante",
                        "3 - Volver"
                    };
                    String seleccion = (String) JOptionPane.showInputDialog(this, "Ingrese un numero\n"
                            + "1 - Modificar el domicilio a traves del mismo votante\n"
                            + "2 - Modificar el domicilio usando el rut y la mesa del votante\n"
                            + "3 - Volver",
                            "Modificar domicilio",
                            JOptionPane.QUESTION_MESSAGE,
                            null, opcionesMenu, opcionesMenu[0]
                    );
                    if (seleccion == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }

                    int opcionModificarDomicilio = 0;
                    try {
                        opcionModificarDomicilio = Integer.parseInt(seleccion.substring(0, 1));
                    } catch (NumberFormatException ex) {
                        areaTexto.append("Error al procesar la opcion, ingrese un numero entero!\n");
                        continue;
                    }

                    // Variables compartidas entre cases (declaradas antes del switch)
                    boolean modoSeleccionCoordenadas;
                    double xComponent;
                    double yComponent;

                    switch (opcionModificarDomicilio) {
                        case 1:
                            Votante v = gestor.buscarVotanteEnMesa(rut, mesaModificarVotante);
                            modoSeleccionCoordenadas = true;
                            xComponent = 0.0;
                            yComponent = 0.0;

                            String inputX1 = JOptionPane.showInputDialog(this, "Ingrese la componente x de la nueva ubicacion");
                            if (inputX1 == null) {
                                areaTexto.append("Operación cancelada\n");
                                return;
                            }
                            try {
                                xComponent = Double.parseDouble(inputX1.trim());
                            } catch (NumberFormatException ex) {
                                areaTexto.append("Error al leer la componente x, ingrese un double!\n");
                                continue;
                            }

                            String inputY1 = JOptionPane.showInputDialog(this, "Ingrese la componente y de la nueva ubicacion");
                            if (inputY1 == null) {
                                areaTexto.append("Operación cancelada\n");
                                return;
                            }
                            try {
                                yComponent = Double.parseDouble(inputY1.trim());
                            } catch (NumberFormatException ex) {
                                areaTexto.append("Error al leer la componente y, ingrese un double!\n");
                                continue;
                            }

                            while (modoSeleccionCoordenadas) {
                                String[] opcionesCoord = {
                                    "1 - Modificar el domicilio usando la ubicacion concreta",
                                    "2 - Modificar el domicilio usando las compenentes x e y de forma independiente",
                                    "3 - Volver"
                                };
                                String selCoord = (String) JOptionPane.showInputDialog(this, "Ingrese un numero\n"
                                        + "1 - Modificar el domicilio usando la ubicacion concreta\n"
                                        + "2 - Modificar el domicilio usando las compenentes x e y de forma independiente\n"
                                        + "3 - Volver",
                                        "Modificar domicilio",
                                        JOptionPane.QUESTION_MESSAGE,
                                        null, opcionesCoord, opcionesCoord[0]
                                );
                                if (selCoord == null) {
                                    areaTexto.append("Operación cancelada\n");
                                    return;
                                }

                                int opcionSeleccionCoordenadas = 0;
                                try {
                                    opcionSeleccionCoordenadas = Integer.parseInt(selCoord.substring(0, 1));
                                } catch (NumberFormatException ex) {
                                    areaTexto.append("Error al leer la opcion, ingrese un numero entero!\n");
                                    continue;
                                }

                                switch (opcionSeleccionCoordenadas) {
                                    case 1:
                                        Coordenadas nuevaUbicacion = new Coordenadas(xComponent, yComponent);
                                        gestor.modificarResidenciaVotante(v, nuevaUbicacion);
                                        areaTexto.append("Domicilio actualizado con exito\n");
                                        modoSeleccionCoordenadas = false;
                                        break;
                                    case 2:
                                        gestor.modificarResidenciaVotante(v, xComponent, yComponent);
                                        areaTexto.append("Domicilio actualizado con exito\n");
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

                            String inputX2 = JOptionPane.showInputDialog(this, "Ingrese la componente x de la nueva ubicacion");
                            if (inputX2 == null) {
                                areaTexto.append("Operación cancelada\n");
                                return;
                            }
                            try {
                                xComponent = Double.parseDouble(inputX2.trim());
                            } catch (NumberFormatException ex) {
                                areaTexto.append("Error al leer la componente x, ingrese un double!\n");
                                continue;
                            }

                            String inputY2 = JOptionPane.showInputDialog(this, "Ingrese la componente y de la nueva ubicacion");
                            if (inputY2 == null) {
                                areaTexto.append("Operación cancelada\n");
                                return;
                            }
                            try {
                                yComponent = Double.parseDouble(inputY2.trim());
                            } catch (NumberFormatException ex) {
                                areaTexto.append("Error al leer la componente y, ingrese un double!\n");
                                continue;
                            }

                            while (modoSeleccionCoordenadas) {
                                String[] opcionesCoord = {
                                    "1 - Modificar el domicilio usando la ubicacion concreta",
                                    "2 - Modificar el domicilio usando las compenentes x e y de forma independiente",
                                    "3 - Volver"
                                };
                                String selCoord = (String) JOptionPane.showInputDialog(this, "Ingrese un numero\n"
                                        + "1 - Modificar el domicilio usando la ubicacion concreta\n"
                                        + "2 - Modificar el domicilio usando las compenentes x e y de forma independiente\n"
                                        + "3 - Volver",
                                        "Modificar domicilio",
                                        JOptionPane.QUESTION_MESSAGE,
                                        null, opcionesCoord, opcionesCoord[0]
                                );
                                if (selCoord == null) {
                                    areaTexto.append("Operación cancelada\n");
                                    return;
                                }

                                int opcionSeleccionCoordenadas = 0;
                                try {
                                    opcionSeleccionCoordenadas = Integer.parseInt(selCoord.substring(0, 1));
                                } catch (NumberFormatException ex) {
                                    areaTexto.append("Error al leer la opcion, ingrese un numero entero!\n");
                                    continue;
                                }

                                switch (opcionSeleccionCoordenadas) {
                                    case 1:
                                        Coordenadas nuevaUbicacion = new Coordenadas(xComponent, yComponent);
                                        gestor.modificarResidenciaVotante(rut, mesaModificarVotante, nuevaUbicacion);
                                        areaTexto.append("Domicilio actualizado con exito\n");
                                        modoSeleccionCoordenadas = false;
                                        break;
                                    case 2:
                                        gestor.modificarResidenciaVotante(rut, mesaModificarVotante, xComponent, yComponent);
                                        areaTexto.append("Domicilio actualizado con exito\n");
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
        });

        btnVolver.addActionListener(e -> {
            panelSubOpciones.removeAll();
            panelSubOpciones.revalidate();
            panelSubOpciones.repaint();
        });

        panelSubOpciones.add(agregarVotante);
        panelSubOpciones.add(listarVotantesAsigMesa);
        panelSubOpciones.add(buscarVotanteMesa);
        panelSubOpciones.add(eliminarVotanteAsigMesa);
        panelSubOpciones.add(modificarVotanteNombre);
        panelSubOpciones.add(modificarVotanteDomicilio);
        panelSubOpciones.add(btnVolver);

        panelSubOpciones.revalidate();
        panelSubOpciones.repaint();
    }

    private void mostrarSubOpcionesMesas() {
        panelSubOpciones.removeAll();
        panelSubOpciones.setBorder(BorderFactory.createTitledBorder("Subopciones - Opción 2"));

        JButton agregarMeseASede = crearBotonSub("Agregar mesa a sede", new Color(62, 96, 163));
        JButton listarMesasDeSede = crearBotonSub("Listar mesas de una sede", new Color(62, 96, 163));
        JButton buscarMesa = crearBotonSub("Buscar una mesa", new Color(62, 96, 163));
        JButton retirarMesaSede = crearBotonSub("Retirar mesas de una sede", new Color(62, 96, 163));
        JButton modificarCapMaxMesa = crearBotonSub("Modificar la cantidad maxima de votantes en una mesa", new Color(62, 96, 163));
        JButton btnVolver = crearBotonSub("⬅ Volver", Color.GRAY);

        agregarMeseASede.addActionListener(e -> {
            boolean esValidoID = false;
            boolean esValidoNumMesa = false;
            boolean esValidaCapMax = false;
            int idSede = -1;
            int numMesaNueva = 0;
            int capMaxMesa = 0;

            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede a la desea agregar una mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sedeAgregarMesa = gestor.buscarSede(idSede);
            if (sedeAgregarMesa == null) {
                areaTexto.append("La sede ingresada no existe\n");
                return;
            }

            while (!esValidoNumMesa) {
                String input = JOptionPane.showInputDialog(this,"Ingrese un numero de mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    numMesaNueva = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Error al leer el numero de mesa, ingrese un valor entero\n");
                    continue;
                }
                esValidoNumMesa = true;
            }

            while (!esValidaCapMax) {
                String input = JOptionPane.showInputDialog(this, "Ingrese la capacidad maxima de la mesa que desea agregar");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    capMaxMesa = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Error al leer la capacidad maxima, ingrese un valor entero\n");
                    continue;
                }
                esValidaCapMax = true;
            }

            Mesa nueva = new Mesa(numMesaNueva, capMaxMesa, new HashMap<String, Integer>(conteoVotos));
            gestor.agregarMesaASede(nueva, sedeAgregarMesa);
        });

        listarMesasDeSede.addActionListener(e -> {
            boolean esValidoID = false;
            int idSede = -1;
            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede donde se encuentran las mesas que desea listar");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sedeListarMesas = gestor.buscarSede(idSede);
            if (sedeListarMesas == null) {
                areaTexto.append("La sede ingresada no existe\n");
                return;
            }

            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            System.setOut(printStream);

            // Llamar al método que imprime en consola
            gestor.listarMesasSede(sedeListarMesas);

            // Restaurar System.out
            System.out.flush();
            System.setOut(originalOut);

            // Mostrar lo capturado en el área de texto
            String salidaCapturada = baos.toString();
            if (!salidaCapturada.isEmpty()) {
                areaTexto.append(salidaCapturada);
            }
        });

        buscarMesa.addActionListener(e -> {
            boolean esValidoID = false;
            boolean esValidoNumMesa = false;
            int idSede = -1;
            int numMesaBuscada = 0;

            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this,"Ingrese el ID de la sede en la que desea buscar una mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sedeBuscarMesa = gestor.buscarSede(idSede);
            if (sedeBuscarMesa == null) {
                areaTexto.append("La sede ingresada no existe\n");
                return;
            }

            while (!esValidoNumMesa) {
                String input = JOptionPane.showInputDialog(this, "Ingrese un numero de mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    numMesaBuscada = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Error al leer el numero de mesa, ingrese un valor entero\n");
                    continue;
                }
                esValidoNumMesa = true;
            }

            Mesa buscada = gestor.buscarMesaEnSede(numMesaBuscada, sedeBuscarMesa);
            if (buscada != null) {
                areaTexto.append("Mesa numero " + buscada.getNumeroMesa() + " encontrada con exito\n");
                areaTexto.append("Capacidad maxima de la mesa: " + buscada.getCapMax() + "\n");
                if (buscada.cantidadVotantes() > 0) {
                    areaTexto.append("Lista de votantes asignados a esta mesa: \n");

                    // ============================================================
                    // REDIRIGIR System.out PARA CAPTURAR LA SALIDA DEL MÉTODO
                    // ============================================================
                    PrintStream originalOut = System.out;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(baos);
                    System.setOut(printStream);

                    // Llamar al método que imprime en consola
                    gestor.listarVotantesMesa(buscada);

                    // Restaurar System.out
                    System.out.flush();
                    System.setOut(originalOut);

                    // Mostrar lo capturado en el área de texto
                    String salidaCapturada = baos.toString();
                    if (!salidaCapturada.isEmpty()) {
                        areaTexto.append(salidaCapturada);
                    }
                }
            } else {
                areaTexto.append("La mesa no ha sido encontrada\n");
            }
        });

        retirarMesaSede.addActionListener(e -> {
            boolean esValidoID = false;
            boolean esValidoNumMesa = false;
            int idSede = -1;
            int numMesaAQuitar = 0;

            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede en la que se encuentra la mesa que desea eliminar");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sedeQuitarMesa = gestor.buscarSede(idSede);
            if (sedeQuitarMesa == null) {
                areaTexto.append("La sede ingresada no existe\n");
                return;
            }

            while (!esValidoNumMesa) {
                String input = JOptionPane.showInputDialog(this, "Ingrese un numero de mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    numMesaAQuitar = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Error al leer el numero de mesa, ingrese un valor entero\n");
                    continue;
                }
                esValidoNumMesa = true;
            }

            Mesa eliminada = gestor.eliminarMesaDeSede(numMesaAQuitar, sedeQuitarMesa);
            if (eliminada == null) {
                areaTexto.append("La mesa no pudo ser eliminada porque no se encuentra en esta sede\n");
            } else {
                areaTexto.append("La mesa ha sido eliminada con exito\n");
            }
        });

        modificarCapMaxMesa.addActionListener(e -> {
            boolean esValidoID = false;
            boolean esValidoNumMesa = false;
            boolean esValidaCapMax = false;
            int idSede = -1;
            int numMesaAModificar = 0;

            while (!esValidoID) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede en la que se encuentra la mesa cuya capacidad maxima desea modificar");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    idSede = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Valor invalido, ingrese un numero entero!\n");
                    continue;
                }
                esValidoID = true;
            }

            Sede sedeModificarMesa = gestor.buscarSede(idSede);
            if (sedeModificarMesa == null) {
                areaTexto.append("La sede ingresada no existe\n");
                return;
            }

            while (!esValidoNumMesa) {
                String input = JOptionPane.showInputDialog(this, "Ingrese un numero de mesa");
                if (input == null) {
                    areaTexto.append("Operación cancelada\n");
                    return;
                }
                try {
                    numMesaAModificar = Integer.parseInt(input.trim());
                } catch (NumberFormatException ex) {
                    areaTexto.append("Error al leer el numero de mesa, ingrese un valor entero\n");
                    continue;
                }
                esValidoNumMesa = true;
            }

            Mesa modificada = gestor.buscarMesaEnSede(numMesaAModificar, sedeModificarMesa);
            if (modificada == null) {
                areaTexto.append("La mesa no pudo ser encontrada en esta sede\n");
            } else {
                esValidaCapMax = false;
                int nuevaCapMax = 0;
                while (!esValidaCapMax) {
                    String input = JOptionPane.showInputDialog(this, "Ingrese la nueva capacidad maxima de la mesa");
                    if (input == null) {
                        areaTexto.append("Operación cancelada\n");
                        return;
                    }
                    try {
                        nuevaCapMax = Integer.parseInt(input.trim());
                    } catch (NumberFormatException ex) {
                        areaTexto.append("Error al leer la capacidad maxima, intente ingresando un entero\n");
                        continue;
                    }
                    esValidaCapMax = true;
                }
                gestor.modificarCapMaxMesa(numMesaAModificar, sedeModificarMesa, nuevaCapMax);
            }
        });

        btnVolver.addActionListener(e -> {
            panelSubOpciones.removeAll();
            panelSubOpciones.revalidate();
            panelSubOpciones.repaint();
        });

        panelSubOpciones.add(agregarMeseASede);
        panelSubOpciones.add(listarMesasDeSede);
        panelSubOpciones.add(buscarMesa);
        panelSubOpciones.add(retirarMesaSede);
        panelSubOpciones.add(modificarCapMaxMesa);
        panelSubOpciones.add(btnVolver);

        panelSubOpciones.revalidate();
        panelSubOpciones.repaint();
    }

    private void mostrarSubOpcionesSedes() {
        panelSubOpciones.removeAll();
        panelSubOpciones.setBorder(BorderFactory.createTitledBorder("Subopciones - Opción 3"));

        JButton buscarSedeId = crearBotonSub("Buscar una sede por su id", new Color(245, 196, 113));
        JButton listarSedes = crearBotonSub("Listar sedes", new Color(245, 196, 113));
        JButton btnVolver = crearBotonSub("⬅ Volver", Color.GRAY);

        buscarSedeId.addActionListener(e -> {
            int idSedeBuscada = 0;

            String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la sede que busca");
            if (input == null) {
                areaTexto.append("Operación cancelada\n");
                return;
            }
            try {
                idSedeBuscada = Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                areaTexto.append("Error al procesar la opcion, ingrese un numero entero!\n");
                return;
            }

            Sede buscada = gestor.buscarSede(idSedeBuscada);
            if (buscada != null) {
                areaTexto.append("Sede encontrada con exito\n");
                if (buscada.getUbicacion() != null) {
                    areaTexto.append("Ubicacion de la sede: " + buscada.getUbicacion().getX() + ", " + buscada.getUbicacion().getY() + "\n");
                }
            } else {
                areaTexto.append("No se ha encontrado la sede solicitada\n");
            }
        });

        listarSedes.addActionListener(e -> {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            System.setOut(printStream);

            // Llamar al método que imprime en consola
            gestor.listarSedes(sedes);

            // Restaurar System.out
            System.out.flush();
            System.setOut(originalOut);

            // Mostrar lo capturado en el área de texto
            String salidaCapturada = baos.toString();
            if (!salidaCapturada.isEmpty()) {
                areaTexto.append(salidaCapturada);
            }
        });

        btnVolver.addActionListener(e -> {
            panelSubOpciones.removeAll();
            panelSubOpciones.revalidate();
            panelSubOpciones.repaint();
        });

        panelSubOpciones.add(buscarSedeId);
        panelSubOpciones.add(listarSedes);
        panelSubOpciones.add(btnVolver);

        panelSubOpciones.revalidate();
        panelSubOpciones.repaint();
    }
    /**
     * Son las funciones para crear los botones, asignarles el color, letra y dimension respectiva
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(150, 45));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return boton;
    }

    private JButton crearBotonSub(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(160, 40));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return boton;
    }
}
