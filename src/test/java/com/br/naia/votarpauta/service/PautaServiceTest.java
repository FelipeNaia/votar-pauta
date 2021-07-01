package com.br.naia.votarpauta.service;

import com.br.naia.votarpauta.constants.PautaStatus;
import com.br.naia.votarpauta.controller.inputdata.AbrirSessaoInputData;
import com.br.naia.votarpauta.controller.inputdata.CadastrarPautaInputData;
import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.repository.PautaRepository;
import com.br.naia.votarpauta.topic.PublicarResultadoDaPautaTopic;
import com.br.naia.votarpauta.translator.PautaTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.br.naia.votarpauta.constants.PautaStatus.ABERTA;
import static com.br.naia.votarpauta.constants.PautaStatus.FECHADA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaTranslator pautaTranslator;

    @Mock
    private PublicarResultadoDaPautaTopic encerrarPautaTopic;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private PautaService pautaService;

    @Captor
    private ArgumentCaptor<Pauta> pautaArgumentCaptor;

    @Test
    void deveCadastrar() {
        String NOME_DA_PAUTA = "Nome da pauta";
        CadastrarPautaInputData cadastrarPautaInputData = mock(CadastrarPautaInputData.class);
        when(cadastrarPautaInputData.getNome()).thenReturn(NOME_DA_PAUTA);

        pautaService.cadastrar(cadastrarPautaInputData);

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        Assertions.assertEquals(NOME_DA_PAUTA, pautaSalva.getNome());
    }

    @Test
    void deveLancarErroAoCadastrarSemNome() {
        CadastrarPautaInputData cadastrarPautaInputData = mock(CadastrarPautaInputData.class);
        Assertions.assertThrows(IllegalArgumentException.class, () -> pautaService.cadastrar(cadastrarPautaInputData));
    }

    @Test
    void deveAbrirPauta() {
        AbrirSessaoInputData abrirSessaoInputData = mock(AbrirSessaoInputData.class);
        when(abrirSessaoInputData.getTempoEmMinutos()).thenReturn(null);
        when(abrirSessaoInputData.getPautaId()).thenReturn(1L);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(
                Pauta.builder()
                .status(PautaStatus.NOVA)
                .build()));

        pautaService.abrirSessao(abrirSessaoInputData);

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        Assertions.assertEquals(ABERTA, pautaSalva.getStatus());
    }

    @Test
    void deveLancarErroAoAbrirPauta() {
        AbrirSessaoInputData abrirSessaoInputData = mock(AbrirSessaoInputData.class);
        when(abrirSessaoInputData.getTempoEmMinutos()).thenReturn(null);
        when(abrirSessaoInputData.getPautaId()).thenReturn(1L);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(
                Pauta.builder()
                .status(PautaStatus.ABERTA)
                .build()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> pautaService.abrirSessao(abrirSessaoInputData));
    }

    @Test
    void deveFecharPauta() {
        when(pautaRepository.findAllByFechamentoBeforeAndStatus(any(LocalDateTime.class), eq(PautaStatus.ABERTA))).thenReturn(List.of(
                Pauta.builder().status(PautaStatus.NOVA).build()));

        pautaService.fecharPautas();

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        Assertions.assertEquals(FECHADA, pautaSalva.getStatus());
        verify(encerrarPautaTopic).send(anyString());
    }
}