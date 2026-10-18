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
            assertThrows(SQLException.class,()->s.executeUpdate("INSERT INTO aluno(id,cpf,rg,endereco,curso,saldo,instituicao_id) VALUES (999,'52998224725','RG','Rua','Curso',-1,1)"));
        }
        assertEquals(0,Flyway.configure().dataSource(url,"sa","").load().migrate().migrationsExecuted);
        try (var c=DriverManager.getConnection(url,"sa","");var s=c.createStatement();var rs=s.executeQuery("SELECT nome FROM instituicao_ensino")) {
            assertTrue(rs.next());assertEquals("Persistente",rs.getString(1));
        }
    }
}
