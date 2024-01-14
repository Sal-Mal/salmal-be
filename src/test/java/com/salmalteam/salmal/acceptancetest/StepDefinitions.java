package com.salmalteam.salmal.acceptancetest;

import static io.restassured.http.ContentType.*;
import static io.restassured.path.json.JsonPath.from;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDefinitions {

	@Autowired
	private ScenarioContext scenarioContext;
	@LocalServerPort
	private int port;

	private String token;

	private RequestSpecification requestSpecification;
	private Response response;

	@Given("파이어베이스 인증후 요청 시 {string} 가 주어진다.")
	public void given(String providerId) {
		scenarioContext.put("providerId", providerId);
		HashMap<String, String> map = new HashMap<>();
		map.put("providerId", providerId);
		requestSpecification = RestAssured.given().request();
		requestSpecification.baseUri("http://localhost:" + port);
		requestSpecification.log().all();
		requestSpecification.contentType(JSON);
		requestSpecification.body(map);
	}

	@When("\\/api\\/auth\\/login 을 통해 요청 한다.")
	public void when() {
		response = requestSpecification.post("/api/auth/login");
	}

	@Then("providerId 가 유효한 id 인지 검증하고 생성된 인증토큰과 재발급 토큰을 받는다.")
	public void then() {
		String json = response.asString();
		String accessToken = from(json).getString("accessToken");
		assertThat(accessToken).isNotBlank();
	}
}