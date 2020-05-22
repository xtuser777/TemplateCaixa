package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (Banco.getInstance().getConnection() == null) return pagamentos;
        pagamentos = new Pagamento().obterTodos(Banco.getInstance().getConnection());
        try {
            Banco.getInstance().getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(LancarContasControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pagamentos;
    }
    
    private List<Recebimento> obterRecebimentos() {
        List<Recebimento> recebimentos = new ArrayList<>();
        if (Banco.getInstance().getConnection() == null) 
            return recebimentos;
        recebimentos = new Recebimento().obterTodos(Banco.getInstance().getConnection());
        try {
            Banco.getInstance().getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(LancarContasControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recebimentos;
    }
    
    public String confirmar(int tipo, double valor, String conta, LocalDate venc) {
        Connection conn = Banco.getInstance().getConnection();
        if (conn == null) return "Erro ao conectar-se ao banco de dados.";
        
        try {
            conn.setAutoCommit(false);
            if(tipo <= 0 || tipo > 2) return "Tipo de conta inválido.";
            if(valor <= 0) return "Valor da conta inválido.";
            if(conta == null || conta.isEmpty()) return "O nome da conta é inválido.";
            if(venc == null) return "A data de vencimento é inválida";
            if(tipo == 1) {
                Pagamento p = new Pagamento(0, conta, LocalDate.now(), null, venc, valor);
                int res_pag = p.gravar(conn);
                if (res_pag == -10 || res_pag == -1) {
                    conn.rollback();
                    conn.close();
                    return "Ocorreu um problema ao executar o lançamento.";
                } 
                /*if (res_pag == -5) {
                    conn.rollback();
                    conn.close();
                    return "Parâmetros incorretos";
                }*/
            } else {
                Recebimento r = new Recebimento(0,conta,LocalDate.now(), null, venc, valor);
                int res_rec = r.gravar(conn);
                if (res_rec == -10 || res_rec == -1) {
                    conn.rollback();
                    conn.close();
                    return "Ocorreu um problema ao executar o lançamento.";
                }
                /*if (res_rec == -5) {
                    
                }*/
            }
            conn.commit();
            conn.close();
            
            return "";
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }
}
