import base64, http.cookiejar, importlib.util, json, os, re, socketserver, subprocess, tempfile, threading, time, urllib.parse, urllib.request
from pathlib import Path
from email import policy
from email.parser import BytesParser
root=Path(__file__).resolve().parents[4]
work=Path(tempfile.mkdtemp(prefix='lab04s03-'))
spec=importlib.util.spec_from_file_location('smtp_local',root/'scripts/smtp_local.py'); mod=importlib.util.module_from_spec(spec);spec.loader.exec_module(mod)
mod.PASTA=work/'emails';mod.PASTA.mkdir()
smtp=socketserver.ThreadingTCPServer(('127.0.0.1',11026),mod.SMTP)
threading.Thread(target=smtp.serve_forever,daemon=True).start()
env=os.environ.copy();env.update(DB_URL='jdbc:h2:file:'+str(work/'moeda'),SMTP_PORT='11026',SERVER_PORT='18083')
log=open(work/'app.log','w');app=subprocess.Popen(['/opt/homebrew/opt/openjdk@21/bin/java','-jar',str(root/'target/moeda-estudantil-0.0.1-SNAPSHOT.jar'),'--spring.profiles.active=demo'],cwd=root,env=env,stdout=log,stderr=subprocess.STDOUT)
base='http://127.0.0.1:18083'
class Client:
 def __init__(self):self.op=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))
 def get(self,p):return self.op.open(base+p,timeout=5).read().decode()
 def token(self,page):return re.search(r'name="_csrf"[^>]*value="([^"]+)"',page).group(1)
 def post(self,p,fields,page):
  fields=dict(fields,_csrf=self.token(page));return self.op.open(base+p,urllib.parse.urlencode(fields).encode(),timeout=5).read().decode()
 def login(self,email,password):return self.post('/entrar',dict(username=email,password=password),self.get('/entrar'))
try:
 for i in range(50):
  try:urllib.request.urlopen(base,timeout=1);break
  except Exception:time.sleep(.5)
 else:raise RuntimeError('Aplicação não iniciou')
 company=Client();student=Client();teacher=Client()
 assert 'Conta criada' in company.post('/empresas',dict(nome='Livraria Sprint 3',email='empresa@sprint3.local',senha='Empresa123!'),company.get('/empresas/novo'))
 page=student.get('/alunos/novo');inst=re.search(r'<option value="(\d+)"',page).group(1)
 assert 'Conta criada' in student.post('/alunos',dict(nome='Ana Sprint 3',email='aluno@sprint3.local',senha='Aluno123!',cpf='52998224725',rg='RG TESTE',endereco='Rua Ficticia 1',curso='Computacao',instituicaoId=inst),page)
 company.login('empresa@sprint3.local','Empresa123!');student.login('aluno@sprint3.local','Aluno123!');teacher.login('professor@demo.local','Professor123!')
 page=company.get('/vantagens/nova');boundary='----Sprint3Form';fields={'descricao':'Desconto em livros Sprint 3','custo':'40','_csrf':company.token(page)};parts=[]
 for key,val in fields.items():parts.append(f'--{boundary}\r\nContent-Disposition: form-data; name="{key}"\r\n\r\n{val}\r\n'.encode())
 png=base64.b64decode('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aD1sAAAAASUVORK5CYII=')
 parts.append(f'--{boundary}\r\nContent-Disposition: form-data; name="foto"; filename="foto.png"\r\nContent-Type: image/png\r\n\r\n'.encode()+png+b'\r\n');parts.append(f'--{boundary}--\r\n'.encode())
 req=urllib.request.Request(base+'/vantagens/nova',data=b''.join(parts),headers={'Content-Type':'multipart/form-data; boundary='+boundary});assert 'Vantagem salva' in company.op.open(req).read().decode()
 catalog=student.get('/vantagens');vid=re.search(r'/vantagens/(\d+)/resgatar',catalog).group(1)
 assert 'Saldo de moedas insuficiente' in student.post('/vantagens/'+vid+'/resgatar',{},catalog)
 page=teacher.get('/moedas/enviar');aid=re.search(r'<option value="(\d+)"',page).group(1)
 assert '−120' in teacher.post('/moedas/enviar',dict(alunoId=aid,quantidade='120',motivo='Colaboracao na Sprint 3'),page)
 cupom=student.post('/vantagens/'+vid+'/resgatar',{},student.get('/vantagens'))
 code=re.search(r'<code[^>]*>\s*([0-9a-f-]{36})',cupom).group(1)
 extrato=student.get('/extrato');assert '−40' in extrato and '+120' in extrato
 saldo=int(re.search(r'class="saldo-linha"[^>]*>\s*<strong[^>]*>\s*(\d+)',extrato).group(1));assert saldo==80
 (work/'cupons-aluno.html').write_text(cupom);(work/'extrato-aluno.html').write_text(extrato)
 page=company.get('/cupons');assert code in page
 assert 'Uso do cupom confirmado' in company.post('/cupons/'+code+'/utilizar',{},page)
 assert 'Este cupom já foi utilizado' in company.post('/cupons/'+code+'/utilizar',{},company.get('/cupons'))
 deadline=time.time()+35
 while time.time()<deadline:
  files=list(mod.PASTA.glob('*.eml'))
  if len(files)>=4:break
  time.sleep(.5)
 messages=[BytesParser(policy=policy.default).parsebytes(p.read_bytes()) for p in files]
 coupons=[m for m in messages if code in m.get_content()]
 assert len(messages)==4 and len(coupons)==2
 assert {str(m['To']) for m in coupons}=={'aluno@sprint3.local','empresa@sprint3.local'}
 assert any('Você trocou 40 moedas' in m.get_content() for m in coupons)
 assert any('Ana Sprint 3 resgatou a vantagem' in m.get_content() for m in coupons)
 result=dict(status='PASS',verificacao='HTTP real com sessão, H2 temporário e SMTP local',emails=len(messages),emailsCupom=len(coupons),saldoFinalAluno=saldo,cupom=code,confirmacaoUnica=True,saldoInsuficienteRejeitado=True,artifacts=str(work))
 (work/'resultado.json').write_text(json.dumps(result,ensure_ascii=False,indent=2));print(json.dumps(result,ensure_ascii=False))
finally:
 app.terminate();app.wait(timeout=10);smtp.shutdown();smtp.server_close();log.close()
