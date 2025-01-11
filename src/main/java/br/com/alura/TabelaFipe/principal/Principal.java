package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConvertDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    Scanner leitura = new Scanner(System.in);
    private final String UrlApi = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvertDados converterDados = new ConvertDados();

    public void exibirMenu() {
        String menu = """
                *** OPÇÕES ***
                CARRO 
                MOTO
                CAMINHÃO
                
                DIGITE UMA DAS OPÇÕES PARA PROSSEGUIR COM A CONSULTA:
                """;

        System.out.println(menu);
        String opcao = leitura.nextLine();

        String endereco;

        if(opcao.toLowerCase().contains("carr")){
            endereco = UrlApi + "carros/marcas";
        } else if(opcao.toLowerCase().contains("mot")){
            endereco = UrlApi + "motos/marcas";
        }else{
            endereco = UrlApi + "caminhoes/marcas";
        }

        String jsonApi = consumoApi.obterDados(endereco);
        System.out.println(jsonApi);
        var marcas = converterDados.obterLista(jsonApi, Dados.class);
        marcas.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);


        System.out.println("Informe o código da marca: ");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        jsonApi = consumoApi.obterDados(endereco);
        var modeloLista = converterDados.obterDados(jsonApi, Modelos.class);

        System.out.println("Modelos dessa marca: ");
        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("Digite um trecho do nome do carro a ser buscado: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream().filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase())).collect(Collectors.toList());

        System.out.println("Modelos Filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo para os valores de avaliação: ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco +"/"+ codigoModelo + "/anos";
        jsonApi = consumoApi.obterDados(endereco);

        List<Dados> anos = converterDados.obterLista(jsonApi, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++){
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            jsonApi = consumoApi.obterDados(enderecoAnos);

            Veiculo veiculo = converterDados.obterDados(jsonApi, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("Veiculos Encontrados: ");
        veiculos.forEach(System.out::println);





    }
}
