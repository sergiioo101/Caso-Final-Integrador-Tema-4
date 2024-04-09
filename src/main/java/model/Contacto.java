package model;

import java.io.Serializable;

public class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String email;
    private String telefono;

    public Contacto(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "nombre = '" + nombre + '\'' +
                ", email = '" + email + '\'' +
                ", telefono = '" + telefono + '\'' +
                '}';
    }
}

