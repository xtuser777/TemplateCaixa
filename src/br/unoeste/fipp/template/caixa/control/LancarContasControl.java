package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class LancarContasControl {
    
    public String obterLancamentos() {
        String lancamentos = "";
        List<Pagamento> pag = obterPagamentos();
        List<Recebimento> rec = obterRecebimentos();
        lancamentos += "Pagamentos:\n\n";
        for(Pagamento p : pag) {
            lancamentos += p.toString()+"\n";
        }
        lancamentos += "\nRecebimentos:\n\n";
        for(Recebimento r : rec) {
            lancamentos += r.toString()+"\n";
        }
        
        return lancamentos;
    }
    
    private List<Pagamento> obterPagamentos() {
        List<Pagamento> pagamentos = new ArrayList<>();
        if (!Banco.getInstance().open()) return pagamentos;
        pagamentos = new Pagamento().obterTodos();
        Banco.getInstance().close();
        
        return pagamentos;
    }
    
    private List<Recebimento> obterRecebimentos() {
        List<Recebimento> recebimentos = new ArrayList<>();
        if (!Banco.getInstance().open()) return recebimentos;
        recebimentos = new Recebimento().obterTodos();
        Banco.getInstance().close();
        
        return recebimentos;
    }
    
    public String confirmar(int tipo, double valor, String conta, LocalDate venc) {
        if (!Banco.getInstance().open()) return "Erro ao conectar-se ao banco de dados.";
        
        Banco.getInstance().beginTransaction();
        
        if(tipo <= 0 || tipo > 2) return "Tipo de conta inválido.";
        if(valor <= 0) return "Valor da conta inválido.";
        if(conta == null || conta.isEmpty()) return "O nome da conta é inválido.";
        if(venc == null) return "A data de vencimento é inválida";
        if(tipo == 1) {
            Pagamento p = new Pagamento(0, conta, LocalDate.now(), null, venc, valor);
            int res_pag = p.gravar();
            if (res_pag == -10 || res_pag == -1) {
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Ocorreu um problema ao executar o lançamento.";
            } 
            /*if (res_pag == -5) {
                conn.rollback();
                conn.close();
                return "Parâmetros incorretos";
            }*/
        } else {
            Recebimento r = new Recebimento(0,conta,LocalDate.now(), null, venc, valor);
            int res_rec = r.gravar();
            if (res_rec == -10 || res_rec == -1) {
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Ocorreu um problema ao executar o lançamento.";
            }
            /*if (res_rec == -5) {

            }*/
        }
        Banco.getInstance().commit();
        Banco.getInstance().close();

        return "";
    }
}
