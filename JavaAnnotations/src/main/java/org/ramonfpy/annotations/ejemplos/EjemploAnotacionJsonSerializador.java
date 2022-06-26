package org.ramonfpy.annotations.ejemplos;

import org.ramonfpy.annotations.ejemplos.models.Producto;
import org.ramonfpy.annotations.ejemplos.procesador.JsonSerializador;

import java.time.LocalDate;

public class EjemploAnotacionJsonSerializador {

    public static void main(String[] args) {

        Producto p1 = new Producto("lámpara led", 269, LocalDate.now());
        Producto p2 = new Producto();
        p2.setNombre("bombilla w3");
        p2.setPrecio(13);
        p2.setFecha(LocalDate.of(2022, 02, 01));

        System.out.println("Json1 = " + JsonSerializador.convertirAJson(p1));
        System.out.println();
        System.out.println("Json2 = " + JsonSerializador.convertirAJson(p2));
    }
}
