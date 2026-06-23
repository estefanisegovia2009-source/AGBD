# Trabajo Practico: API REST CRUD con Spring Boot y Kotlin

## Objetivo

Construir una API REST completa para gestionar un **Inventario de Instrumentos Musicales** utilizando Spring Boot y Kotlin, siguiendo un enfoque guiado por tests (TDD).

Al finalizar este TP, el estudiante sera capaz de:

- Comprender el uso de **anotaciones** en Kotlin para Spring
- Implementar el patron **Null Safety** con tipos nullable (`?`)
- Crear endpoints RESTful para operaciones CRUD
- Utilizar MockMvc para testing de controladores

---

## Dominio: Inventario de Instrumentos Musicales

Se gestionara una unica entidad: **Instrumento** con los siguientes campos:

- `id`: Int (obligatorio)
- `nombre`: String (obligatorio)
- `descripcion`: String? (opcional)

---

## Seccion 0: Capsule Teorica

### 0.1 Anotaciones en Kotlin y Spring

Las **anotaciones** son metadatos que se agregan al codigo para proporcionar informacion adicional al compilador o al framework. En Spring, las anotaciones le dicen al framework como gestionar las clases.

**Sintaxis:**

```kotlin
@NombreDeAnotacion
class MiClase
```

**Anotaciones fundamentales de Spring:**

| Anotacion                | Proposito                                                |
| ------------------------ | -------------------------------------------------------- |
| `@SpringBootApplication` | Marca la clase principal de la aplicacion                |
| `@RestController`        | Marca una clase como controlador REST                    |
| `@GetMapping`            | Mapea solicitudes GET a un metodo                        |
| `@PostMapping`           | Mapea solicitudes POST a un metodo                       |
| `@PutMapping`            | Mapea solicitudes PUT a un metodo                        |
| `@DeleteMapping`         | Mapea solicitudes DELETE a un metodo                     |
| `@RequestBody`           | Indica que el parametro viene del cuerpo de la solicitud |
| `@PathVariable`          | Indica que el parametro viene de la URL                  |
| `@Service`               | Marca una clase como servicio de negocio                 |

**Ejemplo de uso:**

```kotlin
@RestController
@RequestMapping("/api/instrumentos")
class InstrumentoControlador(
    @Autowired private val servicio: InstrumentoServicio
) {
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Instrumento> {
        return ResponseEntity.ok(Instrumento(1, "Guitarra", null))
    }
}
```

---

### 0.2 Null Safety en Kotlin

Kotlin tiene un sistema de **Null Safety** integrado en el tipo de datos. Por defecto, ninguna variable puede ser `null`. Para permitir valores nulos, se usa el simbolo `?`.

**Tipos nullable:**

```kotlin
// Esta variable puede contener un String o null
var descripcion: String? = null

// Esta variable SIEMPRE tendra un String (nunca null)
var nombre: String = "Guitarra"
```

**Acceso seguro con `?`:**

```kotlin
val descripcion: String? = null

// Acceso seguro: retorna null en lugar de lanzar excepcion
val longitud = descripcion?.length

// Operador Elvis: valor por defecto si es null
val longitud = descripcion?.length ?: 0
```

**En funciones:**

```kotlin
// Esta funcion puede retornar null
fun buscarPorId(id: Int): Instrumento? {
    return instrumentos.find { it.id == id }
}
```

---

## Seccion 1: El Modelo

### Requerimiento

Crear la clase de dominio `Instrumento` que represente un instrumento musical.

**Reglas:**

- `id`: tipo `Int`, obligatorio
- `nombre`: tipo `String`, obligatorio
- `descripcion`: tipo `String?` (nullable), opcional

### Implementacion

```kotlin
package etec.crud.model

data class Instrumento(val id: Int, var nombre: String, var descripcion: String?)
```

---

## Seccion 2: El Servicio (Logica de Negocio)

### Requerimiento

Crear la clase `InstrumentoServicio` que gestione una lista en memoria (`MutableList`) con las operaciones CRUD.

**Operaciones requeridas:**

- `guardar(instrumento: Instrumento): Instrumento` - Agrega un instrumento
- `listarTodos(): List<Instrumento>` - Lista todos los instrumentos
- `buscarPorId(id: Int): Instrumento?` - Busca por ID (nullable!)
- `actualizar(id: Int, instrumento: Instrumento): Instrumento?` - Actualiza (nullable!)
- `eliminar(id: Int): Boolean` - Elimina (retorna true/false)

### Implementacion

