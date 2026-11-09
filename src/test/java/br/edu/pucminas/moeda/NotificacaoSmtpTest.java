package br.edu.pucminas.moeda;
import br.edu.pucminas.moeda.servico.NotificacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
class NotificacaoSmtpTest extends BaseIntegracao {
    @org.springframework.beans.factory.annotation.Autowired br.edu.pucminas.moeda.servico.VantagemService beneficios;
    @Autowired NotificacaoService entrega;
    @Autowired JavaMailSenderImpl sender;
    @Test void smtpRealEntregaReconhecimentoECuponsEAposFalhaTentaNovamente() throws Exception {
        int portaOriginal=sender.getPort();String hostOriginal=sender.getHost();
        saldoProfessor(1000);moedas.enviar(professor.getEmail(),ana.getId(),100,"Participação");
        var troca=beneficios.resgatar(ana.getEmail(),oferta(20).getId());
        int indisponivel;try(ServerSocket socket=new ServerSocket(0)){indisponivel=socket.getLocalPort();}
        sender.setHost("127.0.0.1");sender.setPort(indisponivel);entrega.entregarPendentes();
        assertTrue(notificacoes.findAll().stream().allMatch(n->n.getEnviadaEm()==null && n.getTentativas()==1));
        try (ServerSocket smtp=new ServerSocket(0,10,InetAddress.getLoopbackAddress())) {
            sender.setHost("127.0.0.1");sender.setPort(smtp.getLocalPort());
            ExecutorService pool=Executors.newSingleThreadExecutor();
            Future<List<String>> recebidos=pool.submit(()->{
                List<String> mensagens=new ArrayList<>();
                while (mensagens.size()<4) {
                    try (Socket socket=smtp.accept();var leitor=new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));var saida=new PrintWriter(socket.getOutputStream(),true,StandardCharsets.UTF_8)) {
                        saida.print("220 localhost SMTP teste\r\n");saida.flush();boolean dados=false;StringBuilder mensagem=new StringBuilder();String linha;
                        while ((linha=leitor.readLine())!=null) {
                            if (dados) {
                                if (linha.equals(".")) { mensagens.add(mensagem.toString());dados=false;saida.print("250 recebida\r\n"); }
                                else mensagem.append(linha).append('\n');
                            } else if (linha.startsWith("EHLO") || linha.startsWith("HELO")) saida.print("250 localhost\r\n");
                            else if (linha.equals("DATA")) { dados=true;saida.print("354 envie os dados\r\n"); }
                            else if (linha.equals("QUIT")) { saida.print("221 ate logo\r\n");saida.flush();break; }
                            else saida.print("250 ok\r\n");
                            saida.flush();
                        }
                    }
                }
                return mensagens;
            });
            try {
                entrega.entregarPendentes();var mensagens=recebidos.get(10,TimeUnit.SECONDS);
                assertEquals(4,mensagens.size());assertEquals(2,mensagens.stream().filter(m->m.contains(troca.getCodigo())).count());
                assertTrue(mensagens.stream().anyMatch(m->m.contains("To: "+ana.getEmail())));
                assertTrue(mensagens.stream().anyMatch(m->m.contains("To: "+parceiro.getEmail())));
                var decodificadas=new ArrayList<jakarta.mail.internet.MimeMessage>();
                for (String mensagem:mensagens) decodificadas.add(new jakarta.mail.internet.MimeMessage(
                    jakarta.mail.Session.getInstance(new Properties()),new ByteArrayInputStream(mensagem.getBytes(StandardCharsets.UTF_8))));
                var confirmacao=decodificadas.stream().filter(m->{try{return m.getSubject().equals("Confirmação de envio de moedas");}catch(Exception e){throw new RuntimeException(e);}}).findFirst().orElseThrow();
                var recebimento=decodificadas.stream().filter(m->{try{return m.getSubject().equals("Você recebeu moedas estudantis");}catch(Exception e){throw new RuntimeException(e);}}).findFirst().orElseThrow();
                assertEquals(professor.getEmail(),((jakarta.mail.internet.InternetAddress)confirmacao.getAllRecipients()[0]).getAddress());
                assertEquals(ana.getEmail(),((jakarta.mail.internet.InternetAddress)recebimento.getAllRecipients()[0]).getAddress());
                assertTrue(confirmacao.getContent().toString().contains("Você enviou 100 moedas para Ana"));
                assertTrue(recebimento.getContent().toString().contains("Você recebeu 100 moedas de Professor"));
                assertTrue(confirmacao.getContent().toString().contains("Participação"));
                assertTrue(recebimento.getContent().toString().contains("Participação"));
                assertTrue(notificacoes.findAll().stream().allMatch(n->n.getEnviadaEm()!=null && n.getTentativas()==2));
                entrega.entregarPendentes();assertEquals(4,notificacoes.count());
            } finally { pool.shutdownNow(); }
        } finally { sender.setPort(portaOriginal);sender.setHost(hostOriginal); }
    }
    @Test void smtpIndisponivelMantemMensagemPendenteSemRepetirDebito() throws Exception {
        int portaOriginal=sender.getPort();String hostOriginal=sender.getHost();
        int fechada;try(ServerSocket socket=new ServerSocket(0)){fechada=socket.getLocalPort();}
        saldoProfessor(100);moedas.enviar(professor.getEmail(),ana.getId(),30,"Mérito");
        try {
            sender.setHost("127.0.0.1");sender.setPort(fechada);entrega.entregarPendentes();
            assertEquals(2,notificacoes.count());
            assertTrue(notificacoes.findAll().stream().allMatch(n->n.getEnviadaEm()==null && n.getTentativas()==1));
            assertEquals(70,professores.findById(professor.getId()).orElseThrow().getSaldo());
            assertEquals(30,alunos.findById(ana.getId()).orElseThrow().getSaldo());assertEquals(1,envios.count());
        } finally { sender.setPort(portaOriginal);sender.setHost(hostOriginal); }
    }
}
