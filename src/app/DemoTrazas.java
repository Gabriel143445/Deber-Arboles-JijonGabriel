package app;

import enums.TipoRecorrido;
import java.util.List;
import modelo.Libro;
import negocio.CatalogoABB;

/** Reproduce el ejercicio personal de TRAZAS.md para poder comparar la solución manual. */
public class DemoTrazas {
    private static String codigos(List<Libro> libros) {
        StringBuilder salida = new StringBuilder();
        for (Libro libro : libros) {
            if (salida.length() > 0) salida.append(' ');
            salida.append(libro.getCodigo());
        }
        return salida.toString();
    }

    private static void mostrar(String titulo, CatalogoABB catalogo) {
        System.out.println(titulo);
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println(tipo + ": " + codigos(catalogo.recorrer(tipo)));
        }
        System.out.println("ABB válido: " + catalogo.esABBValido());
    }

    public static void main(String[] args) {
        CatalogoABB catalogo = DatosPrueba.cargarBibliotecaInicial()
            .buscar("SW-E1").getCatalogo();
        System.out.println("Códigos personales: 86, 61 (60 ya existía), 1 (de 01)");
        DatosPrueba.insertarCodigos(catalogo, new int[] {86, 61, 1});
        mostrar("Árbol tras las tres inserciones:", catalogo);
        System.out.println("\nTraza del preorden:");
        System.out.println(catalogo.trazaRecorrido(TipoRecorrido.PREORDEN));

        System.out.println("\nEliminar 1 (hoja): " + catalogo.eliminar(1));
        mostrar("Árbol resultante:", catalogo);
        System.out.println("\nEliminar 90 (un hijo): " + catalogo.eliminar(90));
        mostrar("Árbol resultante:", catalogo);
        System.out.println("\nEliminar 60 (dos hijos): " + catalogo.eliminar(60));
        mostrar("Árbol resultante:", catalogo);
    }
}
