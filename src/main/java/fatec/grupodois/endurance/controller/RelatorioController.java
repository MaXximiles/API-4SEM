package fatec.grupodois.endurance.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import fatec.grupodois.endurance.DBConexao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/relatorios")
public class RelatorioController
{
    /* Relatório de eventos por periodo */
    @GetMapping(path = "/eventos_periodo")
    public void Relat_periodo(@RequestParam(value = "datainicio", required = false) String datainicio,
                       @RequestParam(value= "datafim", required = false) String datafim) throws Exception
    {
        Connection conn = null;
        ResultSet resultadoBanco = null;
        conn = DBConexao.abrirConexao();
        Statement stm = conn.createStatement();

        String sql = " SELECT evt_id, evt_criacao, evt_desc, evt_fim, evt_inicio, evt_local, evt_max_part, evt_obs, evt_status, evt_tema, evt_total_part, evt_usr_id " +
                " FROM eventos " +
                " WHERE evt_inicio " +
                " BETWEEN TO_DATE('"+datainicio+"','DD/MM/YYYY') AND TO_DATE ('"+datafim+"','DD/MM/YYYY') ORDER BY evt_inicio ";
        resultadoBanco = stm.executeQuery(sql);

        String htmlText = "<html> \n" +
                "<body> \n" +
               
                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " <tr bgcolor='#4169E1'><td colspan='7' align='center'> \n" +
                "<img src='./endurance_logo.png' width='50' height='50'> \n" +
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

        int i = 1;
        int numPag = 1;
        String cor = "#C0C0C0";
        while(resultadoBanco.next())
        {
            /* Pegando variaveis do banco */
            String num1 = Integer.toString(i);
            String dataHoraIni = (resultadoBanco.getString("evt_inicio"));
            String dataHoraFim = (resultadoBanco.getString("evt_fim"));
            String evento = (resultadoBanco.getString("evt_tema"));
            String descricao = (resultadoBanco.getString("evt_desc"));
            String local = (resultadoBanco.getString("evt_local"));
            String status = (resultadoBanco.getString("evt_status"));

            /* Manipulando visualização data hora inicio */
            String fatia[] = dataHoraIni.split(" "); // Pegando data inicio
            String dataC = fatia[0];
            hora = fatia[1];
            String[] fatia1 = hora.split(Pattern.quote(".")); // Pegando hora inicio
            HoraIni = fatia1[0];
            String fatia2[] = dataC.split("-"); // Trocando padrão data para DD/MM/YYYY
            data = fatia2[2]+"/"+fatia2[1]+"/"+fatia2[0];

            /* Manipulando visualização data hora inicio */
            String fatia3[] = dataHoraFim.split(" "); // Pegando data fim
            datafim = fatia3[0];
            hora = fatia3[1];
            String[] fatia4 = hora.split(Pattern.quote(".")); // Pegando hora fim
            HoraFim = fatia4[0];
            
            if(i % 2 == 0) {cor = "#DCDCDC";}
            else {cor = "#C0C0C0";}
            
            /* Montando tabela para exibição dos dados */
            htmlText += "<tr bgcolor='"+cor+"'> \n" +
                    " <td align='center'>"+ data + "</td> \n" +
                    " <td align='center'>"+ HoraIni + "</td> \n" +
                    " <td align='center'>"+ HoraFim + "</td> \n" +
                    " <td align='center'>"+ evento + "</td> \n" +
                    " <td align='center'>"+ descricao + "</td> \n" +
                    " <td align='center'>"+ local + "</td> \n" +
                    " <td align='center'>"+ status + "</td> \n" +
                    " </tr> \n";
            
            i++;
        }

        htmlText += "</table>"
        		+ "</body> \n </html> \n";

        /* Criando novo documento pdf para relatório */
        Document document = new Document();
        document.open();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        String dataTitulo = dateFormat.format(date);

        File pasta = new File("C:\\endurance");
        pasta.mkdir();

        /* Convertendo String para HTML e salvando no arquivo final */
        HtmlConverter.convertToPdf(htmlText, new FileOutputStream("./src/tempArq/Relatorio_Eventos_Periodo_"+dataTitulo+".pdf"));
        /* ******************************************************************************************
        *  MUDAR PARA SALVAR ONDE O USUÁRIO DESEJAR *************************************************
        ******************************************************************************************* */
        document.close();
    }

