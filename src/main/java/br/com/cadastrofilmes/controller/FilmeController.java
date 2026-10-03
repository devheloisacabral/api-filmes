package br.com.cadastrofilmes.controller;

import br.com.cadastrofilmes.model.Filme;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final ConcurrentMap<Long, Filme> filmes = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public FilmeController() {
        cadastrarInicial("O Senhor dos Anéis: A Sociedade do Anel", "Fantasia", 2001);
        cadastrarInicial("Interestelar", "Ficção científica", 2014);
        cadastrarInicial("Parasita", "Drama", 2019);
    }

    @GetMapping
    public List<Filme> listar() {
        return filmes.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Filme buscarPorId(@PathVariable Long id) {
        return filmeExistente(id);
    }

    @PostMapping
    public ResponseEntity<Filme> cadastrar(@Valid @RequestBody FilmeRequest request) {
        Long id = proximoId.getAndIncrement();
        Filme filme = new Filme(id, request.titulo(), request.genero(), request.anoLancamento());
        filmes.put(id, filme);
        return ResponseEntity.status(HttpStatus.CREATED).body(filme);
    }

    @PutMapping("/{id}")
    public Filme atualizar(@PathVariable Long id, @Valid @RequestBody FilmeRequest request) {
        Filme filme = filmeExistente(id);
        filme.setTitulo(request.titulo());
        filme.setGenero(request.genero());
        filme.setAnoLancamento(request.anoLancamento());
        return filme;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (filmes.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    private Filme filmeExistente(Long id) {
        Filme filme = filmes.get(id);
        if (filme == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado");
        }
        return filme;
    }

    private void cadastrarInicial(String titulo, String genero, int anoLancamento) {
        Long id = proximoId.getAndIncrement();
        filmes.put(id, new Filme(id, titulo, genero, anoLancamento));
    }

    public record FilmeRequest(
            @NotBlank(message = "O título é obrigatório") String titulo,
            @NotBlank(message = "O gênero é obrigatório") String genero,
            @NotNull(message = "O ano de lançamento é obrigatório")
            @Min(value = 1888, message = "O ano de lançamento deve ser igual ou posterior a 1888") Integer anoLancamento) {
    }
}
