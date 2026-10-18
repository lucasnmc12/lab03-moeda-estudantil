#!/usr/bin/env python3
"""Capturador SMTP local de demonstração. Não encaminha emails à internet."""
import html
import socketserver
import threading
from datetime import datetime
from email import policy
from email.parser import BytesParser
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from uuid import uuid4

PASTA = Path(__file__).resolve().parents[1] / 'dados' / 'emails'
PASTA.mkdir(parents=True, exist_ok=True)

class SMTP(socketserver.StreamRequestHandler):
    def responder(self, texto):
        self.wfile.write((texto + '\r\n').encode())
        self.wfile.flush()

    def handle(self):
        self.responder('220 localhost SMTP demonstracao')
        dados = False
        mensagem = []
        for raw in self.rfile:
            linha = raw.rstrip(b'\r\n')
            if dados:
                if linha == b'.':
                    nome = datetime.now().strftime('%Y%m%d-%H%M%S-') + str(uuid4()) + '.eml'
                    (PASTA / nome).write_bytes(b'\r\n'.join(mensagem))
                    self.responder('250 mensagem capturada localmente')
                    dados = False
                    mensagem = []
                else:
                    mensagem.append(linha[1:] if linha.startswith(b'..') else linha)
            elif linha.upper().startswith((b'EHLO', b'HELO')):
                self.responder('250 localhost')
            elif linha.upper() == b'DATA':
                dados = True
                self.responder('354 termine com ponto em linha isolada')
            elif linha.upper() == b'QUIT':
                self.responder('221 ate logo')
                break
            else:
                self.responder('250 ok')

class Caixa(BaseHTTPRequestHandler):
    def do_GET(self):
        partes = ['<!doctype html><html lang="pt-BR"><meta charset="utf-8"><title>Emails locais</title>',
                  '<style>body{font:16px system-ui;max-width:1000px;margin:40px auto;padding:20px}pre{white-space:pre-wrap;background:#eef2eb;padding:20px}</style>',
                  '<h1>Caixa de emails de demonstração</h1><p>Mensagens capturadas neste computador. Nenhum envio externo.</p>']
        arquivos = sorted(PASTA.glob('*.eml'), reverse=True)
        if not arquivos:
            partes.append('<p>Nenhuma mensagem capturada.</p>')
        for arquivo in arquivos:
            mensagem = BytesParser(policy=policy.default).parsebytes(arquivo.read_bytes())
            corpo_email = mensagem.get_body(preferencelist=('plain',)) or mensagem
            partes.append('<article><h2>' + html.escape(str(mensagem.get('Subject', 'Mensagem'))) + '</h2>')
            partes.append('<p>Para: ' + html.escape(str(mensagem.get('To', ''))) + '<br>Data: ' + html.escape(str(mensagem.get('Date', ''))) + '</p>')
            partes.append('<pre>' + html.escape(str(corpo_email.get_content())) + '</pre></article>')
        partes.append('</html>')
        corpo = ''.join(partes).encode()
        self.send_response(200)
        self.send_header('Content-Type', 'text/html; charset=utf-8')
        self.send_header('Content-Length', str(len(corpo)))
        self.end_headers()
        self.wfile.write(corpo)

if __name__ == '__main__':
    smtp = socketserver.ThreadingTCPServer(('127.0.0.1', 1025), SMTP)
    threading.Thread(target=smtp.serve_forever, daemon=True).start()
    print('SMTP: 127.0.0.1:1025. Caixa: http://127.0.0.1:8025. Ctrl+C encerra.')
    try:
        ThreadingHTTPServer(('127.0.0.1', 8025), Caixa).serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        smtp.shutdown()
        smtp.server_close()
