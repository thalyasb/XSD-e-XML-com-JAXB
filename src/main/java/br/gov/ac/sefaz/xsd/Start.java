package br.gov.ac.sefaz.xsd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import br.gov.ac.sefaz.classes.Pedido;
import br.gov.ac.sefaz.classes.TipoProduto;
import br.gov.ac.sefaz.validador.Validador;

public class Start {
	public static void main(String[] args) {
		Pedido pedido = new Pedido();
		List<TipoProduto> tipos = new ArrayList<>();

		TipoProduto tipo1 = new TipoProduto();
		tipo1.setCodigo("1");
		tipo1.setNome("Cola Branca");
		tipo1.setDescricao("Material Escolar");
		tipos.add(tipo1);

		TipoProduto tipo2 = new TipoProduto();
		tipo2.setCodigo("2");
		tipo2.setNome("Chocolate");
		tipo2.setDescricao("Docinho gostoso");
		tipos.add(tipo2);

		TipoProduto tipo3 = new TipoProduto();
		tipo3.setCodigo("3");
		tipo3.setNome("Tesoura");
		tipo3.setDescricao("Arma branca");
		tipos.add(tipo3);

		pedido.setProduto(tipos);

		System.out.println(marshal(pedido));

		marshalToFile(pedido, "C:\\teste\\pedido-jaxb.xml");

		String pedidoxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + "<pedido>\r\n"
				+ "    <produto codigo=\"1\">\r\n" + "        <nome>Cola Branca</nome>\r\n"
				+ "        <descricao>Material Escolar</descricao>\r\n" + "    </produto>\r\n"
				+ "    <produto codigo=\"2\">\r\n" + "        <nome>Chocolate</nome>\r\n"
				+ "        <descricao>Docinho gostoso</descricao>\r\n" + "    </produto>\r\n"
				+ "    <produto codigo=\"3\">\r\n" + "        <nome>Tesoura</nome>\r\n"
				+ "        <descricao>Arma branca</descricao>\r\n" + "    </produto>\r\n" + "</pedido>\r\n";

		// populando objeto pelo xml string
		Pedido pedido2 = (Pedido)unmarshal(Pedido.class, pedidoxml);
		System.out.println(pedido2.toString());

		// populando objeto pelo arquivo
		Pedido pedido3 = (Pedido) unmarshalFromFile(Pedido.class, "C:\\teste\\pedido-jaxb.xml");
		System.out.println(pedido3.toString());

		Validador validador = new Validador();
		
		String caminhoXsd = "C:\\Users\\Administrador\\eclipse-workspace\\exercicios\\src\\pedido.xsd";
		validador.validator(caminhoXsd, pedido3);
		validador.validator(caminhoXsd, pedido2);
		validador.validator(caminhoXsd, pedido);
	}

	public static String marshalToFile(Object object, String fileName) {
		final StringWriter out = new StringWriter();
		JAXBContext context = null;
		Marshaller marshaller = null;
		try {
			context = JAXBContext.newInstance(object.getClass());
			marshaller = context.createMarshaller();
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, new StreamResult(out));
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Writer writer = null;
		try {
			writer = new FileWriter(fileName);
			marshaller.marshal(object, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.getMessage();
			}
		}

		return out.toString();
	}

	/**
	 * Converte o objeto em uma String com estrutura XML.
	 * 
	 * @param object objeto a ser convertido em XML.
	 * @return String contendo a estrutura XML.
	 */
	public static String marshal(Object object) {
		final StringWriter out = new StringWriter();
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, new StreamResult(out));
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	/**
	 * Método unmarshal para popular objeto a partir de uma String XML (usado com a execução unmarshal acima)
	 * @param clazz recebe o string xml
	 * @param stringXml leitura do XML (declarado na execução acima) 
	 * @return popula objeto
	 */
	public static Object unmarshal(Class<?> clazz, String stringXml) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new StringReader(stringXml)));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Método UnmarshalFromFile para popular objeto a partir de um arquivo físico Xml (usado com a execução unmarshal acima)
	 * @param clazz recebe o arquivo
	 * @param fileXml chama o arquivo fisico 
	 * @return popular o objeto
	 */
	public static Object unmarshalFromFile(Class<?> clazz, String fileXml) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(
                    new FileInputStream(fileXml)
            );
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
