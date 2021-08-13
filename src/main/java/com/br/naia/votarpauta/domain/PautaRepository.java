package com.br.naia.votarpauta.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {

    Flux<Pauta> findAllByFechamentoBeforeAndStatus(LocalDateTime now, PautaStatus pautaStatus);

}
