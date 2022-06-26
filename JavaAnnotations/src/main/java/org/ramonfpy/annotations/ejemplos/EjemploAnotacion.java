package org.ramonfpy.annotations.ejemplos;

import org.ramonfpy.annotations.ejemplos.models.Producto;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

public class EjemploAnotacion {

    public static void main(String[] args) {

        Producto p1 = new Producto("Lámpara led", 269, LocalDate.now());
        Producto p2 = new Producto();
        p2.setNombre("Bombilla w3");
        p2.setPrecio(13);
        p2.setFecha(LocalDate.of(2022, 02, 01));

        Field[] atributos = p1.getClass().getDeclaredFields();

        String json = Arrays.stream(atributos)
                .filter(field -> field.isAnnotationPresent(JsonAtributo.class))
                .map(field -> {
                    field.setAccessible(true); //Añadir esto para poder acceder a los atributos private de producto.
                    String nombre = field.getAnnotation(JsonAtributo.class)
                            .nombre()
                            .equals("") ? field.getName() : field.getAnnotation(JsonAtributo.class).nombre();
                    try {
                        return "\"" + nombre + "\":\"" + field.get(p1) + "\"";
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error al serializar a Json!" + e.getMessage());
                    }
                })
                .reduce("{", (a, b) -> {
                    if ("{".equals(a)) {
                        return a + b;
                    } else {
                        return a + ", " + b;
                    }
                }).concat("}");

        System.out.println("json = " + json);
    }
}