    /* Relatório de eventos por usuario */
    @GetMapping(path = "/eventos_usuario")
    public void Relat_usuario(@RequestParam(value= "usuario_id", required = false) int usuario_id) throws Exception
    {
        Connection conn = null;
        ResultSet resultadoBanco = null;
        conn = DBConexao.abrirConexao();
        Statement stm = conn.createStatement();

        String query = "";
        if(usuario_id == 0 ){ query = " WHERE evt_usr_id <> 0";}
        else{ query = " WHERE evt_usr_id = " + usuario_id; }


        String sql = " SELECT evt_id, usr_nome, evt_criacao, evt_desc, evt_fim, evt_inicio, evt_local, evt_max_part, evt_obs, evt_status, evt_tema, evt_total_part, evt_usr_id " +
                " FROM eventos " +
                " INNER JOIN usuarios ON usuarios.usr_id = eventos.evt_usr_id " +
                query +
                " ORDER BY evt_inicio, usr_nome ";
        resultadoBanco = stm.executeQuery(sql);

        String data;
        String hora;
        String HoraIni;
        String HoraFim;
        String htmlText1 = "";
        String htmlText2 = "";
        String htmlText3 = "";
        String usuario_nome = "";

        int i = 1;
        int numPag = 1;
        String cor = "#C0C0C0";
        while(resultadoBanco.next())
        {
            /* Pegando variaveis do banco */
            String num1 = Integer.toString(i);
            String dataHoraIni = (resultadoBanco.getString("evt_inicio"));
            String dataHoraFim = (resultadoBanco.getString("evt_fim"));
            String evento = (resultadoBanco.getString("evt_tema"));
            String descricao = (resultadoBanco.getString("evt_desc"));
            String local = (resultadoBanco.getString("evt_local"));
            String status = (resultadoBanco.getString("evt_status"));
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

            if(i % 2 == 0) {cor = "#DCDCDC";}
            else {cor = "#C0C0C0";}

            /* Montando tabela para exibição dos dados */
            htmlText2 += "<tr bgcolor='"+cor+"'> \n" +
                    " <td align='center'>"+ data + "</td> \n" +
                    " <td align='center'>"+ HoraIni + "</td> \n" +
                    " <td align='center'>"+ HoraFim + "</td> \n";
            if(usuario_id == 0){htmlText2 += " <td align='center'>"+ usuario_nome + "</td> \n";}
            htmlText2 += " <td align='center'>"+ evento + "</td> \n" +
                    " <td align='center'>"+ descricao + "</td> \n" +
                    " <td align='center'>"+ local + "</td> \n" +
                    " <td align='center'>"+ status + "</td> \n" +
                    " </tr> \n" +
                    "\n";
            i++;
        }

        String titulo;
        String colspan;
        if(usuario_id == 0)
        {
            titulo = "TODOS USUÁRIOS";
            colspan = "8";
        }
        else
        {
            titulo = usuario_nome.toUpperCase();
            colspan = "7";
        }

        htmlText1 = "<html> \n" +
                "<body> \n" +
                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " <tr bgcolor='#4169E1'><td colspan='"+colspan+"' align='center'> \n" +
                "<img src='./endurance_logo.png' width='50' height='50'> \n" +
                "<font color='white' size='+2'><b>  RELATÓRIO DE EVENTOS DE "+titulo+" </b></font></td></tr>\n " +
                " <tr bgcolor='#4169E1'> \n" +
                " <td align='center'><b><font color='white'> DATA </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA INICIO </font></b></td> \n" +
                " <td align='center'><b><font color='white'> HORA TÉRMINO </font></b></td> \n";
        if(usuario_id == 0){htmlText1 += " <td align='center'><b><font color='white'> USUÁRIO </font></b></td> \n";}
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
        Document document = new Document();
        document.open();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        String dataTitulo = dateFormat.format(date);

        File pasta = new File("C:\\endurance");
        pasta.mkdir();



        /* Convertendo String para HTML e salvando no arquivo final */
        HtmlConverter.convertToPdf(htmlText, new FileOutputStream("./src/tempArq/Relatorio_eventos_usuario_"+dataTitulo+".pdf"));
        /* ******************************************************************************************
         *  MUDAR PARA SALVAR ONDE O USUÁRIO DESEJAR *************************************************
         ******************************************************************************************* */
        document.close();
    }


