package negocio;

import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.List;
import modelo.UnidadBiblioteca;

public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos = new ArrayList<>();
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) throw new IllegalArgumentException("La unidad no puede ser null.");
        this.dato = dato;
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() { return dato; }
    public List<NodoUnidad> getHijos() { return hijos; }
    public CatalogoABB getCatalogo() { return catalogo; }
}
