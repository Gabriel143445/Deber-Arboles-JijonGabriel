package negocio;

import enums.TipoUnidad;
import java.util.List;
import modelo.UnidadBiblioteca;

public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca biblioteca) {
        if (biblioteca == null || biblioteca.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una BIBLIOTECA.");
        }
        raiz = new NodoUnidad(biblioteca);
    }

    public NodoUnidad getRaiz() { return raiz; }

    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (nueva == null || nueva.getTipo() == TipoUnidad.BIBLIOTECA) return false;
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || buscar(nueva.getCodigo()) != null
                || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) return false;
        padre.getHijos().add(new NodoUnidad(nueva));
        return true;
    }

    public NodoUnidad buscar(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;
        return buscarDesde(raiz, codigo.trim());
    }

    private NodoUnidad buscarDesde(NodoUnidad actual, String codigo) {
        if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) return actual;
        for (NodoUnidad hijo : actual.getHijos()) {
            NodoUnidad hallado = buscarDesde(hijo, codigo);
            if (hallado != null) return hallado;
        }
        return null;
    }

    public int altura() { return alturaDesde(raiz); }

    private int alturaDesde(NodoUnidad actual) {
        int mayor = 0;
        for (NodoUnidad hijo : actual.getHijos()) {
            mayor = Math.max(mayor, 1 + alturaDesde(hijo));
        }
        return mayor;
    }

    public int contarHojas() { return hojasDesde(raiz); }

    private int hojasDesde(NodoUnidad actual) {
        if (actual.getHijos().isEmpty()) return 1;
        int total = 0;
        for (NodoUnidad hijo : actual.getHijos()) total += hojasDesde(hijo);
        return total;
    }

    public int nivel(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return -1;
        return nivelDesde(raiz, codigo.trim(), 0);
    }

    private int nivelDesde(NodoUnidad actual, String codigo, int nivel) {
        if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) return nivel;
        for (NodoUnidad hijo : actual.getHijos()) {
            int hallado = nivelDesde(hijo, codigo, nivel + 1);
            if (hallado != -1) return hallado;
        }
        return -1;
    }

    public String mostrarJerarquia() {
        StringBuilder salida = new StringBuilder();
        jerarquiaDesde(raiz, 0, salida);
        return salida.toString().stripTrailing();
    }

    private void jerarquiaDesde(NodoUnidad actual, int nivel, StringBuilder salida) {
        for (int i = 0; i < nivel; i++) salida.append("  ");
        salida.append(actual.getDato()).append('\n');
        for (NodoUnidad hijo : actual.getHijos()) jerarquiaDesde(hijo, nivel + 1, salida);
    }

    public int contarLibrosTotal() { return librosDesde(raiz); }

    private int librosDesde(NodoUnidad actual) {
        int total = actual.getCatalogo() == null ? 0 : actual.getCatalogo().contar();
        for (NodoUnidad hijo : actual.getHijos()) total += librosDesde(hijo);
        return total;
    }
}
