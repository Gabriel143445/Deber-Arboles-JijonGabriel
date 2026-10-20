package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public class Main {
    private final Scanner entrada = new Scanner(System.in);
    private ArbolBiblioteca biblioteca = DatosPrueba.cargarBibliotecaInicial();

    public static void main(String[] args) {
        new Main().ejecutar();
    }

    private void ejecutar() {
        System.out.println("Catálogo de la Biblioteca FISEI");
        while (true) {
            menu();
            int opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> { biblioteca = DatosPrueba.cargarBibliotecaInicial(); System.out.println("Estructura inicial cargada."); }
                    case 2 -> System.out.println(biblioteca.mostrarJerarquia());
                    case 3 -> agregarUnidad();
                    case 4 -> buscarUnidad();
                    case 5 -> consultarEstructura();
                    case 6 -> insertarLibro();
                    case 7 -> buscarLibro();
                    case 8 -> modificarLibro();
                    case 9 -> eliminarLibro();
                    case 10 -> mostrarRecorrido();
                    case 11 -> ejecutarRetos();
                    case 0 -> { System.out.println("Hasta luego."); return; }
                    default -> System.out.println("Elige una opción del menú.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Dato inválido: " + error.getMessage());
            }
        }
    }

    private void menu() {
        System.out.println("\n1. Recargar datos iniciales");
        System.out.println("2. Mostrar jerarquía");
        System.out.println("3. Agregar unidad");
        System.out.println("4. Buscar unidad");
        System.out.println("5. Altura, hojas, nivel y total de libros");
        System.out.println("6. Insertar libro");
        System.out.println("7. Buscar libro");
        System.out.println("8. Modificar libro");
        System.out.println("9. Eliminar libro");
        System.out.println("10. Recorridos y trazas");
        System.out.println("11. Retos del ABB");
        System.out.println("0. Salir");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try { return Integer.parseInt(texto); }
            catch (NumberFormatException error) { System.out.println("Ingresa un número entero."); }
        }
    }

    private void agregarUnidad() {
        String padre = leerTexto("Código del padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        String tipo = leerTexto("Tipo (AREA o ESTANTE): ").toUpperCase();
        TipoUnidad tipoUnidad = TipoUnidad.valueOf(tipo);
        boolean agregado = biblioteca.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipoUnidad));
        System.out.println(agregado ? "Unidad agregada." : "No se pudo agregar: revisa padre, tipo y código.");
    }

    private void buscarUnidad() {
        NodoUnidad nodo = biblioteca.buscar(leerTexto("Código de la unidad: "));
        System.out.println(nodo == null ? "Unidad no encontrada." :
            nodo.getDato() + " (" + nodo.getDato().getTipo() + ")");
    }

    private void consultarEstructura() {
        System.out.println("Altura: " + biblioteca.altura() + " aristas");
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Libros en total: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar nivel: ");
        System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
    }

    private CatalogoABB seleccionarEstante() {
        NodoUnidad nodo = biblioteca.buscar(leerTexto("Código del estante: "));
        if (nodo == null || nodo.getCatalogo() == null) {
            System.out.println("Ese código no corresponde a un estante.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private void insertarLibro() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        int codigo = leerEntero("Código positivo: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año: ");
        System.out.println(catalogo.insertar(new Libro(codigo, titulo, autor, anio))
            ? "Libro insertado." : "Ese código ya existe en el estante.");
    }

    private void buscarLibro() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        Libro libro = catalogo.buscar(leerEntero("Código del libro: "));
        System.out.println(libro == null ? "Libro no encontrado." : libro);
    }

    private void modificarLibro() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año: ");
        System.out.println(catalogo.modificar(codigo, titulo, autor, anio)
            ? "Libro modificado." : "Libro no encontrado.");
    }

    private void eliminarLibro() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        System.out.println(catalogo.eliminar(leerEntero("Código del libro: "))
            ? "Libro eliminado." : "Libro no encontrado.");
    }

    private TipoRecorrido seleccionarRecorrido() {
        System.out.println("1. Preorden  2. Inorden  3. Posorden");
        return switch (leerEntero("Recorrido: ")) {
            case 1 -> TipoRecorrido.PREORDEN;
            case 2 -> TipoRecorrido.INORDEN;
            case 3 -> TipoRecorrido.POSORDEN;
            default -> throw new IllegalArgumentException("Recorrido desconocido.");
        };
    }

    private void mostrarRecorrido() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        TipoRecorrido tipo = seleccionarRecorrido();
        System.out.println(catalogo.trazaRecorrido(tipo));
    }

    private void ejecutarRetos() {
        CatalogoABB catalogo = seleccionarEstante();
        if (catalogo == null) return;
        int a = leerEntero("Inicio del rango: ");
        int b = leerEntero("Fin del rango: ");
        System.out.println("Libros en el rango: " + catalogo.contarEnRango(a, b));
        int codigo = leerEntero("Código para mostrar el camino: ");
        List<Integer> camino = catalogo.caminoBusqueda(codigo);
        System.out.println("Camino: " + camino);
        System.out.println("¿Es un ABB válido? " + catalogo.esABBValido());
    }
}
