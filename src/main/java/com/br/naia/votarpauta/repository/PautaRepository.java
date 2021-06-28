package com.br.naia.votarpauta.repository;

import com.br.naia.votarpauta.entity.Pauta;
import com.br.naia.votarpauta.constants.PautaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    List<Pauta> findAllByFechamentoBeforeAndStatus(LocalDateTime now, PautaStatus pautaStatus);

}
