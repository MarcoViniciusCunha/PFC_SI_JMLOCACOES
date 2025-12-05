package com.nozama.aluguel_veiculos.services;
import com.nozama.aluguel_veiculos.domain.Rental;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
    public byte[] gerarContrato(Rental rental) {
        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PDPageContentStream content = new PDPageContentStream(document, page);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            float y = 750;

            y = writeLine(content, "CONTRATO DE LOCAÇÃO DE VEÍCULO", 20, y, true);
            y -= 20;

            y = writeLine(content, "LOCATÁRIO", 14, y, true);
            y = writeLine(content, "Nome: " + rental.getCustomer().getNome(), 12, y, false);
            y = writeLine(content, "CPF: " + rental.getCustomer().getCpf(), 12, y, false);
            y = writeLine(content, "CNH: " + rental.getCustomer().getCnh(), 12, y, false);
            y = writeLine(content, "Email: " + rental.getCustomer().getEmail(), 12, y, false);
            y = writeLine(content, "Telefone: " + rental.getCustomer().getTelefone(), 12, y, false);
            y -= 15;

            y = writeLine(content, "VEÍCULO LOCADO", 14, y, true);
            y = writeLine(content, "Placa: " + rental.getVehicle().getPlaca(), 12, y, false);
            y = writeLine(content, "Modelo: " + rental.getVehicle().getModel().getNome(), 12, y, false);
            y = writeLine(content, "Marca: " + rental.getVehicle().getBrand().getNome(), 12, y, false);
            y = writeLine(content, "Ano: " + rental.getVehicle().getAno(), 12, y, false);
            y = writeLine(content, "Cor: " + rental.getVehicle().getColor().getNome(), 12, y, false);
            y -= 15;

            y = writeLine(content, "PERÍODO DA LOCAÇÃO", 14, y, true);
            y = writeLine(content, "Data de retirada: " + rental.getStartDate().format(fmt), 12, y, false);
            y = writeLine(content, "Data prevista de devolução: " + rental.getEndDate().format(fmt), 12, y, false);
            y -= 15;

            y = writeLine(content, "CLÁUSULAS DO CONTRATO", 14, y, true);

            y = writeParagraph(content,
                    "CLÁUSULA 1 – OBJETO\n" +
                            "O presente contrato tem por objeto a locação do veículo acima descrito, de propriedade da LOCADORA, ao LOCATÁRIO, pelo período estipulado.\n\n" +
                            "CLÁUSULA 2 – PAGAMENTO\n" +
                            "O LOCATÁRIO pagará o valor total acordado, incluindo tarifas adicionais como multas, danos, combustível faltante e diárias extras.\n\n" +
                            "CLÁUSULA 3 – RESPONSABILIDADES DO LOCATÁRIO\n" +
                            "- Utilizar o veículo conforme as leis de trânsito.\n" +
                            "- Arcar com multas cometidas.\n" +
                            "- Devolver o veículo nas mesmas condições.\n" +
                            "- Informar imediatamente acidentes ou irregularidades.\n\n" +
                            "CLÁUSULA 4 – DEVOLUÇÃO\n" +
                            "O veículo deve ser devolvido no local combinado e com tanque no mesmo nível da retirada.\n\n" +
                            "CLÁUSULA 5 – FORO\n" +
                            "Fica eleito o foro da comarca do contrato.",
                    12,
                    y
            );

            // Assinaturas
            y -= 40;
            y = writeLine(content, "LOCADORA: ________________________________", 12, y, false);
            y -= 20;
            y = writeLine(content, "LOCATÁRIO: ________________________________", 12, y, false);

            content.close();

            document.save(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private float writeLine(PDPageContentStream content, String text, int fontSize, float y, boolean bold) throws IOException {
        content.beginText();
        content.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, fontSize);
        content.newLineAtOffset(40, y);
        content.showText(text);
        content.endText();
        return y - 14;
    }

    private float writeParagraph(PDPageContentStream content, String text, int fontSize, float y) throws IOException {
        String[] lines = text.split("\n");
        for (String line : lines) {
            y = writeLine(content, line, fontSize, y, false);
        }
        return y;
    }
}
