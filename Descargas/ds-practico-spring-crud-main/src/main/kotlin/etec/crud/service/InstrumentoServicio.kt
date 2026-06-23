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
