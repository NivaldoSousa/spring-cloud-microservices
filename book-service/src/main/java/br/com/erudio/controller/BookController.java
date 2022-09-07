package br.com.erudio.controller;

import br.com.erudio.model.Book;
import br.com.erudio.proxy.CambioProxy;
import br.com.erudio.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CambioProxy cambioProxy;

    @GetMapping(value = "/{id}/{currency}")
    public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {

        var book = bookRepository.getReferenceById(id); // o metodo getById()
        if (book == null) throw new RuntimeException("Book not Found");

        var cambio = cambioProxy.getCambio(book.getPrice(), "USD", currency); // Consumindo o microserviço do cambio-service
        var port = environment.getProperty("local.server.port"); // com esse metodo é possivel obter varias informações sobre a aplicação, local.server.port -> recupera a porta da aplicação

        book.setEnvironment(port);
        book.setPrice(cambio.getConvertedValue());
        return book;
    }


  /*
   * Exemplo de referencia sem a utilização do Feign
   *
   * @GetMapping(value = "/{id}/{currency}")
    public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {

        var book = bookRepository.getReferenceById(id); // o metodo getById()
        if (book == null) throw new RuntimeException("Book not Found");

        HashMap<String, String> params = new HashMap<>(); // setando os valores que farao bind com a url de chamada do microserviço
        params.put("amount", book.getPrice().toString());
        params.put("from", "USD");
        params.put("to", currency);

        var response = new RestTemplate().getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", Cambio.class, params); // Faz a chamada para o microserviço cambio-service retornando o objeto Cambio
        var cambio = response.getBody(); // extrair o objeto cambio do body
        var port = environment.getProperty("local.server.port"); // com esse metodo é possivel obter varias informações sobre a aplicação, local.server.port -> recupera a porta da aplicação

        book.setEnvironment(port);
        book.setPrice(cambio.getConvertedValue());
        return book;
    }*/
}
