package com.example.mes.producao.api.controller;



import com.example.mes.producao.application.service.ProgramacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/programacao")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProgramacaoController {


        private final ProgramacaoService programacaoService;

}
