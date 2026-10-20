package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    public boolean insertar(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("El libro no puede ser null.");
        if (raiz == null) {
            raiz = new NodoLibro(libro);
            return true;
        }
        return insertarDesde(raiz, libro);
    }

    private boolean insertarDesde(NodoLibro nodo, Libro libro) {
        int codigo = libro.getCodigo();
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) return false;
        if (codigo < actual) {
            if (nodo.getIzquierdo() == null) {
                nodo.setIzquierdo(new NodoLibro(libro));
                return true;
            }
            return insertarDesde(nodo.getIzquierdo(), libro);
        }
        if (nodo.getDerecho() == null) {
            nodo.setDerecho(new NodoLibro(libro));
            return true;
        }
        return insertarDesde(nodo.getDerecho(), libro);
    }

    public Libro buscar(int codigo) {
        NodoLibro encontrado = buscarDesde(raiz, codigo);
        return encontrado == null ? null : encontrado.getDato();
    }

    private NodoLibro buscarDesde(NodoLibro nodo, int codigo) {
        if (nodo == null) return null;
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) return nodo;
        return codigo < actual
            ? buscarDesde(nodo.getIzquierdo(), codigo)
            : buscarDesde(nodo.getDerecho(), codigo);
    }

    public int contar() { return contarDesde(raiz); }

    private int contarDesde(NodoLibro nodo) {
        return nodo == null ? 0 : 1 + contarDesde(nodo.getIzquierdo()) + contarDesde(nodo.getDerecho());
    }

    public List<Libro> recorrer(TipoRecorrido tipo) {
        validarTipo(tipo);
        List<Libro> resultado = new ArrayList<>();
        recorrerDesde(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrerDesde(NodoLibro nodo, TipoRecorrido tipo, List<Libro> resultado) {
        if (nodo == null) return;
        if (tipo == TipoRecorrido.PREORDEN) resultado.add(nodo.getDato());
        recorrerDesde(nodo.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) resultado.add(nodo.getDato());
        recorrerDesde(nodo.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) resultado.add(nodo.getDato());
    }

    public String trazaRecorrido(TipoRecorrido tipo) {
        validarTipo(tipo);
        StringBuilder salida = new StringBuilder();
        List<Libro> secuencia = new ArrayList<>();
        trazarDesde(raiz, tipo, 0, salida, secuencia);
        salida.append("Secuencia ").append(tipo).append(":");
        for (Libro libro : secuencia) salida.append(' ').append(libro.getCodigo());
        return salida.toString();
    }

    private void trazarDesde(NodoLibro nodo, TipoRecorrido tipo, int nivel,
                             StringBuilder salida, List<Libro> secuencia) {
        if (nodo == null) {
            indentar(salida, nivel).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        indentar(salida, nivel).append("entra ").append(codigo).append('\n');
        if (tipo == TipoRecorrido.PREORDEN) visitar(nodo, nivel, salida, secuencia);
        trazarDesde(nodo.getIzquierdo(), tipo, nivel + 1, salida, secuencia);
        if (tipo == TipoRecorrido.INORDEN) visitar(nodo, nivel, salida, secuencia);
        trazarDesde(nodo.getDerecho(), tipo, nivel + 1, salida, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) visitar(nodo, nivel, salida, secuencia);
    }

    private void visitar(NodoLibro nodo, int nivel, StringBuilder salida, List<Libro> secuencia) {
        indentar(salida, nivel).append("VISITA ").append(nodo.getDato().getCodigo()).append('\n');
        secuencia.add(nodo.getDato());
    }

    private StringBuilder indentar(StringBuilder salida, int nivel) {
        for (int i = 0; i < nivel; i++) salida.append("  ");
        return salida;
    }

    private void validarTipo(TipoRecorrido tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de recorrido no puede ser null.");
    }

    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        NodoLibro nodo = buscarDesde(raiz, codigo);
        if (nodo == null) return false;
        // Construir primero evita cambios parciales si algún dato es inválido.
        nodo.setDato(new Libro(codigo, titulo, autor, anio));
        return true;
    }

    public boolean eliminar(int codigo) {
        if (buscarDesde(raiz, codigo) == null) return false;
        raiz = eliminarDesde(raiz, codigo);
        return true;
    }

    private NodoLibro eliminarDesde(NodoLibro nodo, int codigo) {
        if (nodo == null) return null;
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminarDesde(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminarDesde(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null) return nodo.getDerecho();
            if (nodo.getDerecho() == null) return nodo.getIzquierdo();
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminarDesde(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return nodo.getIzquierdo() == null ? nodo : minimo(nodo.getIzquierdo());
    }

    public int contarEnRango(int a, int b) {
        if (a > b) return 0;
        return contarRangoDesde(raiz, a, b);
    }

    private int contarRangoDesde(NodoLibro nodo, int a, int b) {
        if (nodo == null) return 0;
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) return contarRangoDesde(nodo.getDerecho(), a, b);
        if (codigo > b) return contarRangoDesde(nodo.getIzquierdo(), a, b);
        return 1 + contarRangoDesde(nodo.getIzquierdo(), a, b)
                 + contarRangoDesde(nodo.getDerecho(), a, b);
    }

    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoDesde(raiz, codigo, camino);
        return camino;
    }

    private void caminoDesde(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) return;
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) caminoDesde(nodo.getIzquierdo(), codigo, camino);
        if (codigo > actual) caminoDesde(nodo.getDerecho(), codigo, camino);
    }

    public boolean esABBValido() { return validarDesde(raiz, Long.MIN_VALUE, Long.MAX_VALUE); }

    private boolean validarDesde(NodoLibro nodo, long minimo, long maximo) {
        if (nodo == null) return true;
        if (nodo.getDato() == null) return false;
        long codigo = nodo.getDato().getCodigo();
        return codigo > 0 && codigo > minimo && codigo < maximo
            && validarDesde(nodo.getIzquierdo(), minimo, codigo)
            && validarDesde(nodo.getDerecho(), codigo, maximo);
    }

    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) throw new IllegalArgumentException("El preorden no puede ser null.");
        int[] posicion = {0};
        CatalogoABB catalogo = new CatalogoABB();
        catalogo.raiz = construirDesdePreorden(preorden, posicion, 0L, Long.MAX_VALUE);
        if (posicion[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no es un preorden ABB válido.");
        }
        return catalogo;
    }

    private static NodoLibro construirDesdePreorden(int[] codigos, int[] posicion,
                                                     long minimo, long maximo) {
        if (posicion[0] == codigos.length) return null;
        int codigo = codigos[posicion[0]];
        if (codigo <= minimo || codigo >= maximo) return null;
        posicion[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Sin especificar", 2026));
        nodo.setIzquierdo(construirDesdePreorden(codigos, posicion, minimo, codigo));
        nodo.setDerecho(construirDesdePreorden(codigos, posicion, codigo, maximo));
        return nodo;
    }
}
