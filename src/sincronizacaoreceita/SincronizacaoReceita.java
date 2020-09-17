/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizacaoreceita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 *
 * @author vinicius
 */
public class SincronizacaoReceita {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ReceitaService receitaService = new ReceitaService();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        // Recebe o caminho da home do usuário
        final String home = System.getProperty("user.home");
        final String separadorLinha = System.getProperty("line.separator");
        
        try {
            // TODO usuário escolher o diretório do arquivo importado
            File arquivoLido = new File(home, "dados-input.csv");
            File arquivoGerado = new File(home, "dados-result.csv");

            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoLido))) ;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoGerado)));
            String linha = null;
            int i = 0;
            String novaColuna = null;
            for ( linha = bufferedReader.readLine(); linha != null; linha = bufferedReader.readLine(),i++) {               
                if (i == 0) { //Ignorar cabeçalho
                    i++;
                    novaColuna = "Sincronizado na Receita";
                } else {
                    String[] campos = linha.replaceAll(",",".").split(";");
                    
                    // Preenche com 0 no início do valor caso a Agência não tenha 4 dígitos
                    String agencia = String.format("%04d", Integer.parseInt(campos[0]));
                    // Preenche com 0 no início do valor caso a Conta não tenha 6 dígitos
                    String conta = String.format("%6s", campos[1].replace("-", "")).replace(' ', '0');

                    // Troca o formato de 100.00 para 100.00 para gerar o obj tipo double
                    double saldo = Double.parseDouble(campos[2].replaceAll(",","."));
                    String status = campos[3];
                    boolean atualizou = receitaService.atualizarConta(agencia, conta, saldo, status);
                    
                    if (atualizou) {
                        novaColuna = "Sucesso";
                    } else {
                        novaColuna = "Erro ao sincronizar";
                    }
                    
                }
                bufferedWriter.write(linha + ";" + novaColuna + separadorLinha);
              
            }
                bufferedWriter.close();
            } catch(Exception e){
                System.out.println(e);
            }
    }
    
}
