package com.br.naia.votarpauta.application.service.pauta;

import com.br.naia.votarpauta.application.exception.DadosDeCadastroInvalidosException;
import com.br.naia.votarpauta.application.exception.PautaNaoEhNovaException;
import com.br.naia.votarpauta.application.kafka.sender.PublicarResultadoDaPautaTopicSender;
import com.br.naia.votarpauta.application.service.voto.VotoService;
import com.br.naia.votarpauta.domain.pauta.Pauta;
import com.br.naia.votarpauta.domain.pauta.PautaRepository;
import com.br.naia.votarpauta.domain.pauta.PautaStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.br.naia.votarpauta.domain.pauta.PautaStatus.ABERTA;
import static com.br.naia.votarpauta.domain.pauta.PautaStatus.FECHADA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaDtoTranslator pautaDtoTranslator;

    @Mock
    private PublicarResultadoDaPautaTopicSender encerrarPautaTopic;

    @Mock
    private VotoService votoService;

    @Mock
    private Validator validator;

    @Mock
    private Set<ConstraintViolation<CadastrarPautaInputData>> constraintViolationSet;

    @InjectMocks
    private PautaService pautaService;

    @Captor
    private ArgumentCaptor<Pauta> pautaArgumentCaptor;

    @Test
    void deveCadastrar() {
        String NOME_DA_PAUTA = "Nome da pauta";
        CadastrarPautaInputData cadastrarPautaInputData = mock(CadastrarPautaInputData.class);
        when(cadastrarPautaInputData.getNome()).thenReturn(NOME_DA_PAUTA);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(new Pauta());
        when(constraintViolationSet.isEmpty()).thenReturn(true);
        when(validator.validate(cadastrarPautaInputData)).thenReturn(constraintViolationSet);

        pautaService.cadastrar(cadastrarPautaInputData);

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        assertEquals(NOME_DA_PAUTA, pautaSalva.getNome());
    }

    @Test
    void deveLancarErroAoCadastrarSemNome() {
        CadastrarPautaInputData cadastrarPautaInputData = mock(CadastrarPautaInputData.class);
        when(constraintViolationSet.isEmpty()).thenReturn(false);
        when(validator.validate(cadastrarPautaInputData)).thenReturn(constraintViolationSet);
        Assertions.assertThrows(DadosDeCadastroInvalidosException.class, () -> pautaService.cadastrar(cadastrarPautaInputData));
    }

    @Test
    void deveAbrirPauta() {
        AbrirSessaoInputData abrirSessaoInputData = mock(AbrirSessaoInputData.class);
        when(abrirSessaoInputData.getTempoEmMinutos()).thenReturn(null);
        when(abrirSessaoInputData.getPautaId()).thenReturn(1L);
        when(pautaRepository.save(any(Pauta.class))).thenReturn(new Pauta());
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(
                Pauta.builder()
                        .status(PautaStatus.NOVA)
                        .build()));

        pautaService.abrirSessao(abrirSessaoInputData);

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        assertEquals(ABERTA, pautaSalva.getStatus());
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

        Assertions.assertThrows(PautaNaoEhNovaException.class, () -> pautaService.abrirSessao(abrirSessaoInputData));
    }

    @Test
    void deveFecharPauta() {
        when(pautaRepository.findAllByFechamentoBeforeAndStatus(any(LocalDateTime.class), eq(PautaStatus.ABERTA))).thenReturn(List.of(
                Pauta.builder().status(PautaStatus.NOVA).build()));

        pautaService.fecharPautas();

        verify(pautaRepository).save(pautaArgumentCaptor.capture());

        Pauta pautaSalva = pautaArgumentCaptor.getValue();

        assertEquals(FECHADA, pautaSalva.getStatus());
        verify(encerrarPautaTopic).send(anyString());
    }
}