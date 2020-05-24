package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Acerto;
import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.MovimentoCaixa;
import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.SQLException;

/**
 *
 * @author Lucas
 */
public abstract class ContaTemplate {

    protected Caixa caixa;
    
    public ContaTemplate(Caixa caixa) {
        this.caixa = caixa;
    }

    public final String confirmar(int conta) throws SQLException {
        if (!Banco.getInstance().open()) return "Erro ao conectar-se ao banco de dados.";
        if (caixa.isStatus()) { 
            Banco.getInstance().beginTransaction();
            if (atualizarConta(conta)) {
                if (criarMovimento(conta)) {
                    if (atualizarCaixa(conta)) {
                        Banco.getInstance().commit();
                        Banco.getInstance().close();
                        return "";
                    } else {
                        Banco.getInstance().rollback();
                        Banco.getInstance().close();
                        return "Ocorreu um problema ao atualizar o caixa.";
                    }
                } else {
                    Banco.getInstance().rollback();
                    Banco.getInstance().close();
                    return "Ocorreu um problema ao movimentar o caixa.";
                }
            } else {
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Ocorreu um problema ao atualizar a conta.";
            }
        } else {
            Banco.getInstance().rollback();
            Banco.getInstance().close();
            return "Caixa fechado!";
        }
    }

    public abstract boolean atualizarConta(int conta);

    public boolean criarMovimento(int conta) {
        if (this instanceof QuitacaoControl) {
            Pagamento p = new Pagamento().ObterPorId(conta);
            Acerto a = new Acerto(0, p.getDataQuitacao(), p.getValor(), 1, p.getConta());
            int ra = a.salvar();
            if (ra == -10 || ra == -1) {
                return false;
            }
            MovimentoCaixa mc = new MovimentoCaixa(0, p.getValor(), 1, caixa, a);
            int rmc = mc.salvar();
            if (rmc == -10 || rmc == -1) {
                return false;
            }

            return true;
        } else {
            Recebimento r = new Recebimento().ObterPorId(conta);
            Acerto a = new Acerto(0, r.getDataRecebimento(), r.getValor(), 1, r.getConta());
            int ra = a.salvar();
            if (ra == -10 || ra == -1) {
                return false;
            }
            MovimentoCaixa mc = new MovimentoCaixa(0, r.getValor(), 1, caixa, a);
            int rmc = mc.salvar();
            if (rmc == -10 || rmc == -1) {
                return false;
            }

            return true;
        }
    }

    public boolean atualizarCaixa(int conta) {
        if (this instanceof QuitacaoControl) {
            Pagamento p = new Pagamento().ObterPorId(conta);
            int res = caixa.decrementar(p.getValor());
            if (res == -10 || res == -4 || res == -5) {
                return false;
            }
        } else {
            Recebimento r = new Recebimento().ObterPorId(conta);
            int res = caixa.incrementar(r.getValor());
            if (res == -10 || res == -4 || res == -5) {
                return false;
            }
        }
        
        return true;
    }

}
