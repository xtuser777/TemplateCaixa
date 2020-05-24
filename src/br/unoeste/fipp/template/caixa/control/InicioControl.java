package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.util.Banco;

/**
 *
 * @author lucas
 */
public class InicioControl {
    
    public Caixa obterCaixa(int id) {
        if (!Banco.getInstance().open()) return null;
        Caixa c = Caixa.getById(id);
        Banco.getInstance().close();
        
        return c;
    }
    
    public String abreCaixa(Caixa c) {
        if (c == null || c.getId() == 0) return "Erro interno do sistema.";
        if (!Banco.getInstance().open()) return "Erro ao conectar-se ao banco de dados.";
        Banco.getInstance().beginTransaction();
        int res = c.abre();
        
        switch (res) {
            case -10: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro durante a aexecução do comando SQL, contate o suporte.";
            case -5: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro de parâmetros de sistema.";
            case -1: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro durante a aexecução do comando SQL, contate o suporte.";
            default: 
                Banco.getInstance().commit();
                Banco.getInstance().close();
                c.setStatus(true); 
                return "";
        }
    }
    
    public String fecharCaixa(Caixa c) {
        if (c == null || c.getId() == 0) return "Erro interno do sistema.";
        if (!Banco.getInstance().open()) return "Erro ao conectar-se ao banco de dados.";
        Banco.getInstance().beginTransaction();
        int res = c.fechar();
        
        switch (res) {
            case -10: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro durante a aexecução do comando SQL, contate o suporte.";
            case -5: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro de parâmetros de sistema.";
            case -1: 
                Banco.getInstance().rollback();
                Banco.getInstance().close();
                return "Erro durante a aexecução do comando SQL, contate o suporte.";
            default: 
                Banco.getInstance().commit();
                Banco.getInstance().close();
                c.setStatus(false); 
                return "";
        }
    }
}
