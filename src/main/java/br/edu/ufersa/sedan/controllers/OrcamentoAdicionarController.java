package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.DAO.OrcamentoDAO;
import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.OrcamentoService;
import br.edu.ufersa.sedan.model.services.PecaService;
import br.edu.ufersa.sedan.model.services.ServicoService;
import br.edu.ufersa.sedan.model.services.VeiculoService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoAdicionarController {

    @FXML private TextField txtBuscaVeiculo;
    @FXML private ListView<Veiculo> listaSugestoes;
    @FXML private ListView<Peca> lvPecas;
    @FXML private ListView<Servico> lvServicos;
    @FXML private Label lblTotal;
    @FXML private Label lblErro;

    private OrcamentoService orcamentoService;
    private VeiculoService veiculoService;
    private PecaService pecaService;
    private ServicoService servicoService;
    private Runnable aoSalvar;

    private Veiculo veiculoSelecionado = null;
    private final OrcamentoDAO dao = new OrcamentoDAO();

    public void setDependencias(OrcamentoService os, VeiculoService vs, PecaService ps, ServicoService ss, Runnable callback) {
        this.orcamentoService = os;
        this.veiculoService = vs;
        this.pecaService = ps;
        this.servicoService = ss;
        this.aoSalvar = callback;
        carregarListasMultiplaSelecao();
    }

    @FXML
    public void initialize() {
        listaSugestoes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Veiculo v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) { setText(null); }
                else { setText(v.getPlaca() + " — " + v.getMarca() + " (" + (v.getDono() != null ? v.getDono().getNome() : "Sem dono") + ")"); }
            }
        });

        listaSugestoes.setOnMouseClicked(e -> {
            Veiculo v = listaSugestoes.getSelectionModel().getSelectedItem();
            if (v != null) {
                veiculoSelecionado = v;
                txtBuscaVeiculo.setText(v.getPlaca());
                esconderSugestoes();
            }
        });

        // Habilita seleção múltipla nativa do JavaFX para Peças e Serviços
        lvPecas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvServicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Atualiza dinamicamente o contador total em tempo real
        lvPecas.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> atualizarTotalEstimado());
        lvServicos.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> atualizarTotalEstimado());
    }

    private void carregarListasMultiplaSelecao() {
        if (pecaService != null) lvPecas.setItems(FXCollections.observableArrayList(pecaService.listarTodos()));
        if (servicoService != null) lvServicos.setItems(FXCollections.observableArrayList(servicoService.listarTodos()));
    }

    @FXML
    private void onBuscarVeiculo() {
        String termo = txtBuscaVeiculo.getText().trim().toLowerCase();
        veiculoSelecionado = null;
        if (termo.isEmpty()) { esconderSugestoes(); return; }

        List<Veiculo> todos = veiculoService.listarVeiculos();
        if (todos == null) return;

        List<Veiculo> filtrados = new ArrayList<>();
        for (Veiculo v : todos) {
            if (v.getPlaca() != null && v.getPlaca().toLowerCase().contains(termo)) {
                filtrados.add(v);
            }
        }

        if (filtrados.isEmpty()) { esconderSugestoes(); }
        else {
            listaSugestoes.setItems(FXCollections.observableArrayList(filtrados));
            listaSugestoes.setVisible(true);
            listaSugestoes.setManaged(true);
        }
    }

    private void esconderSugestoes() {
        listaSugestoes.setVisible(false);
        listaSugestoes.setManaged(false);
    }

    private void atualizarTotalEstimado() {
        double total = 0;
        for (Peca p : lvPecas.getSelectionModel().getSelectedItems()) { if (p != null) total += p.getPreco(); }
        for (Servico s : lvServicos.getSelectionModel().getSelectedItems()) { if (s != null) total += s.getPreco(); }
        lblTotal.setText(String.format("R$ %.2f", total));
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        if (veiculoSelecionado == null) {
            lblErro.setText("É obrigatório selecionar um veículo cadastrado.");
            return;
        }

        Orcamento novo = new Orcamento();
        novo.setVeiculo(veiculoSelecionado);
        novo.setPlacaVeiculo(veiculoSelecionado.getPlaca());
        novo.setData(LocalDate.now());


        List<Peca> pecasSelecionadas = new ArrayList<>();
        for (Peca p : lvPecas.getSelectionModel().getSelectedItems()) {
            if (p != null && p.getId() > 0) { // Garante que a peça existe fisicamente no banco
                pecasSelecionadas.add(p);
            }
        }

        List<Servico> servicosSelecionados = new ArrayList<>();
        for (Servico s : lvServicos.getSelectionModel().getSelectedItems()) {
            if (s != null) {
                servicosSelecionados.add(s);
            }
        }

        novo.setListaPecas(pecasSelecionadas);
        novo.setServicos(servicosSelecionados);

        dao.inserir(novo);
        if (aoSalvar != null) aoSalvar.run();
        fechar();
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { ((Stage) txtBuscaVeiculo.getScene().getWindow()).close(); }
}