    /* Relatório de usuarios vacina */
    @GetMapping(path = "/eventos_vacina")
    public void Relat_vacina(@RequestParam(value= "vacinados", required = false) int vacinados) throws Exception
    {
        Connection conn = null;
        ResultSet resultadoBanco = null;
        conn = DBConexao.abrirConexao();
        Statement stm = conn.createStatement();

        String sql;
        if(vacinados == 1)//TODOS OS USUARIOS QUE SÃO VACINADOS
        {
            sql = " SELECT usr_nome, usr_sobrenome, usr_email, usr_vacina, usr_ativo FROM USUARIOS " +
                  " WHERE usr_vacina IS NOT NULL " +
                  " ORDER BY usr_nome ";
        }
        else//TODOS OS USUARIOS
        {
            sql = " SELECT usr_nome, usr_sobrenome, usr_email, usr_vacina, usr_ativo FROM USUARIOS " +
                  " ORDER BY usr_nome ";
        }
        resultadoBanco = stm.executeQuery(sql);

        int qtdVacinados = 0;
        int qtdUsuarios = 0;
        ResultSet numVacinados;
        Statement stm1 = conn.createStatement();
        ResultSet numUsuarios;
        Statement stm2 = conn.createStatement();

        String sql1 = " SELECT COUNT(usr_vacina) FROM USUARIOS "; //QTD de usuarios vacinados
        numVacinados = stm1.executeQuery(sql1);
        while(numVacinados.next()){ qtdVacinados = numVacinados.getInt("COUNT(usr_vacina)");}

        String sql2 = " SELECT COUNT(usr_email) FROM USUARIOS "; //QTD de usuarios no sistema
        numUsuarios = stm2.executeQuery(sql2);
        while(numUsuarios.next()){ qtdUsuarios = numUsuarios.getInt("COUNT(usr_email)");}

        int i = 1;
        int numPag = 1;
        String cor = "#C0C0C0";
        String htmlText1 = "";
        String htmlText2 = "";
        String htmlText3 = "";
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

            if(i % 2 == 0) {cor = "#DCDCDC";}
            else {cor = "#C0C0C0";}

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
        int nVacinados = qtdUsuarios-qtdVacinados;
        htmlText1 = "<html> \n" +
                "<body> \n" +
                " \n" +
                " <table cellspacing='0' cellpadding='0' border='1' width='100%' style='border-color:Black;'> \n" +
                " \n" +
                " <tr bgcolor='#4169E1'>\n" +
                " <td colspan='4' align='center'> \n" +
                " <img src='./endurance_logo.png' width='50' height='50'> \n" +
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
        Document document = new Document();
        document.open();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        String dataTitulo = dateFormat.format(date);

        File pasta = new File("C:\\endurance");
        pasta.mkdir();

        /* Convertendo String para HTML e salvando no arquivo final */
        HtmlConverter.convertToPdf(htmlText, new FileOutputStream("./src/tempArq/Relatorio_Vacina_"+dataTitulo+".pdf"));
        /* ******************************************************************************************
         *  MUDAR PARA SALVAR ONDE O USUÁRIO DESEJAR *************************************************
         ******************************************************************************************* */
        document.close();
    }



}
