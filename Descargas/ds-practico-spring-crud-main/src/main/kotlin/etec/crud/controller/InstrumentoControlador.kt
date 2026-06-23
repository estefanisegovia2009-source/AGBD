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
