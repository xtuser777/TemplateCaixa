package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucas
 */
public class ReceberControl extends ContaTemplate {
    
    protected Recebimento recebimento;

    public ReceberControl(Caixa caixa) {
        super(caixa);
    }

    @Override
    public boolean atualizarConta(int conta) {
        recebimento = new Recebimento().ObterPorId(connection, conta);
        if (recebimento == null) {
            return false;
        } else {
            return recebimento.receber(connection) > 0;
        }
    }
    
    public Recebimento obter(int id) {
        connection = Banco.getInstance().getConnection();
        recebimento = new Recebimento().ObterPorId(connection, id);
        try {
            Banco.getInstance().getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(QuitacaoControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recebimento;
    }
    
}
