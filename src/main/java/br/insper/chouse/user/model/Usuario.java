package br.insper.chouse.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String auth0Id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int pontuacaoTotal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Usuario() {
    }

    public Usuario(String auth0Id, String nome, String email) {
        this.auth0Id = auth0Id;
        this.nome = nome;
        this.email = email;
    }

    @PrePersist
    void definirDataDeCriacao() {
        if (criadoEm == null) {
            criadoEm = LocalDateTime.now();
        }
    }

    public void adicionarPontos(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Pontos não podem ser negativos");
        }
        pontuacaoTotal += pontos;
    }

    public void atualizarPerfil(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }
}
