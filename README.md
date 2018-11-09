# Décima cuarta práctica - Calendario de eventos

![PUCMMM-logo](https://i.imgur.com/9eEIci9.png)

Décima cuarta práctica realizada para la asignatura **Programación Web Avanzada (ISC-517)** perteneciente a la carrera **Ingeniería de Sistemas y Computación** de la **Pontificia Universidad Católica Madre y Maestra (PUCMM)** en el ciclo **Agosto-Diciembre 2018**.

## Realizado por

**JEAN LOUIS TEJEDA GARCÍA** -  MAT. 2013-1459

**OSCAR DIONISIO NÚÑEZ SIRI** -  MAT. 2014-0056

**CESAR JOSÉ PEÑA MARTE** - MAT. 2013-0488

## Objetivo general
Crear una aplicación web utilizando Vaadin y SpringBoot, basándose en los conceptos, que utilice persistencia en la base de datos gracias a Hibernate (ORM - JPA), y que refleje una aplicación para la creación de eventos y gerentes conjunto con la administración de sus propiedades por medio de un CRUD. Las tareas necesarias para el completimiento de la práctica están en la sección [Tareas requeridas](#tareas-requeridas).

## Vídeo de demostración del proyecto

(Todavía no realizado)

## Tecnologías requeridas

- Java
- SpringBoot
- ORM
- Vaadin
  - Calendar
  - Binder
  - Dataprovider

## Otras tecnologías utilizadas

- Font Awesome 5
- H2

## Modelo de datos
Para esta aplicación es requerido utilizar una colección estática de clases donde se modelan los objetivos de la práctica basándose en las propiedades que tiene que tener la aplicación basándose en los objetivos. 

Estructura de datos creada:
- Eventos
- Usuarios (Gerentes)

## Tareas requeridas

- Debe existir un control de acceso para acceder a la aplicación.
- Debe existir un CRUD de Gerentes para el admin.
- El CRUD debe implementar Binder y Dataprovider con paginación de la información para las tablas.
- Debe existir una opción donde el gerente configure su nombre y correo electrónicos.
- Los eventos debe permitir cambio de fecha en función a la actualización directamente con el componente de calendario.
