package org.ramonfpy.annotations.ejemplos.models;

import org.ramonfpy.annotations.ejemplos.JsonAtributo;

import java.time.LocalDate;
import java.util.Objects;

public class Producto {

    //@JsonAtributo (nombre = "Descripción") //Toma este valor en lugar de nombre.
    @JsonAtributo (capitalizar = true)
    private String nombre;

    @JsonAtributo
    private long precio;
    private LocalDate fecha;

    public Producto() {
    }

    public Producto(String nombre, long precio, LocalDate fecha) {
        this.nombre = nombre;
        this.precio = precio;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return precio == producto.precio && Objects.equals(nombre, producto.nombre) && Objects.equals(fecha, producto.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, precio, fecha);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fecha=" + fecha +
                '}';
    }
}
