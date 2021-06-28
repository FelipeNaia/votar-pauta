package com.br.naia.votarpauta.repository;

import com.br.naia.votarpauta.constants.VotoValor;
import com.br.naia.votarpauta.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Long countAllByPauta_IdAndVotoValor(Long pautaID, VotoValor valor);
}
