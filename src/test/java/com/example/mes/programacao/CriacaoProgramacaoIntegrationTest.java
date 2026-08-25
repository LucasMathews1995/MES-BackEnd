package com.example.mes.programacao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CriacaoProgramacaoIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProgramacaoRepository programacaoRepository;

        @Autowired
        private OrdemProducaoRepository ordemProducaoRepository;

        @Autowired
        private LoteRepository loteRepository;

        @Test
        @DisplayName("1. Deve vincular lote à Ordem de Produção e alterar status da OP para PROCESSANDO")
        void deveVincularLoteEAlterarStatusDaOP() throws Exception {
                
                Long loteId = criarLoteHelper();

               
                Long equipamentoId = criarEquipamentoHelper();

             
                Long ordemId = criarOPHelper(equipamentoId);


                mockMvc.perform(patch("/api/ordem_producao/vincular/{idOP}/{idLote}", ordemId, loteId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(ordemId))
                                .andExpect(jsonPath("$.statusOP").value(StatusOP.PROCESSANDO.toString()));

              
                Lote lote = loteRepository.findById(loteId).orElseThrow();
                assertThat(lote.getStatus()).isEqualTo(StatusLote.DESABASTECIDO);

                OrdemProducao op = ordemProducaoRepository.findById(ordemId).orElseThrow();
                assertThat(op.getStatus()).isEqualTo(StatusOP.PROCESSANDO);
        }

        @Test
        @DisplayName("2. Deve criar programação e executar a estratégia de reserva do lote")
        void deveCriarProgramacaoEExecutarEstrategia() throws Exception {
                
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

                mockMvc.perform(post("/api/programacao")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadProg))
                                .andExpect(status().isCreated());

                List<Programacao> programacoes = programacaoRepository.findAll();

                assertThat(programacoes).hasSize(5);
                assertThat(programacoes.get(0).getStatus()).isEqualTo(StatusProgramacao.CRIADA);

                Lote lote = loteRepository.findById(loteId).orElseThrow();
                assertThat(lote.getStatus()).isEqualTo(StatusLote.DESABASTECIDO); 
        }

        @Test
        @DisplayName("Deve programar e alterar o status da programação para PROGRAMADA via API")
        void deveProgramarEAlterarStatusParaProgramadaEDepoisQualidade() throws Exception {

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

                mockMvc.perform(post("/api/programacao")
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadProg))
                                .andExpect(status().isCreated())
                                .andReturn();

                List<Programacao> programacoes = programacaoRepository.findAll();
                assertThat(programacoes).isNotEmpty();

                Long programacaoId = programacoes.get(0).getId();

                mockMvc.perform(patch("/api/programacao/{id}/programar", programacaoId)
                                .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());


                                mockMvc.perform(patch("/api/programacao/{id}/colocar-qualidade", programacaoId)
            .with(jwt().jwt(builder -> builder.claim("preferred_username", "lucasmathews")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

                Programacao programacaoSalva = programacaoRepository.findById(programacaoId)
                                .orElseThrow(() -> new RuntimeException("Programação não encontrada"));

                assertThat(programacaoSalva.getStatus()).isEqualTo(StatusProgramacao.QUALIDADE);
                assertThat(programacaoSalva.getFila()).isNull();
                assertThat(programacaoSalva.getLoteConsumido().getStatus()).isEqualTo(StatusLote.QUALIDADE);
                assertThat(programacaoSalva.getLoteProduzido()).isNull();
                

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