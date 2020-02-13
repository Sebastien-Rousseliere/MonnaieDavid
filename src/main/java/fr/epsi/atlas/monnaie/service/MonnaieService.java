package fr.epsi.atlas.monnaie.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.repository.MonnaieRepository;

@Service
public class MonnaieService {

	@Autowired
	private MonnaieRepository monnaieRepository;
	
	public Monnaie getByCode(String codeMonnaie) throws MonnaieInexistanteException{
		Optional<Monnaie> optionalMonnaie =  monnaieRepository.findById(codeMonnaie);
		if(! optionalMonnaie.isPresent()) {
			throw new MonnaieInexistanteException();
		}
		return optionalMonnaie.get();
	}
	
	@Transactional
	public void deleteByCode(String codeMonnaie) {
		monnaieRepository.deleteById(codeMonnaie);
	}

	@Transactional
	public Monnaie modify(String codeMonnaie, BigDecimal tauxDeChange) throws MonnaieInexistanteException {
		Monnaie monnaie = this.getByCode(codeMonnaie);
		monnaie.setTauxDeChange(tauxDeChange);
		return monnaie;
	}
	@Transactional
	public Monnaie create(String codeMonnaie, BigDecimal tauxDeChange) {
		Monnaie monnaie = new Monnaie(codeMonnaie, tauxDeChange);
		return monnaieRepository.save(monnaie);
	}



}


