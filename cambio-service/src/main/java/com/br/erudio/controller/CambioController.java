package com.br.erudio.controller;

import com.br.erudio.model.Cambio;
import com.br.erudio.repository.CambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("cambio-service")
public class CambioController {

    @Autowired
    private Environment environment;

    @Autowired
    private CambioRepository cambioRepository;

    @GetMapping(value = "/{amount}/{from}/{to}")
    public Cambio getCambio(@PathVariable("amount") BigDecimal amount, @PathVariable("from") String from,
                            @PathVariable("to") String to) {

        var cambio = cambioRepository.findByFromAndTo(from, to);
        if(cambio == null) throw new RuntimeException("Currency Unsupported");

        var port = environment.getProperty("local.server.port"); // com esse metodo é possivel obter varias informações sobre a aplicação, local.server.port -> recupera a porta da aplicação

        BigDecimal conversionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount); // multiplicando o valor total pelo fator de conversão

        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING)); // arredondando para fica com duas casas decimais
        cambio.setEnvironment(port);
        return cambio;
    }
}
