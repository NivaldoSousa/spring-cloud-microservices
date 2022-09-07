package br.com.erudio.proxy;

import br.com.erudio.response.Cambio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
 * Interface para acesso ao microserviço cambio-service
 * */
@FeignClient(name = "cambio-service", url = "localhost:8000") // configuramos o nome do serviço e sua url
public interface CambioProxy {

    @GetMapping(value = "/cambio-service/{amount}/{from}/{to}")
    public Cambio getCambio(@PathVariable("amount") Double amount, @PathVariable("from") String from,
                            @PathVariable("to") String to);
}