package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

public final class DatosPrueba {
    private DatosPrueba() { }

    public static ArbolBiblioteca cargarBibliotecaInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
            new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        insertarCodigos(arbol.buscar("SW-E1").getCatalogo(),
            new int[] {60, 35, 80, 20, 45, 70, 90, 40, 75});
        insertarCodigos(arbol.buscar("SW-E2").getCatalogo(), new int[] {15, 10, 25});
        insertarCodigos(arbol.buscar("TI-E1").getCatalogo(), new int[] {50});
        return arbol;
    }

    public static void insertarCodigos(CatalogoABB catalogo, int[] codigos) {
        for (int codigo : codigos) {
            catalogo.insertar(new Libro(codigo, "Libro " + codigo, "Autor de prueba", 2026));
        }
    }
}
