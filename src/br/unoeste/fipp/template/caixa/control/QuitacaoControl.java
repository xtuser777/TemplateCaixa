package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas
 */
public class QuitacaoControl extends ContaTemplate {
    
    private Pagamento pagamento;

    public QuitacaoControl(Caixa caixa) {
        super(caixa);
    }

    @Override
    public boolean atualizarConta(int conta) {
        pagamento = new Pagamento().ObterPorId(connection, conta);
        if (pagamento == null) {
            return false;
        } else {
            return pagamento.quitar(connection) > 0;
        }
    }
    
    public Pagamento obter(int id) {
        connection = Banco.getInstance().getConnection();
        pagamento = new Pagamento().ObterPorId(connection, id);
        try {
            Banco.getInstance().getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(QuitacaoControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pagamento;
    }
    
}
