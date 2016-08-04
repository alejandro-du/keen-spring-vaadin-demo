package com.example;

import com.vaadin.wscdn.WidgetSet;
import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;
import io.keen.client.java.KeenQueryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${keen.projectId}")
    private String projectId;

    @Value("${keen.writeKey}")
    private String writeKey;

    @Value("${keen.readKey}")
    private String readKey;

    @Bean
    public KeenProject keenProject() {
        return new KeenProject(projectId, writeKey, readKey);
    }

    @Bean
    public KeenClient keenClient(KeenProject keenProject) {
        KeenClient keenClient = new JavaKeenClientBuilder().build();
        keenClient.setDefaultProject(keenProject());
        return keenClient;
    }

    @Bean
    public KeenQueryClient keenQueryClient(KeenProject keenProject) {
        return new KeenQueryClient.Builder(keenProject).build();
    }

    @Bean
    public WidgetSet vaadinCdnInitializer() {
        return new WidgetSet();
    }

}
