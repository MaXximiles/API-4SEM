package fatec.grupodois.endurance.service;


import com.itextpdf.html2pdf.HtmlConverter;
import fatec.grupodois.endurance.DBConexao;
import fatec.grupodois.endurance.exception.NenhumResultadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import static fatec.grupodois.endurance.constant.FileConstant.*;

@Service
public class RelatorioService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public RelatorioService() {}

    public ResponseEntity<byte[]> fetchRelatorioPeriodo(String dataInicio, String dataFim) throws NenhumResultadoException {

        Connection conn = null;
        ResultSet resultadoBanco = null;
        Statement stm;
        try {
            conn = DBConexao.abrirConexao();
            stm = conn.createStatement();

            String sql = " SELECT evt_id, evt_criacao, evt_desc, evt_fim, evt_inicio, evt_local, evt_max_part, evt_obs, evt_status, evt_tema, evt_total_part, evt_usr_id " +
                    " FROM eventos " +
                    " WHERE evt_inicio " +
                    " BETWEEN TO_DATE('"+dataInicio+"','YYYY-MM-DD') AND TO_DATE ('"+dataFim+"','YYYY-MM-DD') ORDER BY evt_inicio ";
            resultadoBanco = stm.executeQuery(sql);
        } catch(SQLException e) {
            LOGGER.error("Erro ao criar query");
            LOGGER.info(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro ao criar query");
            LOGGER.info(e.getMessage());
        }

        String htmlText = "<html> \n" +
                "<body> \n" +

                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " <tr bgcolor='#4169E1'><td colspan='7' align='center'> \n" +
                "<img src='./endurance_logoB.png' width='70' height='50'> \n" +
                "<font color='white' size='+2'><b>  RELATÓRIO DE EVENTOS POR PERÍODO </b></font></td></tr>\n " +
                " <tr bgcolor='#4169E1'> \n" +
                " <td align='center'><b><font color='white'> DATA </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA INICIO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA TÉRMINO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> EVENTO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> DESCRIÇÃO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> LOCAL </font></b></td> \n" +
                " <td align='center'><b><font color='white'> STATUS </font></b></td> \n" +
                " </tr> \n" +
                " \n";

        String data;
        String hora;
        String HoraIni;
        String HoraFim;
        String num1;
        String dataHoraIni;
        String dataHoraFim;
        String evento;
        String descricao;
        String local;
        String status;
        int i = 1;
        int numPag = 1;
        String cor;
        while(true)
        {
            try {
                if (!resultadoBanco.next()) break;
            } catch(Exception e) {
                LOGGER.warn("Tabela de resultado vazia", e.getMessage());
                fecharConexão(resultadoBanco, conn);
                throw new NenhumResultadoException("Nenhum resultado foi encontrado");
            }
            /* Pegando variaveis do banco */
            try {
                num1 = Integer.toString(i);
                dataHoraIni = (resultadoBanco.getString("evt_inicio"));
                dataHoraFim = (resultadoBanco.getString("evt_fim"));
                evento = (resultadoBanco.getString("evt_tema"));
                descricao = (resultadoBanco.getString("evt_desc"));
                local = (resultadoBanco.getString("evt_local"));
                status = (resultadoBanco.getString("evt_status"));

                /* Manipulando visualização data hora inicio */
                String fatia[] = dataHoraIni.split(" "); // Pegando data inicio
                String dataC = fatia[0];
                hora = fatia[1];
                String[] fatia1 = hora.split(Pattern.quote(".")); // Pegando hora inicio
                HoraIni = fatia1[0];
                String fatia2[] = dataC.split("-"); // Trocando padrão data para DD/MM/YYYY
                data = fatia2[2] + "/" + fatia2[1] + "/" + fatia2[0];

                /* Manipulando visualização data hora inicio */
                String fatia3[] = dataHoraFim.split(" "); // Pegando data fim
                dataFim = fatia3[0];
                hora = fatia3[1];
                String[] fatia4 = hora.split(Pattern.quote(".")); // Pegando hora fim
                HoraFim = fatia4[0];

                cor = getCor(i);

                /* Montando tabela para exibição dos dados */
                htmlText += "<tr bgcolor='" + cor + "'> \n" +
                        " <td align='center'>" + data + "</td> \n" +
                        " <td align='center'>" + HoraIni + "</td> \n" +
                        " <td align='center'>" + HoraFim + "</td> \n" +
                        " <td align='center'>" + evento + "</td> \n" +
                        " <td align='center'>" + descricao + "</td> \n" +
                        " <td align='center'>" + local + "</td> \n" +
                        " <td align='center'>" + status + "</td> \n" +
                        " </tr> \n";

                i++;
            } catch (SQLException e) {
                LOGGER.error("Erro ao solicitar coluna do banco");
                LOGGER.info(e.getMessage());
            }
        }

        fecharConexão(resultadoBanco, conn);

        htmlText += "</table>"
                + "</body> \n </html> \n";

        /* Criando novo documento pdf para relatório */

        String nomeArquivo = getNomeArquivo(false, true);

        /* Convertendo String para HTML e salvando no arquivo final */
        ResponseEntity<byte[]> response = getResponseEntity(htmlText, nomeArquivo);

        return response;
    }

    private String getCor(int i) {
        String cor;
        if (i % 2 == 0) {
            cor = "#DCDCDC";
        } else {
            cor = "#C0C0C0";
        }
        return cor;
    }

    public ResponseEntity<byte[]> fetchRelatorioUsuario(Long usuarioId) throws NenhumResultadoException {

        Connection conn = null;
        ResultSet resultadoBanco = null;
        Statement stm;
        try {
            conn = DBConexao.abrirConexao();
            stm = conn.createStatement();
            String query = "";
            query = usuarioId != 0 ? " WHERE evt_usr_id = " + usuarioId:" WHERE evt_usr_id <> 0";

            String sql = " SELECT evt_id, usr_nome, evt_criacao, evt_desc, evt_fim, evt_inicio, evt_local, evt_max_part, evt_obs, evt_status, evt_tema, evt_total_part, evt_usr_id " +
                    " FROM eventos " +
                    " INNER JOIN usuarios ON usuarios.usr_id = eventos.evt_usr_id " +
                    query +
                    " ORDER BY evt_inicio, usr_nome ";

            resultadoBanco = stm.executeQuery(sql);
        } catch(Exception e) {
            LOGGER.error("Erro ao criar query");
            LOGGER.info(e.getMessage());
        }

        String data;
        String hora;
        String HoraIni;
        String HoraFim;
        String htmlText1 = "";
        String htmlText2 = "";
        String htmlText3 = "";
        String usuario_nome = "";
        String dataHoraIni;
        String dataHoraFim;
        String evento;
        String descricao;
        String local;
        String status;

        int i = 1;
        String cor;

        while(true)
        {
            try {
                if (!resultadoBanco.next()) break;
            } catch (SQLException e) {
                LOGGER.warn("Tabela de resultado vazia", e.getMessage());
                fecharConexão(resultadoBanco, conn);
                throw new NenhumResultadoException("Nenhum resultado foi encontrado");
            }
            /* Pegando variaveis do banco */
            try {
                dataHoraIni = (resultadoBanco.getString("evt_inicio"));
                dataHoraFim = (resultadoBanco.getString("evt_fim"));
                evento = (resultadoBanco.getString("evt_tema"));
                descricao = (resultadoBanco.getString("evt_desc"));
                local = (resultadoBanco.getString("evt_local"));
                status = (resultadoBanco.getString("evt_status"));
                usuario_nome = (resultadoBanco.getString("usr_nome"));
                /* Manipulando visualização data hora inicio */
                String fatia[] = dataHoraIni.split(" "); // Pegando data inicio
                String dataC = fatia[0];
                hora = fatia[1];
                String[] fatia1 = hora.split(Pattern.quote(".")); // Pegando hora inicio
                HoraIni = fatia1[0];
                String fatia2[] = dataC.split("-"); // Trocando padrão data para DD/MM/YYYY
                data = fatia2[2]+"/"+fatia2[1]+"/"+fatia2[0];

                /* Manipulando visualização data  */
                String fatia3[] = dataHoraFim.split(" "); // Pegando data fim
                hora = fatia3[1];
                String[] fatia4 = hora.split(Pattern.quote(".")); // Pegando hora fim
                HoraFim = fatia4[0];

                cor = getCor(i);

                /* Montando tabela para exibição dos dados */
                htmlText2 += "<tr bgcolor='"+cor+"'> \n" +
                        " <td align='center'>"+ data + "</td> \n" +
                        " <td align='center'>"+ HoraIni + "</td> \n" +
                        " <td align='center'>"+ HoraFim + "</td> \n";
                if(usuarioId == 0){htmlText2 += " <td align='center'>"+ usuario_nome + "</td> \n";}
                htmlText2 += " <td align='center'>"+ evento + "</td> \n" +
                        " <td align='center'>"+ descricao + "</td> \n" +
                        " <td align='center'>"+ local + "</td> \n" +
                        " <td align='center'>"+ status + "</td> \n" +
                        " </tr> \n" +
                        "\n";
                i++;
            } catch (SQLException e) {
                LOGGER.error("Erro ao solicitar coluna do banco");
                LOGGER.info(e.getMessage());
            }
        }

        fecharConexão(resultadoBanco, conn);

        String titulo;
        String colspan;
        if(usuarioId == 0) {
            titulo = "TODOS USUÁRIOS";
            colspan = "8";
        } else {
            titulo = usuario_nome.toUpperCase();
            colspan = "7";
        }

        htmlText1 = "<html> \n" +
                "<body> \n" +
                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " <tr bgcolor='#4169E1'><td colspan='"+colspan+"' align='center'> \n" +
                "<img src='./endurance_logoB.png' width='70' height='50'> \n" +
                "<font color='white' size='+2'><b>  RELATÓRIO DE EVENTOS DE "+titulo+" </b></font></td></tr>\n " +
                " <tr bgcolor='#4169E1'> \n" +
                " <td align='center'><b><font color='white'> DATA </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA INICIO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA TÉRMINO </font></b></td> \n";
        if(usuarioId == 0){htmlText1 += " <td align='center'><b><font color='white'> USUÁRIO </font></b></td> \n";}
        htmlText1 += " <td align='center'><b><font color='white'> EVENTO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> DESCRIÇÃO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> LOCAL </font></b></td> \n" +
                " <td align='center'><b><font color='white'> STATUS </font></b></td> \n" +
                " </tr> \n" +
                " \n";

        htmlText3 = "</table>"
                + "</body> \n </html> \n";

        String htmlText = htmlText1+htmlText2+htmlText3;

        /* Criando novo documento pdf para relatório */

        String nomeArquivo = getNomeArquivo(true, false);

        /* Convertendo String para HTML e salvando no arquivo final */

        ResponseEntity<byte[]> response = getResponseEntity(htmlText, nomeArquivo);

        return response;
    }

    public ResponseEntity<byte[]> fetchRelatorioVacinados(String vacinados) {

        Connection conn = null;
        ResultSet resultadoBanco = null;
        Statement stm;
        String sql = "";
        try {
            conn = DBConexao.abrirConexao();
            stm = conn.createStatement();

            if(vacinados.equals("1")){
                sql = " SELECT usr_nome, usr_sobrenome, usr_email, usr_vacina, usr_ativo FROM USUARIOS " +
                        " WHERE usr_vacina IS NOT NULL " +
                        " ORDER BY usr_nome ";
            } else {
                sql = " SELECT usr_nome, usr_sobrenome, usr_email, usr_vacina, usr_ativo FROM USUARIOS " +
                        " ORDER BY usr_nome ";
            }

            resultadoBanco = stm.executeQuery(sql);
        } catch(Exception e) {
            LOGGER.error("Erro ao criar query");
            LOGGER.info(e.getMessage());
        }


        int qtdVacinados = 0;
        int qtdUsuarios = 0;
        ResultSet numVacinados;
        ResultSet numUsuarios;
        Statement stm1 = null;
        Statement stm2 = null;

        try {
            stm1 = conn.createStatement();
            stm2 = conn.createStatement();
        } catch(Exception e) {
            LOGGER.error("Erro ao criar query");
            LOGGER.info(e.getMessage());
        }

        try {
            String sql1 = " SELECT COUNT(usr_vacina) FROM USUARIOS "; //QTD de usuarios vacinados
            numVacinados = stm1.executeQuery(sql1);
            while(numVacinados.next()){ qtdVacinados = numVacinados.getInt("COUNT(usr_vacina)");}

            String sql2 = " SELECT COUNT(usr_email) FROM USUARIOS "; //QTD de usuarios no sistema
            numUsuarios = stm2.executeQuery(sql2);
            while(numUsuarios.next()){ qtdUsuarios = numUsuarios.getInt("COUNT(usr_email)");}
        } catch(SQLException e) {
            LOGGER.error("Erro na execução da query");
            LOGGER.info(e.getMessage());
        }

        int i = 1;
        int numPag = 1;
        String cor = "#C0C0C0";
        String htmlText1 = "";
        String htmlText2 = "";
        String htmlText3 = "";
        try {
            while(resultadoBanco.next())
            {
                /* Pegando variaveis do banco */
                String usr_nome = (resultadoBanco.getString("usr_nome"));
                String usr_nome2 = (resultadoBanco.getString("usr_sobrenome"));
                String usr_email = (resultadoBanco.getString("usr_email"));
                int usr_status = (resultadoBanco.getInt("usr_ativo"));
                String usr_vacina = (resultadoBanco.getString("usr_vacina"));

                String status = "";
                if(usr_status == 1){ status = " Ativo ";}

                String vacina = "";
                if(usr_vacina != null){vacina = " Vacinado ";}
                else{vacina = " Não vacinado ";}

                cor = getCor(i);

                /* Montando tabela para exibição dos dados */
                htmlText2 += "<tr bgcolor='"+cor+"'> \n" +
                        " <td >"+usr_nome+" "+usr_nome2+" </td> \n" +
                        " <td align='center'>"+ usr_email + "</td> \n" +
                        " <td align='center'>"+ status + "</td> \n" +
                        " <td align='center'>"+ vacina + "</td> \n" +
                        " </tr> \n" +
                        "\n";
                i++;
            }
        } catch(SQLException e) {
            LOGGER.error("Erro ao solicitar coluna do banco");
            LOGGER.info(e.getMessage());
        }

        fecharConexão(resultadoBanco, conn);

        int nVacinados = qtdUsuarios-qtdVacinados;
        htmlText1 = "<html> \n" +
                "<body> \n" +
                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " \n" +
                " <tr bgcolor='#4169E1'>\n" +
                " <td colspan='4' align='center' valign='middle'> \n" +
                " <img src='./endurance_logoB.png' width='70' height='50'> \n" +
                " <font color='white' size='+2'><b>  RELATÓRIO DE USUARIOS E VACINA </b></font>\n" +
                " </td>\n" +
                " </tr>\n " +
                " \n" +
                " <tr bgcolor='#4169E1'>\n" +
                " <td colspan='4'><font color='white'><b>" +
                " Total de Usuários do Sistema: "+qtdUsuarios+" <br>" +
                " Total de Usuários Vacinados: "+qtdVacinados+" <br>" +
                " Total de Usuários Não Vacinados "+nVacinados+
                " </b></font></td>" +
                " </tr>\n" +
                " <tr bgcolor='#4169E1'> \n" +
                " <td align='center'><b><font color='white'> USUÁRIO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> EMAIL </font></b></td> \n" +
                " <td align='center'><b><font color='white'> STATUS </font></b></td> \n" +
                " <td align='center'><b><font color='white'> VACINA </font></b></td> \n" +
                " </tr> \n" +
                " \n";

        htmlText3 = "</table>"
                + "</body> \n </html> \n";

        String htmlText = htmlText1+htmlText2+htmlText3;

        /* Criando novo documento pdf para relatório */

        String nomeArquivo = getNomeArquivo(false, false);

        /* Convertendo String para HTML e salvando no arquivo final */

        ResponseEntity<byte[]> response = getResponseEntity(htmlText, nomeArquivo);

        return response;
    }

    private ResponseEntity<byte[]> getResponseEntity(String htmlText, String nomeArquivo) {

        Path relatorioDir;
        String caminhoArquivo = null;
        try {
            relatorioDir = Paths.get(USER_FOLDER + RELATORIOS_DIR).toAbsolutePath().normalize();
            if(!Files.exists(relatorioDir)) {
                Files.createDirectories(relatorioDir);
                LOGGER.info(DIRECTORY_CREATED);
            }
            caminhoArquivo = relatorioDir + nomeArquivo;
            Files.deleteIfExists(Paths.get(caminhoArquivo));
            FileOutputStream stream = new FileOutputStream(caminhoArquivo);
            HtmlConverter.convertToPdf(htmlText, stream);
            stream.close();
        } catch(Exception e) {
            LOGGER.error("Erro ao converter HTML para PDF");
            LOGGER.info(e.getMessage());
        }

        Path path = Paths.get(caminhoArquivo);
        byte[] pdfContents = null;
        try {
            pdfContents = Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error("Erro ao ler PDF",e.getMessage());
            LOGGER.info(e.getMessage());
        }

        HttpHeaders headers = getHttpHeaders(nomeArquivo);
        ResponseEntity<byte[]> response = new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        return response;
    }

    private HttpHeaders getHttpHeaders(String nomeArquivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData(nomeArquivo, nomeArquivo);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    private String getNomeArquivo(boolean usuario, boolean periodo) {
        StringBuilder nomeArquivo = new StringBuilder();
        if(usuario) {
            nomeArquivo.append(NOME_RELATORIO_USUARIO);
        } else if(periodo) {
            nomeArquivo.append(NOME_RELATORIO_PERIODO);
        } else {
            nomeArquivo.append(NOME_RELATORIO_VACINA);
        }

        nomeArquivo.append(PDF_EXTENSION);

        return nomeArquivo.toString();
    }

    private void fecharConexão(ResultSet resultadoBanco, Connection conn) {
        try {
            resultadoBanco.close();
        } catch (SQLException e) {
            LOGGER.error("Erro ao solicitar coluna do banco");
            LOGGER.info(e.getMessage());
        }
        try {
            conn.close();
        } catch(SQLException e) {
            LOGGER.error("Erro ao fechar conexão com o banco");
            LOGGER.info(e.getMessage());
        }
    }
}
