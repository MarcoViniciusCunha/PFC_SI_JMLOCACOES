package com.nozama.aluguel_veiculos.services;
import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Toll;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PdfService {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarContrato(Rental rental) {
        if (rental.getCustomer().getNome() == null || rental.getCustomer().getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Não é possível gerar contrato pois o cliente foi deletado");
        }

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
            y = writeLine(content, "CPF: "+ rental.getCustomer().getCpf(), 12, y, false);
            y = writeLine(content, "CNH: "+ rental.getCustomer().getCnh(), 12, y, false);
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
                            "O presente contrato tem por objeto a locação do veículo acima descrito, de propriedade da LOCADORA,\n ao LOCATÁRIO, pelo período estipulado.\n\n" +
                            "CLÁUSULA 2 – PAGAMENTO\n" +
                            "O LOCATÁRIO pagará o valor total acordado, incluindo tarifas adicionais como multas, danos,\n combustível faltante e diárias extras.\n\n" +
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

    public byte[] gerarComprovantePagamento(Payment pagamento) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = 750;

            y = writeLine(content, "COMPROVANTE DE PAGAMENTO", 20, y, true);
            y -= 15;

            // CNPJ da empresa e data/hora de emissão
            y = writeLine(content, "Empresa JMLocações - CNPJ: 12.345.678/0001-90", 12, y, false);
            y = writeLine(content, "Emitido em: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 12, y, false);

            String numeroComprovante = UUID.randomUUID().toString();
            y = writeLine(content, "Comprovante nº: " + numeroComprovante, 12, y, false);
            y -= 20;

            Rental rental = pagamento.getRental();
            List<Fine> multas = rental.getFines();
            List<Toll> pedagios = rental.getTolls();

            y = writeLine(content, "LOCATÁRIO", 14, y, true);
            y = writeLine(content, "Nome: " + rental.getCustomer().getNome(), 12, y, false);
            y = writeLine(content, "Email: " + rental.getCustomer().getEmail(), 12, y, false);
            y = writeLine(content, "Telefone: " + rental.getCustomer().getTelefone(), 12, y, false);
            y -= 15;

            y = writeLine(content, "VEÍCULO", 14, y, true);
            y = writeLine(content, "Placa: " + rental.getVehicle().getPlaca(), 12, y, false);
            y = writeLine(content, "Modelo: " + rental.getVehicle().getModel().getNome(), 12, y, false);
            y = writeLine(content, "Marca: " + rental.getVehicle().getBrand().getNome(), 12, y, false);
            y -= 15;

            y = writeLine(content, "DETALHES DO PAGAMENTO", 14, y, true);
            y = writeLine(content, "Data do pagamento: " + pagamento.getData_pagamento().format(fmt), 12, y, false);
            y = writeLine(content, "Valor pago: R$ " + pagamento.getValor().setScale(2, BigDecimal.ROUND_HALF_UP), 12, y, false);
            y = writeLine(content, "Status: " + pagamento.getStatus(), 12, y, false);
            y = writeLine(content, "Forma de pagamento: " + pagamento.getFormaPagto(), 12, y, false);
            y -= 3;
            if (pagamento.getParcelas() != null && pagamento.getParcelas() > 1) {
                y = writeLine(content, "PAGAMENTO PARCELADO", 12, y, true);
                y = writeLine(content, "Quantidade de parcelas: " + pagamento.getParcelas() + "x", 12, y, false);
            }
            y -= 30;

            if ((multas != null && !multas.isEmpty()) || (pedagios != null && !pedagios.isEmpty())) {
                y -= 15;
                y = writeLine(content, "OUTRAS TAXAS", 14, y, true);

                if (multas != null && !multas.isEmpty()) {
                    y = writeLine(content, "Multas:", 12, y, true);
                    for (Fine multa : multas) {
                        y = writeLine(content, "- " + multa.getDescricao() + " | Valor: R$ " +
                                multa.getValor().setScale(2, BigDecimal.ROUND_HALF_UP) + " | Data: " +
                                multa.getData_multa().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 12, y, false);
                    }
                }

                if (pedagios != null && !pedagios.isEmpty()) {
                    y -= 5; // pequeno espaço extra
                    y = writeLine(content, "Pedágios:", 12, y, true);
                    for (Toll pedagio : pedagios) {
                        y = writeLine(content, "- " + pedagio.getRodovia() + " - " + pedagio.getCidade() +
                                " | Valor: R$ " + pedagio.getValor().setScale(2, BigDecimal.ROUND_HALF_UP) +
                                " | Data: " + pedagio.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 12, y, false);
                    }
                }

                y -= 15; // espaço antes do rodapé
            }

            y = writeLine(content, "Empresa XYZ - Rua Exemplo, 123, São Paulo/SP", 10, y, false);
            y = writeLine(content, "Email: jmlocacoes@empresa.com | Telefone: (11) 1234-5678", 10, y, false);
            y = writeLine(content, "Este comprovante é válido para fins fiscais e contábeis.", 10, y, false);

            content.close();
            document.save(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF de pagamento", e);
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
