package gaian.svsa.ct.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import gaian.svsa.ct.modelo.enums.AgenteViolador;
import gaian.svsa.ct.modelo.enums.CodigoEncaminhamento;
import gaian.svsa.ct.modelo.enums.DireitoViolado;
import gaian.svsa.ct.modelo.enums.OrigemDenuncia;
import gaian.svsa.ct.modelo.enums.StatusRD;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author laurojr
 *
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name="Denuncia.buscarTodos", query="select d from Denuncia d where d.tenant_id = :tenantId"),
	@NamedQuery(name="Denuncia.buscarTodosDia", query="select d from Denuncia d where d.tenant_id = :tenantId "
			+ "and d.unidade = :unidade "
			+ "and d.dataEmissao between :ini and :fim "),	
})
public class Denuncia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@ToString.Include
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private Long tenant_id;
	
	private Integer ano;
	
	private String relato;
	
	@ToString.Include
	@Column(length = 512000,columnDefinition="Text")
	private String assunto;
	
	@ToString.Include
	private String nomeOrgao;
	
	@ToString.Include
	private Long numero;
	
	@ToString.Include
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@Enumerated(EnumType.STRING)
	private AgenteViolador agenteViolador;
	
	@Enumerated(EnumType.STRING)
	private StatusRD statusRD;
	
	@ElementCollection(targetClass = DireitoViolado.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "DireitosViolados", joinColumns = @JoinColumn(name = "codigo_denuncia"))
	@Column(name = "direito", nullable = false)
	@Enumerated(EnumType.STRING)
	private List<DireitoViolado> direitosViolados;

	@Enumerated(EnumType.STRING)
	private OrigemDenuncia origemDenuncia;
	
	@ManyToOne
	@JoinColumn(name="codigo_tecnico")
	private Usuario tecnico;
	
	@ManyToOne
	@JoinColumn(name="codigo_pessoa")
	private PessoaDenuncia pessoa;
	
	@ManyToOne
	@JoinColumn(name="codigo_unidade")
	private Unidade unidade;

	@ToString.Include
	private String endereco;
	
	private Boolean excluido = false;

	@Enumerated(EnumType.STRING)
	private CodigoEncaminhamento codigoEncaminhamento;
	
	@Transient
	public String getNrDenuncia() {
		return numero + "/" + ano;	
	}
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;
	
	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}		
	}

	@ManyToOne
	@JoinColumn(name="prontuario")
	private Prontuario prontuario;
	
}
