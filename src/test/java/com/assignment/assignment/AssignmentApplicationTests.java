package com.assignment.assignment;

import com.assignment.assignment.DAO.MetaDataDAO;
import com.assignment.assignment.Service.FileService;
import com.assignment.assignment.controller.MainController;
import com.assignment.assignment.entity.MetaData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest(classes =AssignmentApplication.class)
@WebAppConfiguration

public class AssignmentApplicationTests {

	@Test
	public void contextLoads() {
	}

	private MockMvc mockMvc;


	@MockBean
	private FileService fileService;

	@MockBean
	private MetaDataDAO metaDataDAO;

	@InjectMocks
	private MainController mainController;



	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
	}

	@Test
	public void testUploadMethod() throws Exception{

	}

	@Test
	public void testUploadFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());
		when(fileService.uploadFile(file)).thenReturn("Success");
//		assertEquals(mockMvc.perform(post("/metadatas",file)),"Succeed to Upload File");
		mockMvc.perform(post("/metadatas",file)).andExpect(content().string("Success"));
				//jsonPath("$").value("Succeed to Upload File"));

	}

	@Test
	public void testDownloadFile() throws Exception {
		when(fileService.downloadFile(1, new MockHttpServletResponse(), new MockHttpServletRequest())).thenReturn("Fail to DownloadFile");
	}

	@Test
	public void testGetMetaDataById() throws Exception {
		Date date = new Date();
		MetaData metaData = new MetaData(1, "Peter.txt", "/desktop", date, 1123);
		when(fileService.loadMetaDataDetails(1)).thenReturn(Optional.ofNullable(metaData));

		mockMvc.perform(get("/metadatas/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
		)


				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Peter.txt"))
				.andExpect(jsonPath("$.path").value("/desktop"))
				.andExpect(jsonPath("$.time").value(date))
				.andExpect( jsonPath("$.size").value(1123));


	}




	@Test
	public void testGetMetaDataList() throws Exception {
		Date date = new Date();
		List<MetaData> metaDataList = new ArrayList<>();
		metaDataList.add(new MetaData(1, "Peter.txt","/desktop" , date, 223));
		metaDataList.add(new MetaData(2, "Peter.gif","/downloads", date, 332));

		when(fileService.loadAllMetaData()).thenReturn(metaDataList);

		mockMvc.perform(get("/metadatas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(hasSize(2)))
				.andExpect(jsonPath("$[0].id").value( 1))
				.andExpect(jsonPath("$[0].name").value("Peter.txt"))
				.andExpect(jsonPath("$[0].size").value(223))
				.andExpect(jsonPath("$[0].path").value("/desktop"))
				.andExpect(jsonPath("$[0].time").value(date))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("Peter.gif"))
				.andExpect(jsonPath("$[1].size").value(332))
				.andExpect(jsonPath("$[1].path").value("/downloads"))
				.andExpect(jsonPath("$[1].time").value(date));
		}
//	}

}
