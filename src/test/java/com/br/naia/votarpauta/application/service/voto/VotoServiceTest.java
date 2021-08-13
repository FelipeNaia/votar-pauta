//package com.br.naia.votarpauta.application.service.voto;
//
//import com.br.naia.votarpauta.application.exception.CpfInvalidoException;
//import com.br.naia.votarpauta.application.exception.CpfJaVotouException;
//import com.br.naia.votarpauta.application.exception.PautaNaoEstaAbertaParaVotoException;
//import com.br.naia.votarpauta.application.integration.userinfo.UserInfoIntegration;
//import com.br.naia.votarpauta.domain.Pauta;
//import com.br.naia.votarpauta.domain.PautaRepository;
//import com.br.naia.votarpauta.domain.PautaStatus;
//import com.br.naia.votarpauta.domain.Voto;
//import com.br.naia.votarpauta.domain.VotoValor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//
//public class VotoServiceTest {
//
//    @Mock
//    private VotoRepository votoRepository;
//
//    @Mock
//    private PautaRepository pautaRepository;
//
//    @Mock
//    private UserInfoIntegration userInfoIntegration;
//
//    @InjectMocks
//    private VotoService votoService;
//
//    @Test
//    void deveContarVotosSim() {
//        Long id = 1L;
//        votoService.contabilizarVotosSim(id);
//        verify(votoRepository, Mockito.times(1)).countAllByPauta_IdAndVotoValor(id, VotoValor.SIM);
//    }
//
//    @Test
//    void deveContarVotosNao() {
//        Long id = 1L;
//        votoService.contabilizarVotosNao(id);
//        verify(votoRepository, Mockito.times(1)).countAllByPauta_IdAndVotoValor(id, VotoValor.NAO);
//    }
//
//    @Test
//    void deveRegistrarVoto() {
//        VotarInputData votarInputData = mock(VotarInputData.class);
//        Long pautaId = 1L;
//        String CPF = "123";
//
//        when(votarInputData.getPautaId()).thenReturn(pautaId);
//        when(votarInputData.getCpf()).thenReturn(CPF);
//        when(votarInputData.getVotoValor()).thenReturn(VotoValor.SIM);
//
//        Pauta pauta = Pauta
//                .builder()
//                .status(PautaStatus.ABERTA)
//                .fechamento(LocalDateTime.now().plusHours(1))
//                .build();
//
//        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
//        when(votoRepository.existsByPauta_IdAndCpf(pautaId, CPF)).thenReturn(false);
//        when(userInfoIntegration.cpfEhValido(CPF)).thenReturn(true);
//
//        votoService.votar(votarInputData);
//
//        verify(votoRepository).save(Voto.builder().pauta(pauta).cpf(CPF).votoValor(VotoValor.SIM).build());
//    }
//
//    @Test
//    void deveFalharEmPautaFechada() {
//        VotarInputData votarInputData = mock(VotarInputData.class);
//        Long pautaId = 1L;
//
//        when(votarInputData.getPautaId()).thenReturn(pautaId);
//
//        Pauta pauta = Pauta
//                .builder()
//                .status(PautaStatus.FECHADA)
//                .fechamento(LocalDateTime.now().minusMinutes(1))
//                .build();
//
//        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
//
//        Assertions.assertThrows(PautaNaoEstaAbertaParaVotoException.class, () -> votoService.votar(votarInputData));
//    }
//
//    @Test
//    void deveFalharEmVotoRepetido() {
//        VotarInputData votarInputData = mock(VotarInputData.class);
//        Long pautaId = 1L;
//        String CPF = "123";
//
//        when(votarInputData.getPautaId()).thenReturn(pautaId);
//        when(votarInputData.getCpf()).thenReturn(CPF);
//
//        Pauta pauta = Pauta
//                .builder()
//                .status(PautaStatus.ABERTA)
//                .fechamento(LocalDateTime.now().plusHours(1))
//                .build();
//
//        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
//        when(votoRepository.existsByPauta_IdAndCpf(pautaId, CPF)).thenReturn(true);
//
//        Assertions.assertThrows(CpfJaVotouException.class, () -> votoService.votar(votarInputData));
//    }
//
//    @Test
//    void deveFalharEmCpfInvalido() {
//        VotarInputData votarInputData = mock(VotarInputData.class);
//        Long pautaId = 1L;
//        String CPF = "123";
//
//        when(votarInputData.getPautaId()).thenReturn(pautaId);
//        when(votarInputData.getCpf()).thenReturn(CPF);
//
//        Pauta pauta = Pauta
//                .builder()
//                .status(PautaStatus.ABERTA)
//                .fechamento(LocalDateTime.now().plusHours(1))
//                .build();
//
//        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
//        when(votoRepository.existsByPauta_IdAndCpf(pautaId, CPF)).thenReturn(false);
//        when(userInfoIntegration.cpfEhValido(CPF)).thenReturn(false);
//
//        Assertions.assertThrows(CpfInvalidoException.class, () -> votoService.votar(votarInputData));
//    }
//
//}