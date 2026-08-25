package com.example.mes.programacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.jayway.jsonpath.JsonPath;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProgramacaoTestQualidade {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProgramacaoRepository programacaoRepository;

        @Autowired
        private OrdemProducaoRepository ordemProducaoRepository;

        @Autowired
        private LoteRepository loteRepository;

        @Test
        void colocarQualidade() throws Exception {

                Long programacaoId = criarProgramacaoHelper();

                mockMvc.perform(patch("/api/programacao/{id}/programar", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PROGRAMADA"));

                mockMvc.perform(patch("/api/programacao/{id}/executar", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("EM_EXECUCAO"));

                mockMvc.perform(patch("/api/programacao/{id}/desabastecer", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("DESABASTECIDO"));

                mockMvc.perform(patch("/api/programacao/{id}/colocar-qualidade", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("QUALIDADE"));

                mockMvc.perform(patch("/api/programacao/{id}/retirar-qualidade", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("CRIADA"));

                mockMvc.perform(patch("/api/programacao/{id}/programar", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PROGRAMADA"));

                mockMvc.perform(patch("/api/programacao/{id}/executar", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("EM_EXECUCAO"));

                mockMvc.perform(patch("/api/programacao/{id}/concluir", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDA"));
                Programacao programacao = programacaoRepository.findById(programacaoId).orElseThrow();

                assertThat(programacao.getLoteProduzido().getStatus() == StatusLote.FINALIZADO);
                assertThat(programacao.getLoteConsumido().getStatus() == StatusLote.CONSUMIDO);

        }

        private Long criarProgramacaoHelper() throws Exception {

                Long loteId = criarLoteHelper();

                Long equipamentoId = criarEquipamentoHelper();

                Long ordemId = criarOPHelper(equipamentoId);
                vincularLoteAOPHelper(ordemId, loteId);

                String payloadProg = String.format("""
                                {
                                    "loteId": %d,
                                    "equipamentoId": %d,
                                    "quantidadeConsumida": 2500000
                                }
                                """, loteId, equipamentoId);

                String jsonResponse = mockMvc.perform(post("/api/programacao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadProg)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews"))))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Number idNumber = JsonPath.read(jsonResponse, "$[0].id");
                return idNumber.longValue();
        }

        private Long criarLoteHelper() throws Exception {
                String payload = """
                                {
                                    "quantidade": 2500000,
                                    "dataHoraInicio": "2026-08-08T11:08:01",
                                    "descricao": "Descrição de teste da programação"
                                }
                                """;
                MvcResult res = mockMvc.perform(post("/api/lotes")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                                .andExpect(status().isOk()).andReturn();
                return JsonPath.parse(res.getResponse().getContentAsString()).read("$.id", Long.class);
        }

        private Long criarEquipamentoHelper() throws Exception {
                String payload = """
                                {
                                    "nome": "Torno CNC 02",
                                    "sigla": "TNC-01",
                                    "descricao": "Equipamento de usinagem e corte de precisão",
                                    "dataAtivacao": "2026-08-08T10:00:00",
                                    "capacidade": 500000
                                }
                                """;
                MvcResult res = mockMvc.perform(post("/api/equipamentos")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                                .andExpect(status().isOk()).andReturn();
                return JsonPath.parse(res.getResponse().getContentAsString()).read("$.id", Long.class);
        }

        private Long criarOPHelper(Long equipamentoId) throws Exception {
                String payload = String.format("""
                                {
                                    "equipamentoId": %d,
                                    "capacidadeMaxima": 2500000
                                }
                                """, equipamentoId);
                MvcResult res = mockMvc.perform(post("/api/ordem_producao")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                                .andExpect(status().isOk()).andReturn();
                return JsonPath.parse(res.getResponse().getContentAsString()).read("$.id", Long.class);
        }

        private void vincularLoteAOPHelper(Long ordemId, Long loteId) throws Exception {

                mockMvc.perform(patch("/api/ordem_producao/vincular/{idOP}/{idLote}", ordemId, loteId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

}
