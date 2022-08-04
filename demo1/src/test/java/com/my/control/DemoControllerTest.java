package com.my.control;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//@WebMvcTest(DemoController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DemoControllerTest {
	@Autowired
	private MockMvc mockMvc; // 모의 객체 : "흉내"내는 "가짜" 모듈
	
	@Test
	void testUseRepository() throws Exception {
		MockHttpServletRequestBuilder  mockRequestBuilder = MockMvcRequestBuilders.get("/userepository").accept(org.springframework.http.MediaType.APPLICATION_JSON);
		int expectedCount = 9;
		// 모의객체를 이용해서 요청/응답한다. 응답결과 : ResultActions
		ResultActions resultActions = mockMvc.perform(mockRequestBuilder);
		//응답결과가 200 임을 예상함
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());
		
		//응답결과는 {"totalCnt" : 7} 일껄 예상함
		// jsonPath를 잉요해서 totalCnt 프로퍼티값이 7과 같은가 예상한다.
		org.hamcrest.Matcher<Integer> matcher;
		ResultMatcher resultMatcher;
		
		matcher = org.hamcrest.CoreMatchers.is(expectedCount);
		resultMatcher = jsonPath("totalCnt", matcher);
		resultActions.andExpect(resultMatcher);
	}
}
