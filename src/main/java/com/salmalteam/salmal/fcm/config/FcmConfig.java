package com.salmalteam.salmal.fcm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FcmConfig {

	private final String path;

	public FcmConfig(@Value("${google.fcm.path}") String path) {
		this.path = path;
	}

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(new ClassPathResource(path).getInputStream()))
			.build();
		return FirebaseApp.initializeApp(firebaseOptions);
	}
}