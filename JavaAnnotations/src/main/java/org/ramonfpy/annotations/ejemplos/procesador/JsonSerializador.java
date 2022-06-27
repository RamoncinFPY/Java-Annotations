package org.ramonfpy.annotations.ejemplos.procesador;

import org.ramonfpy.annotations.ejemplos.Init;
import org.ramonfpy.annotations.ejemplos.JsonAtributo;
import org.ramonfpy.annotations.ejemplos.procesador.exception.JsonSerializadorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonSerializador {

    public static void inicializarObjeto(Object object){
        if(Objects.isNull(object)){
            throw new JsonSerializadorException("El objeto a serializar no puede ser null!");
        }
        Method[] metodos = object.getClass().getDeclaredMethods();
        Arrays.stream(metodos).filter(m -> m.isAnnotationPresent(Init.class))
                .forEach(m-> {
                    m.setAccessible(true);
                    try {
                        m.invoke(object);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new JsonSerializadorException(
                                "Error al serializar, no se puede iniciar el objeto"
                                        + e.getMessage());
                    }
                });
    }

    public static String convertirAJson(Object object) throws JsonSerializadorException {

        if (Objects.isNull(object)) {
            throw new JsonSerializadorException("El objeto a serializar no puede ser null!");
        }
        inicializarObjeto(object);
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
                            /*nuevoValor = String.valueOf(nuevoValor.charAt(0)).toUpperCase()
                                    + nuevoValor.substring(1).toLowerCase();*/

                            nuevoValor = Arrays.stream(nuevoValor.split(" "))
                                    .map(palabra -> palabra.substring(0, 1).toUpperCase()
                                            + palabra.substring(1).toLowerCase())
                                    .collect(Collectors.joining(" "));

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
