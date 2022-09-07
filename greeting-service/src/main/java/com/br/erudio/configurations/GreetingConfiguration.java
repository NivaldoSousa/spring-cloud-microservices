package com.br.erudio.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/*
 * Classe de configuração para obter os valores de configuração do application.yml
 * 
 * */
@Component
@RefreshScope // é usada para carregar o valor das propriedades de configuração do servidor Config
@ConfigurationProperties("greeting-service") // faz o bind entre o application.yml com a propriedade configurada "greeting-service"
public class GreetingConfiguration {
	
	private String greeting;
	private String defaultValue;
	
	public GreetingConfiguration() {

	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
		
}
