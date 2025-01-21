package com.manager.config;


import com.manager.client.author.AuthorsRestClientImpl;
import com.manager.client.book.BooksRestClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public BooksRestClientImpl booksRestClient(
            @Value("${Bookstore.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new BooksRestClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }

    @Bean
    public AuthorsRestClientImpl authorsRestClient(
            @Value("${Bookstore.services.catalogue.uri:http://localhost8081}") String catalogueBaseUri){
        return new AuthorsRestClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }

}
