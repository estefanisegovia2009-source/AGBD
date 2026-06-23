package etec.crud.controller

import com.ninjasquad.springmockk.MockkBean
import etec.crud.model.Instrumento
import etec.crud.service.InstrumentoServicio
import io.mockk.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.test.Test

@WebMvcTest(InstrumentoControlador::class)
class InstrumentoControladorTests {
    @Autowired
    lateinit var api: MockMvc

    @MockkBean
    lateinit var instrumentoServicio: InstrumentoServicio

    @Test
    fun `GET todos los instrumentos debe retornar lista vacia inicialmente`() {
        every { instrumentoServicio.listarTodos() } returns emptyList()

        api
            .get("/api/instrumentos")
            .andExpect {
                status { isOk() }
                jsonPath("$") { isArray() }
                jsonPath("$") { isEmpty() }
            }

        verify(exactly = 1) { instrumentoServicio.listarTodos() }
    }

    @Test
    fun `GET todos los instrumentos debe retornar instrumentos existentes`() {
        val instrumentos =
            listOf(
                Instrumento(id = 1, nombre = "Guitarra", descripcion = null),
                Instrumento(id = 2, nombre = "Piano", descripcion = "De cola"),
            )
        every { instrumentoServicio.listarTodos() } returns instrumentos

        api
            .get("/api/instrumentos")
            .andExpect {
                status { isOk() }
                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(2) }
                jsonPath("$[0].nombre") { value("Guitarra") }
                jsonPath("$[1].nombre") { value("Piano") }
            }

        verify(exactly = 1) { instrumentoServicio.listarTodos() }
    }

    @Test
    fun `GET instrumento por id debe retornar instrumento cuando existe`() {
        val instrumento = Instrumento(id = 1, nombre = "Guitarra", descripcion = null)

        every { instrumentoServicio.buscarPorId(1) } returns instrumento

        api
            .get("/api/instrumentos/1")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.nombre") { value("Guitarra") }
            }

        verify(exactly = 1) { instrumentoServicio.buscarPorId(1) }
    }

    @Test
    fun `GET instrumento por id debe retornar 404 cuando no existe`() {
        every { instrumentoServicio.buscarPorId(999) } returns null

        api
            .get("/api/instrumentos/999")
            .andExpect {
                status { isNotFound() }
            }

        verify(exactly = 1) { instrumentoServicio.buscarPorId(999) }
    }

    @Test
    fun `POST debe crear nuevo instrumento y retornarlo`() {
        val nuevoInstrumento = Instrumento(id = 1, nombre = "Guitarra", descripcion = null)
        every { instrumentoServicio.guardar(any()) } returns nuevoInstrumento

        api
            .post("/api/instrumentos") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"id":1,"nombre":"Guitarra","descripcion":null}"""
            }
            .andExpect {
                status { isCreated() }
                jsonPath("$.nombre") { value("Guitarra") }
            }

        verify(exactly = 1) { instrumentoServicio.guardar(any()) }
    }

    @Test
    fun `POST debe retornar 400 cuando nombre esta vacio`() {
        api
            .post("/api/instrumentos") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"id":0,"nombre":"","descripcion":null}"""
            }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `PUT debe actualizar instrumento existente`() {
        val instrumentoActualizado = Instrumento(id = 1, nombre = "Guitarra Acustica", descripcion = "Fender")
        every { instrumentoServicio.actualizar(eq(1), any()) } returns instrumentoActualizado

        api
            .put("/api/instrumentos/1") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"id":1,"nombre":"Guitarra Acustica","descripcion":"Fender"}"""
            }
            .andExpect {
                status { isOk() }
                jsonPath("$.nombre") { value("Guitarra Acustica") }
                jsonPath("$.descripcion") { value("Fender") }
            }

        verify(exactly = 1) { instrumentoServicio.actualizar(eq(1), any()) }
    }

    @Test
    fun `PUT debe retornar 404 cuando instrumento no existe`() {
        val instrumentoActualizado = Instrumento(id = 999, nombre = "Test", descripcion = null)
        every { instrumentoServicio.actualizar(eq(999), any()) } returns null

        api
            .put("/api/instrumentos/999") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"id":999,"nombre":"Test","descripcion":null}"""
            }
            .andExpect {
                status { isNotFound() }
            }

        verify(exactly = 1) { instrumentoServicio.actualizar(eq(999), any()) }
    }

    @Test
    fun `DELETE debe remover instrumento y retornar 204`() {
        every { instrumentoServicio.eliminar(1) } returns true

        api
            .delete("/api/instrumentos/1")
            .andExpect {
                status { isNoContent() }
            }

        verify(exactly = 1) { instrumentoServicio.eliminar(1) }
    }

    @Test
    fun `DELETE debe retornar 404 cuando instrumento no existe`() {
        every { instrumentoServicio.eliminar(999) } returns false

        api
            .delete("/api/instrumentos/999")
            .andExpect {
                status { isNotFound() }
            }

        verify(exactly = 1) { instrumentoServicio.eliminar(999) }
    }
}