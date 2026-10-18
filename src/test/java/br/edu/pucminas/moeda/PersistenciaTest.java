package br.edu.pucminas.moeda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.flywaydb.core.Flyway;
import java.nio.file.Path;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
class PersistenciaTest {
    @TempDir Path pasta;
    @Test void migracaoEReaberturaPreservamDadosEConstraints() throws Exception {
        String url="jdbc:h2:file:"+pasta.resolve("moeda");
        Flyway.configure().dataSource(url,"sa","").load().migrate();
        try (var c=DriverManager.getConnection(url,"sa","");var s=c.createStatement()) {
            s.executeUpdate("INSERT INTO instituicao_ensino(nome) VALUES ('Persistente')");
            s.executeUpdate("INSERT INTO usuario(id,nome,email,senha_hash,ativo) VALUES (100,'Teste','teste@local','hash',true)");
            SQLException saldo=assertThrows(SQLException.class,()->s.executeUpdate("INSERT INTO aluno(id,cpf,rg,endereco,curso,saldo,instituicao_id) VALUES (100,'52998224725','RG','Rua','Curso',-1,1)"));
            assertEquals("23513",saldo.getSQLState());
            SQLException fk=assertThrows(SQLException.class,()->s.executeUpdate("INSERT INTO aluno(id,cpf,rg,endereco,curso,saldo,instituicao_id) VALUES (100,'52998224725','RG','Rua','Curso',0,999)"));
            assertEquals("23506",fk.getSQLState());
            SQLException duplicado=assertThrows(SQLException.class,()->s.executeUpdate("INSERT INTO usuario(nome,email,senha_hash,ativo) VALUES ('Outro','teste@local','hash',true)"));
            assertEquals("23505",duplicado.getSQLState());
        }
        assertEquals(0,Flyway.configure().dataSource(url,"sa","").load().migrate().migrationsExecuted);
        try (var c=DriverManager.getConnection(url,"sa","");var s=c.createStatement();var rs=s.executeQuery("SELECT nome FROM instituicao_ensino")) {
            assertTrue(rs.next());assertEquals("Persistente",rs.getString(1));
        }
    }
}
