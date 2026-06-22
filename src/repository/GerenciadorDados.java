package repository;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import exception.EntidadeNaoEncontradaException;
import model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GerenciadorDados<T> {

    private final List<T> lista = new ArrayList<>();
    private final Class<T> tipo;
    private final String caminhoArquivo;
    private final Function<T, Integer> idExtractor;
    private final Gson gson;

    public GerenciadorDados(Class<T> tipo, String nomeArquivo, Function<T, Integer> idExtractor) {
        this.tipo = tipo;
        this.caminhoArquivo = "data/" + nomeArquivo + ".json";
        this.idExtractor = idExtractor;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, t, ctx) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, t, ctx) -> LocalDate.parse(json.getAsString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, t, ctx) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, t, ctx) -> LocalDateTime.parse(json.getAsString()))
                .registerTypeAdapter(Produto.class, (JsonSerializer<Produto>) (src, t, ctx) -> {
                    JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();
                    obj.addProperty("tipo", src.getClass().getSimpleName());
                    return obj;
                })
                .registerTypeAdapter(Produto.class, (JsonDeserializer<Produto>) (json, t, ctx) -> {
                    JsonObject obj = json.getAsJsonObject();
                    String tipoConcreto = obj.has("tipo") ? obj.get("tipo").getAsString() : "";
                    if ("ProdutoEletronico".equals(tipoConcreto))
                        return ctx.deserialize(json, ProdutoEletronico.class);
                    if ("ProdutoAlimenticio".equals(tipoConcreto))
                        return ctx.deserialize(json, ProdutoAlimenticio.class);
                    throw new JsonParseException("Tipo de produto desconhecido: " + tipoConcreto);
                })
                .registerTypeAdapter(Funcionario.class, (JsonSerializer<Funcionario>) (src, t, ctx) -> {
                    JsonObject obj = ctx.serialize(src, src.getClass()).getAsJsonObject();
                    obj.addProperty("tipo", src.getClass().getSimpleName());
                    return obj;
                })
                .registerTypeAdapter(Funcionario.class, (JsonDeserializer<Funcionario>) (json, t, ctx) -> {
                    JsonObject obj = json.getAsJsonObject();
                    String tipoConcreto = obj.has("tipo") ? obj.get("tipo").getAsString() : "";
                    if ("Vendedor".equals(tipoConcreto))
                        return ctx.deserialize(json, Vendedor.class);
                    if ("Gerente".equals(tipoConcreto))
                        return ctx.deserialize(json, Gerente.class);
                    throw new JsonParseException("Tipo de funcionário desconhecido: " + tipoConcreto);
                })
                .create();
    }

    public void adicionar(T entidade) {
        lista.add(entidade);
        salvar();
    }

    public T buscarPorId(int id) throws EntidadeNaoEncontradaException {
        for (T entidade : lista) {
            if (idExtractor.apply(entidade) == id) {
                return entidade;
            }
        }
        throw new EntidadeNaoEncontradaException("Entidade com id " + id + " não encontrada.");
    }

    public List<T> listarTodos() {
        return Collections.unmodifiableList(lista);
    }

    public void atualizar(T entidade) throws EntidadeNaoEncontradaException {
        int id = idExtractor.apply(entidade);
        for (int i = 0; i < lista.size(); i++) {
            if (idExtractor.apply(lista.get(i)) == id) {
                lista.set(i, entidade);
                salvar();
                return;
            }
        }
        throw new EntidadeNaoEncontradaException("Não foi possível encontrar a entidade de id " + id
                + " para atualizar.");
    }

    public void remover(int id) throws EntidadeNaoEncontradaException {
        boolean removido = lista.removeIf(e -> idExtractor.apply(e) == id);
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Não foi possível encontrar a entidade de id " + id
                    + " para remover.");
        }
        salvar();
    }

    public void salvar() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Type listType = TypeToken.getParameterized(List.class, tipo).getType();
        try (Writer writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(lista, listType, writer);
        } catch (IOException e) {
            System.err.println("Houve um erro ao salvar o arquivo " + caminhoArquivo + ": " + e.getMessage());
        }
    }

    public void carregar() {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return;
        }
        try (Reader reader = new FileReader(arquivo)) {
            Type listType = TypeToken.getParameterized(List.class, tipo).getType();
            List<T> carregados = gson.fromJson(reader, listType);
            if (carregados != null) {
                lista.addAll(carregados);
            }
        } catch (Exception e) {
            System.err.println("Houve um erro ao carregar o arquivo " + caminhoArquivo + ": " + e.getMessage());
            // Preserva o arquivo problemático em .bak para não perdê-lo num save posterior.
            arquivo.renameTo(new File(caminhoArquivo + ".bak"));
        }
    }
}