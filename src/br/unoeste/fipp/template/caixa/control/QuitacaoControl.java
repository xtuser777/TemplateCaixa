package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.model.Pagamento;
import br.unoeste.fipp.template.caixa.util.Banco;

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
        pagamento = new Pagamento().ObterPorId(conta);
        if (pagamento == null) {
            return false;
        } else {
            return pagamento.quitar() > 0;
        }
    }
    
    public Pagamento obter(int id) {
        if (!Banco.getInstance().open()) return null;
        pagamento = new Pagamento().ObterPorId(id);
        Banco.getInstance().close();
        
        return pagamento;
    }
    
}