```kotlin
package etec.crud.service

import etec.crud.model.Instrumento
import org.springframework.stereotype.Service

@Service
class InstrumentoServicio {
    private val instrumentos = mutableListOf<Instrumento>()

    fun guardar(instrumento: Instrumento): Instrumento {
        instrumentos.add(instrumento)
        return instrumento
    }

    fun listarTodos(): List<Instrumento> = instrumentos.toList()

    fun buscarPorId(id: Int): Instrumento? {
        return instrumentos.find { it.id == id }
    }

    fun actualizar(id: Int, instrumento: Instrumento): Instrumento? {
        val indice = instrumentos.indexOfFirst { it.id == id }
        if (indice == -1) return null
        instrumentos[indice] = instrumento
        return instrumento
    }

    fun eliminar(id: Int): Boolean {
        return instrumentos.removeIf { it.id == id }
    }
}
```

---

## Seccion 3: El Controlador (API REST)

### Requerimiento

Crear el controlador REST `InstrumentoControlador` con los siguientes endpoints:

| Metodo | Endpoint                 | Descripcion                  | Estado de exito                |
| ------ | ------------------------ | ---------------------------- | ------------------------------ |
| GET    | `/api/instrumentos`      | Lista todos los instrumentos | 200 OK                         |
| GET    | `/api/instrumentos/{id}` | Obtiene uno por ID           | 200 OK / 404 Not Found         |
| POST   | `/api/instrumentos`      | Crea un nuevo instrumento    | 201 Created / 400 Bad Request  |
| PUT    | `/api/instrumentos/{id}` | Actualiza un instrumento     | 200 OK / 400 Bad Request / 404 Not Found |
| DELETE | `/api/instrumentos/{id}` | Elimina un instrumento       | 204 No Content / 404 Not Found |

**Validacion:** El campo `nombre` no puede estar vacio en POST y PUT.

### Implementacion

```kotlin
package etec.crud.controller

import etec.crud.model.Instrumento
import etec.crud.service.InstrumentoServicio
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/instrumentos")
class InstrumentoControlador(
    @Autowired private val servicio: InstrumentoServicio
) {

    @GetMapping
    fun listarTodos(): List<Instrumento> {
        return servicio.listarTodos()
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Instrumento> {
        val instrumento = servicio.buscarPorId(id)
        return if (instrumento == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(instrumento)
        }
    }

    @PostMapping
    fun crear(@RequestBody instrumento: Instrumento): ResponseEntity<Instrumento> {
        if (instrumento.nombre.isBlank()) {
            return ResponseEntity.badRequest().build()
        }
        val guardado = servicio.guardar(instrumento)
        return ResponseEntity.status(201).body(guardado)
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Int, @RequestBody instrumento: Instrumento): ResponseEntity<Instrumento> {
        if (instrumento.nombre.isBlank()) {
            return ResponseEntity.badRequest().build()
        }
        val actualizado = servicio.actualizar(id, instrumento)
        return if (actualizado == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(actualizado)
        }
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Int): ResponseEntity<Void> {
        val eliminado = servicio.eliminar(id)
        return if (!eliminado) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.noContent().build()
        }
    }
}
```

---

## Comandos para Ejecutar los Tests

Desde la raiz del proyecto:

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar solo los tests de una seccion especifica
./mvnw test -Dtest=InstrumentoTests
./mvnw test -Dtest=InstrumentoServicioTests
./mvnw test -Dtest=InstrumentoControladorTests
```

---

## Criterios de Aprobacion

Para aprobar este TP, debes:

1. **Seccion 1:** Los tests de `InstrumentoTests` pasan (verde)
2. **Seccion 2:** Los tests de `InstrumentoServicioTests` pasan (verde)
3. **Seccion 3:** Los tests de `InstrumentoControladorTests` pasan (verde)

Todos los tests deben estar en verde para considerar el TP como aprobado.

---

## Estructura de Archivos

```
crud/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── kotlin/etec/crud/
│   │   │   ├── CrudApplication.kt
│   │   │   ├── model/
│   │   │   │   └── Instrumento.kt
│   │   │   ├── service/
│   │   │   │   └── InstrumentoServicio.kt
│   │   │   └── controller/
│   │   │       └── InstrumentoControlador.kt
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── kotlin/etec/crud/
│           ├── CrudApplicationTests.kt
│           ├── model/
│           │   └── InstrumentoTests.kt
│           ├── service/
│           │   └── InstrumentoServicioTests.kt
│           └── controller/
│               └── InstrumentoControladorTests.kt
└── README.md
```

---

## Recursos Adicionales

- [Kotlin Null Safety](https://kotlinlang.org/docs/null-safety.html)
- [Spring Boot Annotations](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-annotations)
- [MockMvc Testing](https://spring.io/guides/gs/testing-web/)