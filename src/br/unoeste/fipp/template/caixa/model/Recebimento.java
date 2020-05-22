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
public class Recebimento {

    private int id;
    private String conta;
    private LocalDate dataConta;
    private LocalDate dataRecebimento;
    private LocalDate dataVencimento;
    private Double valor;

    public Recebimento(int id, String conta, LocalDate dataConta, LocalDate dataRecebimento, LocalDate dataVencimento, Double valor) {
        this.id = id;
        this.conta = conta;
        this.dataConta = dataConta;
        this.dataRecebimento = dataRecebimento;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
    }
    
    public Recebimento() {
        
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

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
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
        if (this.dataRecebimento != null) {
            status = "🗹";
        } else {
            status = "⮽";
        }
        return status+": "+this.id+" | Conta: "+this.conta+" | Valor: "+this.valor+" | Venc: "+this.dataVencimento.toString();
    }

    public int gravar(Connection conn) {
        int result = -10;
        String sql = ""
                + "insert "
                + "into recebimento (rec_conta, rec_valor, rec_data_conta, rec_data_recebimento, rec_data_vencimento) "
                + "values(?,?,?,?,?) "
                + "returning rec_id;";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, conta);
                ps.setDouble(2, valor);
                ps.setDate(3, Date.valueOf(dataConta));
                ps.setDate(4, null);
                ps.setDate(5, Date.valueOf(dataVencimento));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        result = rs.getInt("rec_id");
                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return result;
    }
    
    public int receber(Connection conn) {
        int result = -10;
        String sql = ""
                + "update recebimento "
                + "set rec_data_recebimento = ? "
                + "where rec_id = ?;";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public int atualizar(Connection conn) {
        int result = -10;
        String sql = ""
                + "update recebimento "
                + "set rec_conta = ?, rec_valor = ?, rec_data_conta = ?, rec_data_recebimento = ?, rec_data_vencimento = ? "
                + "where rec_id = ?;";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public Recebimento ObterPorId(Connection conn, int id) {

        Recebimento r = null;
        String sql = "select * from recebimento where rec_id = ?;";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        r = new Recebimento(
                            rs.getInt("rec_id"),
                            rs.getString("rec_conta"),
                            rs.getDate("rec_data_conta").toLocalDate(),
                            (rs.getDate("rec_data_recebimento") != null) ? rs.getDate("rec_data_recebimento").toLocalDate() : null,
                            rs.getDate("rec_data_vencimento").toLocalDate(),
                            rs.getDouble("rec_valor")
                        );
                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return r;
    }

    public ArrayList<Recebimento> obterTodos(Connection conn) {

        ArrayList<Recebimento> recebimentos = null;
        String sql = "select * from recebimento;";
        Banco db = Banco.getInstance();

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    recebimentos = new ArrayList<>();
                    while (rs.next()) {
                        recebimentos.add(
                            new Recebimento(
                                    rs.getInt("rec_id"),
                                    rs.getString("rec_conta"),
                                    rs.getDate("rec_data_conta").toLocalDate(),
                                    (rs.getDate("rec_data_recebimento") != null) ? rs.getDate("rec_data_recebimento").toLocalDate() : null,
                                    rs.getDate("rec_data_vencimento").toLocalDate(),
                                    rs.getDouble("rec_valor")
                            )
                        );
                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return recebimentos;
    }

}
