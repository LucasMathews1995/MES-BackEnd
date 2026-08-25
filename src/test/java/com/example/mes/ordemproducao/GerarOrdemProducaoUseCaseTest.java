package com.example.mes.ordemproducao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.jayway.jsonpath.JsonPath;

import jakarta.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GerarOrdemProducaoUseCaseTest {
  @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdemProducaoRepository repository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar a ordem de produção com sucesso e salvar os lotes processados")
    void deveriaCriarOrdemProducaoComSucesso() throws Exception  {

        Long equipamentoId = criarEquipamentoHelper();

        String payloadOP = String.format("""
                {
                    "equipamentoId": %d,
                    "capacidadeMaxima": 5000000
                }
                """, equipamentoId);

         mockMvc.perform(post("/api/ordem_producao")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON).content(payloadOP))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.statusOP").value(StatusOP.INICIADA.toString()));

    }

   private Long criarEquipamentoHelper() throws Exception {
                String payload = """
                                {
                                    "nome": "Torno CNC 02",
                                    "sigla": "TNC-01",
                                    "descricao": "Equipamento de usinagem e corte de precisão",
                                    "dataAtivacao": "2026-08-08T10:00:00",
                                    "capacidade": 5000000
                                }
                                """;
                MvcResult res = mockMvc.perform(post("/api/equipamentos")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                                .andExpect(status().isOk()).andReturn();
                return JsonPath.parse(res.getResponse().getContentAsString()).read("$.id", Long.class);
        }
}
