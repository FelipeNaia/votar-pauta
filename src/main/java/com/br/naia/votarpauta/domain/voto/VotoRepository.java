package com.br.naia.votarpauta.domain.voto;

import com.br.naia.votarpauta.domain.voto.VotoValor;
import com.br.naia.votarpauta.domain.voto.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Long countAllByPauta_IdAndVotoValor(Long pautaID, VotoValor valor);
    Boolean existsByPauta_IdAndCpf(Long pautaID, String cpf);
}
