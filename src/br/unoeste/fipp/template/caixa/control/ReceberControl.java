package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.Recebimento;
import br.unoeste.fipp.template.caixa.util.Banco;

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
        recebimento = new Recebimento().ObterPorId(conta);
        if (recebimento == null) {
            return false;
        } else {
            return recebimento.receber() > 0;
        }
    }
    
    public Recebimento obter(int id) {
        if (!Banco.getInstance().open()) return null;
        recebimento = new Recebimento().ObterPorId(id);
        Banco.getInstance().close();
        
        return recebimento;
    }
    
}
