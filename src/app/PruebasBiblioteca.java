package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public class PruebasBiblioteca {
    private int correctas;
    private int total;

    public static void main(String[] args) {
        new PruebasBiblioteca().ejecutar();
    }

    private void comprobar(String nombre, boolean condicion) {
        total++;
        if (condicion) correctas++;
        System.out.println((condicion ? "OK   " : "FALLÓ ") + nombre);
    }

    private static String codigos(List<Libro> libros) {
        StringBuilder salida = new StringBuilder();
        for (Libro libro : libros) {
            if (salida.length() > 0) salida.append(' ');
            salida.append(libro.getCodigo());
        }
        return salida.toString();
    }

    private void ejecutar() {
        ArbolBiblioteca soloRaiz = new ArbolBiblioteca(
            new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        comprobar("Raíz sola: altura 0", soloRaiz.altura() == 0);
        comprobar("Raíz sola: una hoja", soloRaiz.contarHojas() == 1);

        ArbolBiblioteca arbol = DatosPrueba.cargarBibliotecaInicial();
        CatalogoABB sw = arbol.buscar("SW-E1").getCatalogo();
        comprobar("Biblioteca inicial: altura 2", arbol.altura() == 2);
        comprobar("Biblioteca inicial: cuatro hojas", arbol.contarHojas() == 4);
        comprobar("Biblioteca inicial: trece libros", arbol.contarLibrosTotal() == 13);
        comprobar("Niveles 0, 1, 2 y -1", arbol.nivel("BIB") == 0
            && arbol.nivel("sw") == 1 && arbol.nivel("SW-E2") == 2
            && arbol.nivel("XYZ") == -1);
        comprobar("No se admite estante bajo biblioteca", !arbol.agregar("BIB",
            new UnidadBiblioteca("X", "Estante inválido", TipoUnidad.ESTANTE)));
        comprobar("No se admiten códigos de unidad repetidos", !arbol.agregar("TI",
            new UnidadBiblioteca("SW-E1", "Repetido", TipoUnidad.ESTANTE)));
        comprobar("Preorden de SW-E1", codigos(sw.recorrer(TipoRecorrido.PREORDEN))
            .equals("60 35 20 45 40 80 70 75 90"));
        comprobar("Inorden de SW-E1", codigos(sw.recorrer(TipoRecorrido.INORDEN))
            .equals("20 35 40 45 60 70 75 80 90"));
        comprobar("Posorden de SW-E1", codigos(sw.recorrer(TipoRecorrido.POSORDEN))
            .equals("20 40 45 35 75 70 90 80 60"));
        comprobar("Código repetido no se inserta", !sw.insertar(
            new Libro(60, "Repetido", "Autor", 2026)));
        comprobar("Camino de búsqueda de 42", sw.caminoBusqueda(42)
            .equals(List.of(60, 35, 45, 40)));
        comprobar("Cinco libros entre 40 y 75", sw.contarEnRango(40, 75) == 5);
        comprobar("Traza con secuencia correcta", sw.trazaRecorrido(TipoRecorrido.PREORDEN)
            .endsWith("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));
        comprobar("Buscar inexistente devuelve null", sw.buscar(999) == null);
        comprobar("Modificar inexistente devuelve false", !sw.modificar(999, "X", "Y", 2026));
        comprobar("Eliminar inexistente devuelve false", !sw.eliminar(999));

        boolean datosInvalidos = false;
        try { new Libro(0, "", "", 1899); }
        catch (IllegalArgumentException esperado) { datosInvalidos = true; }
        comprobar("Libro inválido lanza excepción", datosInvalidos);

        sw.insertar(new Libro(42, "Libro 42", "Autor", 2026));
        comprobar("Eliminar hoja 20", sw.eliminar(20));
        comprobar("Eliminar nodo 70 con un hijo", sw.eliminar(70));
        comprobar("Eliminar raíz 60 con dos hijos", sw.eliminar(60));
        comprobar("Preorden tras las tres eliminaciones",
            codigos(sw.recorrer(TipoRecorrido.PREORDEN)).equals("75 35 45 40 42 80 90"));
        comprobar("ABB sigue válido después de eliminar", sw.esABBValido());

        CatalogoABB alterado = new CatalogoABB();
        NodoLibro nodo = new NodoLibro(new Libro(60, "Raíz", "Autor", 2026));
        nodo.setIzquierdo(new NodoLibro(new Libro(35, "Hijo", "Autor", 2026)));
        nodo.getIzquierdo().setDerecho(new NodoLibro(new Libro(70, "Nieto mal ubicado", "Autor", 2026)));
        alterado.setRaiz(nodo);
        comprobar("Detecta nieto mal ubicado", !alterado.esABBValido());

        CatalogoABB reconstruido = CatalogoABB.desdePreorden(
            new int[] {60, 35, 20, 45, 40, 80, 70, 75, 90});
        comprobar("Reconstrucción desde preorden", reconstruido.esABBValido()
            && codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN))
                .equals("60 35 20 45 40 80 70 75 90"));

        System.out.println("\n" + correctas + "/" + total + " pruebas OK");
        if (correctas != total) System.exit(1);
    }
}
