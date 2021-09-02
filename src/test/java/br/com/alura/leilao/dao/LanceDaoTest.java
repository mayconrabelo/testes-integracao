package br.com.alura.leilao.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.LanceBuilder;
import br.com.alura.leilao.util.builder.LeilaoBuilder;
import br.com.alura.leilao.util.builder.UsuarioBuilder;

class LanceDaoTest {

	private LanceDao lanceDao;
	private LeilaoDao dao;
	private EntityManager em;
	
	@BeforeEach
	public void beforeEach() {
		this.em = JPAUtil.getEntityManager();
		this.dao = new LeilaoDao(em);
		this.lanceDao = new LanceDao(em);
		em.getTransaction().begin();
	}
	
	@AfterEach
	public void afterEach() {
		em.getTransaction().rollback();
	}
	
	@Test
	public void deveriaSalvarLance() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		Leilao leilao = new LeilaoBuilder()
				.comNome("mochila")
				.comValorInicial("100")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.criar();
		em.persist(leilao);
		Lance lance = new LanceBuilder()
				.comUsuario(usuario)
				.comValor("300")
				.comData(LocalDate.now())
				.comLeilao(leilao)
				.criar();
		em.persist(lance);
		
		leilao.propoe(lance);
		
		Leilao salvo = dao.buscarPorId(leilao.getId());
		
		Lance lance2 = salvo.getLances().get(0);
		
		assertEquals(new BigDecimal("300"), lance2.getValor());
		
	}
	
	@Test
	public void deveriaRetornarOMaiorLance() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		Leilao leilao = new LeilaoBuilder()
				.comNome("mochila")
				.comValorInicial("100")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.criar();
		em.persist(leilao);
		Lance lance = new LanceBuilder()
				.comUsuario(usuario)
				.comValor("300")
				.comData(LocalDate.now())
				.comLeilao(leilao)
				.criar();
		em.persist(lance);
		
		Lance maiorLanceDoLeilao = lanceDao.buscarMaiorLanceDoLeilao(leilao);
		
		assertEquals(new BigDecimal("300"), maiorLanceDoLeilao.getValor());
		
	}
}
