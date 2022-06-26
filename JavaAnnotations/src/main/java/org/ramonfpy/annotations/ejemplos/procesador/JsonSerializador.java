package org.ramonfpy.annotations.ejemplos.procesador;

import org.ramonfpy.annotations.ejemplos.JsonAtributo;
import org.ramonfpy.annotations.ejemplos.procesador.exception.JsonSerializadorException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JsonSerializador {

    public static String convertirAJson(Object object) throws JsonSerializadorException {

        if (Objects.isNull(object)){
            throw new JsonSerializadorException("El objeto a serializar no puede ser null!");
        }
        Field[] atributos = object.getClass().getDeclaredFields();

        return Arrays.stream(atributos)
                .filter(field -> field.isAnnotationPresent(JsonAtributo.class))
                .map(field -> {
                    field.setAccessible(true); //Añadir esto para poder acceder a los atributos private de producto.
                    String nombre = field.getAnnotation(JsonAtributo.class)
                            .nombre()
                            .equals("") ? field.getName() : field.getAnnotation(JsonAtributo.class).nombre();
                    try {
                        Object valor = field.get(object);
                        if (field.getAnnotation(JsonAtributo.class).capitalizar()
                                && valor instanceof String) {
                            String nuevoValor = (String) valor;
                            //nuevoValor = nuevoValor.substring(0,1).toUpperCase()
                            nuevoValor = String.valueOf(nuevoValor.charAt(0)).toUpperCase()
                                    + nuevoValor.substring(1).toLowerCase();
                            field.set(object, nuevoValor);
                        }
                        return "\"" + nombre + "\":\"" + field.get(object) + "\"";
                    } catch (IllegalAccessException e) {
                        throw new JsonSerializadorException("Error al serializar a Json!" + e.getMessage());
                    }
                })
                .reduce("{", (a, b) -> {
                    if ("{".equals(a)) {
                        return a + b;
                    } else {
                        return a + ", " + b;
                    }
                }).concat("}");
    }
}
