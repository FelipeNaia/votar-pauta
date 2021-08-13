package com.br.naia.votarpauta.domain.pauta;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {

    List<Pauta> findAllByFechamentoBeforeAndStatus(LocalDateTime now, PautaStatus pautaStatus);

}
