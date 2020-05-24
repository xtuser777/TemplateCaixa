package br.unoeste.fipp.template.caixa.model;

import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author kenta
 */
public class Pagamento {

    private int id;
    private String conta;
    private LocalDate dataConta;
    private LocalDate dataQuitacao;
    private LocalDate dataVencimento;
    private Double valor;

    public Pagamento(int id, String conta, LocalDate dataConta, LocalDate dataQuitacao, LocalDate dataVencimento, Double valor) {
        this.id = id;
        this.conta = conta;
        this.dataConta = dataConta;
        this.dataQuitacao = dataQuitacao;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
    }

    public Pagamento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public LocalDate getDataConta() {
        return dataConta;
    }

    public void setDataConta(LocalDate dataConta) {
        this.dataConta = dataConta;
    }

    public LocalDate getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(LocalDate dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }
    
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
    
    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        String status = "";
        if (this.dataQuitacao != null) {
            status = "🗹";
        } else {
            status = "⮽";
        }
        return status+": "+this.id+" | Conta: "+this.conta+" | Valor: "+this.valor+" | Venc: "+this.dataVencimento.toString();
    }

    public int gravar() {
        int result = -10;
        String sql = ""
                + "insert "
                + "into pagamento (pag_conta, pag_valor, pag_data_conta, pag_data_quitacao, pag_data_vencimento) "
                + "values(?,?,?,?,?) returning pag_id;";

        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setString(1, conta);
                ps.setDouble(2, valor);
                ps.setDate(3, Date.valueOf(dataConta));
                ps.setDate(4, null);
                ps.setDate(5, Date.valueOf(dataVencimento));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        result = rs.getInt("pag_id");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return result;
    }
    
    public int quitar() {
        int result = -10;
        String sql = ""
                + "update pagamento "
                + "set pag_data_quitacao = ? "
                + "where pag_id = ?;";

        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, id);

                result = ps.executeUpdate();

                ps.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return result;
    }

    public int atualizar() {
        int result = -10;
        String sql = ""
                + "update pagamento "
                + "set pag_conta = ?, pag_valor = ?, pag_data_conta = ?, pag_data_quitacao = ?, pag_data_vencimento = ? "
                + "where pag_id = ?;";

        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setString(1, conta);
                ps.setDouble(2, valor);
                ps.setDate(3, Date.valueOf(dataConta));
                ps.setDate(4, null);
                ps.setDate(5, Date.valueOf(dataVencimento));
                ps.setInt(6, id);

                result = ps.executeUpdate();

                ps.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return result;
    }

    public Pagamento ObterPorId(int id) {

        Pagamento r = null;
        String sql = "select * from pagamento where pag_id = ?;";

        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        r = new Pagamento(
                            rs.getInt("pag_id"),
                            rs.getString("pag_conta"),
                            rs.getDate("pag_data_conta").toLocalDate(),
                            (rs.getDate("pag_data_quitacao") != null) ? rs.getDate("pag_data_quitacao").toLocalDate() : null,
                            rs.getDate("pag_data_vencimento").toLocalDate(),
                            rs.getDouble("pag_valor")
                        );
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return r;
    }

    public ArrayList<Pagamento> obterTodos() {

        ArrayList<Pagamento> pagamentos = null;
        String sql = "select * from pagamento;";

        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    pagamentos = new ArrayList<>();
                    while (rs.next()) {
                        pagamentos.add(new Pagamento(
                                    rs.getInt("pag_id"),
                                    rs.getString("pag_conta"),
                                    rs.getDate("pag_data_conta").toLocalDate(),
                                    (rs.getDate("pag_data_quitacao") != null) ? rs.getDate("pag_data_quitacao").toLocalDate() : null,
                                    rs.getDate("pag_data_vencimento").toLocalDate(),
                                    rs.getDouble("pag_valor")
                            )
                        );
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return pagamentos;
    }

}
