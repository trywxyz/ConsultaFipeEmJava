package br.com.alura.TabelaFipe.service;

import java.util.List;

public interface IConvertDados {
    <T> T obterDados(String json, Class<T> classe);


    <T> List<T> obterLista(String json, Class<T> classe);
}
