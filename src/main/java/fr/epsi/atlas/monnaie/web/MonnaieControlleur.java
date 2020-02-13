package fr.epsi.atlas.monnaie.web;


import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.service.MonnaieInexistanteException;
import fr.epsi.atlas.monnaie.service.MonnaieService;



@RestController
@RequestMapping(path = "/monnaie/{codeMonnaie}")
public class MonnaieControlleur {
	@Autowired
	private MonnaieService monnaieService;
	
	@ExceptionHandler(MonnaieInexistanteException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String handleException(MonnaieInexistanteException e) {
		return "la monnaie n'existe pas";
	}
	
	@GetMapping
	public Monnaie getByCode(@PathVariable String codeMonnaie) throws MonnaieInexistanteException {
		return monnaieService.getByCode(codeMonnaie);
	}
	
	@DeleteMapping
	public void deleteMonnaie(@PathVariable String codeMonnaie) {
		monnaieService.deleteByCode(codeMonnaie);
	}
	
	@PutMapping
	public ResponseEntity<Monnaie> modifyByCode(@PathVariable String codeMonnaie, @RequestBody TauxDeChangeDto tauxDeChangeDto, 
			UriComponentsBuilder uriBuilder) {
		try {
			Monnaie monnaie = monnaieService.modify(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
			return ResponseEntity.ok().body(monnaie);
		
		}catch (MonnaieInexistanteException e){
				Monnaie monnaie = monnaieService.create(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
				URI uri = uriBuilder.path("/monnaie/{codeMonnaie}").buildAndExpand(codeMonnaie).toUri();
				return ResponseEntity.created(null).body(monnaie);
		}
	}
	
	@PostMapping
	public BigDecimal converted(BigDecimal monnaieToConvert, @PathVariable String codeMonnaie, @RequestBody TauxDeChangeDto tauxDeChangeDto, 
			UriComponentsBuilder uriBuilder) {
		BigDecimal monnaieConverted = monnaieToConvert.multiply(tauxDeChangeDto.getTauxDeChange());
		URI uri = uriBuilder.path("/monnaie/{codeMonnaie}/converted").buildAndExpand(codeMonnaie).toUri();
		return monnaieConverted;
		
	}
	
}
