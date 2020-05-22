package br.unoeste.fipp.template.caixa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Acerto 
{
    private int id;
    private LocalDate data;
    private double valor;
    private int tipo;
    private String motivo;

    public Acerto(int id, LocalDate data, double valor, int tipo, String motivo) 
    {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
        this.motivo = motivo;
    }

    public int getId() 
    {
        return id;
    }

    public LocalDate getData() 
    {
        return data;
    }

    public double getValor()
    {
        return valor;
    }

    public int getTipo()
    {
        return tipo;
    }

    public String getMotivo() 
    {
        return motivo;
    }
    
    public int salvar(Connection conn)
    {
        if (this.id != 0 || this.data == null || this.tipo == 0 || this.valor <= 0 || this.motivo == null || this.motivo.isEmpty()) return -5;
        
        String sql = "insert into acerto(act_data,act_valor,act_tipo,act_motivo) values(?,?,?,?) returning act_id;";
        
        if (conn != null)
        {
            try (PreparedStatement ps = conn.prepareStatement(sql))
            {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(data, LocalTime.now())));
                ps.setDouble(2, valor);
                ps.setInt(3, tipo);
                ps.setString(4, motivo);

                try(ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        int result = rs.getInt("act_id");
                        this.id = result;
                        return result;
                    } else {
                        return -10;
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -10;
            }
        }
        
        return -10;
    }
}
