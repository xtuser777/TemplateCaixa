package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Acerto;
import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.MovimentoCaixa;
import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Lucas
 */
public abstract class ContaTemplate {

    protected Caixa caixa;
    protected Connection connection;
    
    public ContaTemplate(Caixa caixa) {
        this.caixa = caixa;
    }

    public final String confirmar(int conta) throws SQLException {
        if (caixa.isStatus()) { 
            connection = Banco.getInstance().getConnection();
            connection.setAutoCommit(false);
            if (atualizarConta(conta)) {
                if (criarMovimento(conta)) {
                    if (atualizarCaixa(conta)) {
                        connection.commit();
                        connection.close();
                        return "";
                    } else {
                        connection.rollback();
                        connection.close();
                        return "Ocorreu um problema ao atualizar o caixa.";
                    }
                } else {
                    connection.rollback();
                    connection.close();
                    return "Ocorreu um problema ao movimentar o caixa.";
                }
            } else {
                connection.rollback();
                connection.close();
                return "Ocorreu um problema ao atualizar a conta.";
            }
        } else {
            connection.rollback();
            connection.close();
            return "Caixa fechado!";
        }
    }

    public abstract boolean atualizarConta(int conta);

    public boolean criarMovimento(int conta) {
        if (this instanceof QuitacaoControl) {
            //Connection conn = Banco.getInstance().getConnection();
            Pagamento p = new Pagamento().ObterPorId(connection, conta);
            try {
                //conn.setAutoCommit(false);
                Acerto a = new Acerto(0, p.getDataQuitacao(), p.getValor(), 1, p.getConta());
                int ra = a.salvar(connection);
                if (ra == -10 || ra == -1) {
                    //connection.rollback();
                    //connection.close();
                    return false;
                }
                MovimentoCaixa mc = new MovimentoCaixa(0, p.getValor(), 1, caixa, a);
                int rmc = mc.salvar(connection);
                if (rmc == -10 || rmc == -1) {
                    //connection.rollback();
                    //connection.close();
                    return false;
                }
                //connection.commit();
                //connection.close();

                return true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        } else {
            //Connection connection = Banco.getInstance().getConnection();
            Recebimento r = new Recebimento().ObterPorId(connection, conta);
            try {
                //connection.setAutoCommit(false);
                Acerto a = new Acerto(0, r.getDataRecebimento(), r.getValor(), 1, r.getConta());
                int ra = a.salvar(connection);
                if (ra == -10 || ra == -1) {
                    //connection.rollback();
                    //connection.close();
                    return false;
                }
                MovimentoCaixa mc = new MovimentoCaixa(0, r.getValor(), 1, caixa, a);
                int rmc = mc.salvar(connection);
                if (rmc == -10 || rmc == -1) {
                    //connection.rollback();
                    //connection.close();
                    return false;
                }
                //connection.commit();
                //connection.close();

                return true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
    }

    public boolean atualizarCaixa(int conta) {
        if (this instanceof QuitacaoControl) {
            //Connection connection = Banco.getInstance().getConnection();
            Pagamento p = new Pagamento().ObterPorId(connection, conta);
            int res = caixa.decrementar(connection, p.getValor());
            if (res == -10 || res == -4 || res == -5) {
                return false;
            }
        } else {
            //Connection connection = Banco.getInstance().getConnection();
            Recebimento r = new Recebimento().ObterPorId(connection, conta);
            int res = caixa.incrementar(connection, r.getValor());
            if (res == -10 || res == -4 || res == -5) {
                return false;
            }
        }
        
        return true;
    }

}
