package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.library.book.dto.BookRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ru.library.LibraryApplication.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @Test
    void createBook_returnsCreated() throws Exception {
        var req = new BookRequest("978-3-16-148410-0", "Clean Code", "Robert Martin", 2008);

        mvc.perform(post("/api/books")
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }
    @Test
    void createDuplicateIsbn_returns409() throws Exception {
        var req = new BookRequest("111", "A", "B", 2000);
        mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(req))).andExpect(status().isCreated());

        mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(req))).andExpect(status().isConflict());
    }
}