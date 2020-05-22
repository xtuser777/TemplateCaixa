package br.unoeste.fipp.template.caixa.control;

import br.unoeste.fipp.template.caixa.model.Caixa;
import br.unoeste.fipp.template.caixa.util.Banco;

/**
 *
 * @author lucas
 */
public class InicioControl 
{
    public Caixa obterCaixa(int id)
    {
        Caixa c = null;
        c = Caixa.getById(Banco.getInstance().getConnection(), id);
        
        return c;
    }
    
    public String abreCaixa(Caixa c)
    {
        if (c == null || c.getId() == 0) return "Erro interno do sistema.";
        
        int res = c.abre(Banco.getInstance().getConnection());
        
        switch (res)
        {
            case -10: return "Erro durante a aexecução do comando SQL, contate o suporte.";
            case -5: return "Erro de parâmetros de sistema.";
            case -1: return "Erro durante a aexecução do comando SQL, contate o suporte.";
            default: 
                c.setStatus(true); 
                return "";
        }
    }
    
    public String fecharCaixa(Caixa c)
    {
        if (c == null || c.getId() == 0) return "Erro interno do sistema.";
        
        int res = c.fechar(Banco.getInstance().getConnection());
        
        switch (res)
        {
            case -10: return "Erro durante a aexecução do comando SQL, contate o suporte.";
            case -5: return "Erro de parâmetros de sistema.";
            case -1: return "Erro durante a aexecução do comando SQL, contate o suporte.";
            default: 
                c.setStatus(false); 
                return "";
        }
    }
}
