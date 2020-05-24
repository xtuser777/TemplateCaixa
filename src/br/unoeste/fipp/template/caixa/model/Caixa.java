package br.unoeste.fipp.template.caixa.model;

import br.unoeste.fipp.template.caixa.util.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Caixa
{
    private int id;
    private boolean status;
    private double saldoInicial;
    private double saldoFinal;

    public Caixa(int id, boolean status, double saldoInicial, double saldoFinal) 
    {
        this.id = id;
        this.status = status;
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
    }

    public int getId()
    {
        return id;
    }

    public boolean isStatus()
    {
        return status;
    }
    
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public double getSaldoInicial()
    {
        return saldoInicial;
    }

    public double getSaldoFinal() 
    {
        return saldoFinal;
    }
    
    public int abre()
    {
        if (this.id <= 0) return -5;
        this.status = true;
        String sql = "update caixa set cxa_status = true where cxa_id = ?;";
        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setInt(1, id);

                return ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -10;
            }
        }        
        
        return -10;
    }
    
    public int fechar() {
        if (this.id <= 0) return -5;
        this.status = false;
        String sql = "update caixa set cxa_status = false where cxa_id = ?;";
        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setInt(1, id);

                return ps.executeUpdate();
            } catch (SQLException ex)  {
                System.out.println(ex.getMessage());
                return -10;
            }
        }        
        
        return -10;
    }
    
    public int decrementar(Double valor) {
        if (valor == null || valor < 0 || valor > this.saldoFinal || !status) return -5;
        
        if(valor.intValue() != valor && valor.toString().replace(".","#").split("#")[1].length() > 2) return -4;
        
        this.saldoInicial = this.saldoFinal;
        this.saldoFinal = this.saldoFinal - valor;
        
        String sql = "update caixa set cxa_saldo_inicial = ?, cxa_saldo_final = ? where cxa_id = ?;";
        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setDouble(1, saldoInicial);
                ps.setDouble(2, saldoFinal);
                ps.setInt(3, id);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -10;
            }
        }
        
        return -10;
    }
    
    public int incrementar(Double valor)
    {
        if (valor == null || valor < 0 || !status) return -5;
        
        if(valor.intValue() != valor && valor.toString().replace(".","#").split("#")[1].length() > 2) return -4;
        
        this.saldoInicial = this.saldoFinal;
        this.saldoFinal = this.saldoFinal + valor;
        
        String sql = "update caixa set cxa_saldo_inicial = ?, cxa_saldo_final = ? where cxa_id = ?;";
        if (Banco.getInstance().getConnection() != null) {
            try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
                ps.setDouble(1, saldoInicial);
                ps.setDouble(2, saldoFinal);
                ps.setInt(3, id);
                return ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -10;
            }
        }
        
        return -10;
    }
    
    public static Caixa getById(int id)
    {
        if (Banco.getInstance().getConnection() == null || id <= 0) return null;
        String sql = "select * from caixa where cxa_id = ?;";
        try (PreparedStatement ps = Banco.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Caixa(
                        rs.getInt("cxa_id"),
                        rs.getBoolean("cxa_status"),
                        rs.getDouble("cxa_saldo_inicial"),
                        rs.getDouble("cxa_saldo_final")                                
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException ex)  {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
