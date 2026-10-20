package modelo;

public class Libro {
    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código del libro debe ser positivo.");
        }
        validarTexto(titulo, "El título");
        validarTexto(autor, "El autor");
        validarAnio(anio);
        this.codigo = codigo;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
    }

    private static void validarAnio(int anio) {
        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2026.");
        }
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }

    public void setTitulo(String titulo) {
        validarTexto(titulo, "El título");
        this.titulo = titulo.trim();
    }

    public void setAutor(String autor) {
        validarTexto(autor, "El autor");
        this.autor = autor.trim();
    }

    public void setAnio(int anio) {
        validarAnio(anio);
        this.anio = anio;
    }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + autor + ", " + anio + ")";
    }
}
